package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.factura.FacturaDto;
import com.gpadilla.mycar.entity.*;
import com.gpadilla.mycar.enums.EstadoFactura;
import com.gpadilla.mycar.enums.TipoDePago;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.FacturaMapper;
import com.gpadilla.mycar.pdf.PdfGenerator;
import com.gpadilla.mycar.repository.FacturaArchivoRepository;
import com.gpadilla.mycar.repository.FacturaRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Service
public class FacturaService extends BaseService<
        Factura,                  // Entidad
        Long,                     // ID
        FacturaRepository,        // Repositorio
        FacturaDto,               // DetailDto
        FacturaDto,               // SummaryDto
        FacturaDto,               // CreateDto
        FacturaDto,               // UpdateDto
        FacturaMapper             // Mapper
        > {

     final PdfGenerator pdfGenerator;
    private final FacturaArchivoRepository facturaArchivoRepository;

    public FacturaService(FacturaRepository repository, FacturaMapper mapper, PdfGenerator pdfGenerator, FacturaArchivoRepository facturaArchivoRepository) {
        super("Factura", repository, mapper);
        this.pdfGenerator = pdfGenerator;
        this.facturaArchivoRepository = facturaArchivoRepository;
    }

    // ✅ DTO interno para devolver factura + PDF
    @Getter
    @AllArgsConstructor
    public static class FacturaConPdf {
        private final Factura factura;
        private final byte[] pdfBytes;
    }

    @Transactional(readOnly = true)
    public byte[] generarFacturaPdfEnMemoria(Long facturaId) {
        // 1️⃣ Buscar la factura en BD
        Factura factura = repository.findByIdAndEliminadoFalse(facturaId)
                .orElseThrow(() -> new BusinessException("Factura no encontrada"));

        // 2️⃣ Convertir a DTO
        FacturaDto dto = mapper.toDto(factura);

        // 3️⃣ Generar PDF en memoria
        return pdfGenerator.generarFacturaPdf(dto);
    }


    // 🧩 Ejemplo de hook opcional: validaciones personalizadas
    @Override
    protected void validateCreate(FacturaDto dto) {
        if (dto.getTotalPagado() == null || dto.getTotalPagado() <= 0) {
            throw new IllegalArgumentException("El total pagado debe ser mayor a cero.");
        }
    }

    @Transactional
    public Factura crearFacturaDeAlquiler(Alquiler alquiler, EstadoFactura estado, TipoDePago tipoDePago, Promocion promocion) {
        Double total = alquiler.getMonto();
        if (promocion != null)
            total = total * (1 - promocion.getPorcentajeDescuento());

        Factura factura = Factura.builder()
                .numeroFactura(generarSiguienteNumeroDeFactura())
                .estado(estado)
                .fechaFactura(LocalDate.now())
                .totalPagado(total)
                .eliminado(false)
                .build();

        factura.setFormaDePago(FormaDePago.builder()
                .tipoDePago(tipoDePago)
                .observacion("")
                .build());

        factura.setDetalles(List.of(
                DetalleFactura.builder()
                        .factura(factura)
                        .alquiler(alquiler)
                        .subtotal(alquiler.getMonto())
                        .promocion(promocion)
                        .build()));

        return repository.save(factura);
    }

    @Transactional(readOnly = true)
    protected Long generarSiguienteNumeroDeFactura() {
        Long max = repository.findMaxNumeroFactura();
        return max == null ? 1L : max + 1;
    }

    @Transactional(readOnly = true)
    public Factura buscarFactura(Long id) {
        return repository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new BusinessException("Factura no encontrada"));
    }

    @Transactional(readOnly = true)
    public Factura buscarFacturaDeAlquiler(Long alquilerId) {
        return repository.buscarFacturaDeAlquiler(alquilerId)
                .orElseThrow(() -> new BusinessException("Factura no encontrada"));
    }

    @Transactional(readOnly = true)
    public byte[] generarFacturaPdfPorAlquiler(Long alquilerId) {
        Factura factura = repository.findFacturaCompletaPorAlquiler(alquilerId)
                .orElseThrow(() -> new BusinessException("No se encontró factura para el alquiler ID: " + alquilerId));

        FacturaDto dto = mapper.toDto(factura);
        return pdfGenerator.generarFacturaPdf(dto);
    }
}
