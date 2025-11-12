package com.gpadilla.mycar.service.pagos;

import com.gpadilla.mycar.dtos.factura.FacturaDto;
import com.gpadilla.mycar.entity.*;
import com.gpadilla.mycar.enums.EstadoFactura;
import com.gpadilla.mycar.enums.TipoDePago;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.FacturaMapper;
import com.gpadilla.mycar.repository.FacturaRepository;
import com.gpadilla.mycar.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public FacturaService(FacturaRepository repository, FacturaMapper mapper) {
        super("Factura", repository, mapper);
    }


    public FacturaDto buscarFacturaDto(Long id) {
        // Usa findDto() del BaseService, que ya aplica eliminado = false
        return findDto(id);
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
}
