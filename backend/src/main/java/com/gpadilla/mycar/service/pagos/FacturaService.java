package com.gpadilla.mycar.service.pagos;

import com.gpadilla.mycar.entity.*;
import com.gpadilla.mycar.enums.EstadoFactura;
import com.gpadilla.mycar.enums.TipoDePago;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.repository.FacturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacturaService {
    private final FacturaRepository facturaRepository;

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

        return facturaRepository.save(factura);
    }

    @Transactional(readOnly = true)
    protected Long generarSiguienteNumeroDeFactura() {
        Long max = facturaRepository.findMaxNumeroFactura();
        return max == null ? 1L : max + 1;
    }

    @Transactional(readOnly = true)
    public Factura buscarFactura(Long id) {
        return facturaRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new BusinessException("Factura no encontrada"));
    }

    @Transactional(readOnly = true)
    public Factura buscarFacturaDeAlquiler(Long alquilerId) {
        return facturaRepository.buscarFacturaDeAlquiler(alquilerId)
                .orElseThrow(() -> new BusinessException("Factura no encontrada"));
    }
}
