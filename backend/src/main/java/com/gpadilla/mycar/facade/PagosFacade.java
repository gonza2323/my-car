package com.gpadilla.mycar.facade;

import com.gpadilla.mycar.dtos.pagos.PaymentResponse;
import com.gpadilla.mycar.entity.Alquiler;
import com.gpadilla.mycar.entity.Factura;
import com.gpadilla.mycar.enums.EstadoFactura;
import com.gpadilla.mycar.enums.EstadoPagoAlquiler;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.service.AlquilerService;
import com.gpadilla.mycar.service.pagos.FacturaService;
import com.gpadilla.mycar.service.pagos.MercadoPagoService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PagosFacade {

    private final AlquilerService alquilerService;
    private final MercadoPagoService mercadoPagoService;
    private final FacturaService facturaService;
    private final AlquilerFacade alquilerFacade;

    @Transactional
    public PaymentResponse generarLinkDePagoMPClientes(Long alquilerId, Long clienteId) throws MPException, MPApiException {
        Alquiler alquiler = alquilerService.find(alquilerId);
        validarAlquilerParaPagoCliente(alquiler, clienteId);

        Factura factura = facturaService.buscarFacturaDeAlquiler(alquilerId);
        validarFacturaNoPagada(factura);

        // todo usar promocion (buscar factura)
        String urlDePago = mercadoPagoService.createPreference(alquilerId, alquiler.getMonto());

        return PaymentResponse.builder()
                .alquilerId(alquilerId)
                .urlDePago(urlDePago)
                .status(EstadoPagoAlquiler.EN_PROCESO)
                .build();
    }

    @Transactional
    public void confirmarPagoMercadoPago(Long alquilerId) {
        Alquiler alquiler = alquilerService.find(alquilerId);
        validarAlquilerNoPagado(alquiler);

        Factura factura = facturaService.buscarFacturaDeAlquiler(alquilerId);
        validarFacturaNoPagada(factura);

        alquiler.setEstado(EstadoPagoAlquiler.PAGADO);
        factura.setEstado(EstadoFactura.PAGADA);

        // todo enviar factura por mail
    }

    private void validarAlquilerParaPagoCliente(Alquiler alquiler, Long clienteId) {
        validarAlquilerDeCliente(alquiler, clienteId);
        validarAlquilerNoPagado(alquiler);
    }

    private void validarAlquilerNoPagado(Alquiler alquiler) {
        if (alquiler.getEstado() == EstadoPagoAlquiler.PAGADO)
            throw new BusinessException("Este alquiler ya fue pagado");
    }

    private void validarFacturaNoPagada(Factura factura) {
        if (factura.getEstado() == EstadoFactura.PAGADA)
            throw new BusinessException("Este alquiler ya fue pagado");
    }

    private void validarAlquilerDeCliente(Alquiler alquiler, Long clienteId) {
        if (!clienteId.equals(alquiler.getCliente().getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
}
