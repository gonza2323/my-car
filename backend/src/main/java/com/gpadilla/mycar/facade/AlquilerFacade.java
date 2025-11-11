package com.gpadilla.mycar.facade;

import com.gpadilla.mycar.dtos.alquiler.AlquilerCreateRequestDto;
import com.gpadilla.mycar.entity.*;
import com.gpadilla.mycar.enums.EstadoFactura;
import com.gpadilla.mycar.enums.EstadoPagoAlquiler;
import com.gpadilla.mycar.enums.TipoDePago;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.service.*;
import com.gpadilla.mycar.service.pagos.FacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlquilerFacade {

    private final AlquilerService alquilerService;
    private final ClienteService clienteService;
    private final AutoService autoService;
    private final CaracteristicasAutoService caracteristicasAutoService;
    private final CostoAutoService costoAutoService;
    private final FacturaService facturaService;
    private final PromocionService promocionService;

    @Transactional
    public void registrarAlquiler(AlquilerCreateRequestDto request) {
        Cliente cliente = clienteService.find(request.getClienteId());

        List<Auto> vehiculos = autoService.encontrarVehiculosDisponiblesParaAlquiler(
                request.getCaracteristicaAutoId(),
                request.getFechaDesde(),
                request.getFechaHasta());

        if (vehiculos.isEmpty())
            throw new BusinessException("No hay vehículos disponibles de ese modelo en ese rango de fechas");
        Auto vehiculo = vehiculos.getFirst();

        Long cantDias = ChronoUnit.DAYS.between(request.getFechaDesde(), request.getFechaHasta()) + 1;
        Double costoPorDia = costoAutoService.buscarCostoDeModeloEnFecha(
                request.getCaracteristicaAutoId(),
                request.getFechaDesde())
                .getCostoTotal();

        EstadoPagoAlquiler estadoAlquiler =
                request.getFormaDePago() == TipoDePago.BILLETERA_VIRTUAL
                        ? EstadoPagoAlquiler.NO_PAGADO
                        : EstadoPagoAlquiler.PAGADO;

        Alquiler alquiler = alquilerService.registrarAlquiler(
                request.getFechaDesde(),
                request.getFechaHasta(),
                costoPorDia,
                costoPorDia * cantDias,
                estadoAlquiler,
                vehiculo,
                cliente);

        EstadoFactura estadoFactura =
                request.getFormaDePago() == TipoDePago.BILLETERA_VIRTUAL
                        ? EstadoFactura.SIN_DEFINIR
                        : EstadoFactura.PAGADA;

        Promocion promocion = null;

        // si el usuario mandó un código de descuento lo validamos
        if (request.getCodigoDescuento() != null && !request.getCodigoDescuento().isEmpty())
            promocion = promocionService.validarCodigoPromocion(request.getCodigoDescuento());

        Factura factura = facturaService.crearFacturaDeAlquiler(alquiler, estadoFactura, request.getFormaDePago(), promocion);

        if (estadoFactura == EstadoFactura.PAGADA) {
            // todo enviar pdf por mail si está pagada, si no, recién cuando la pague por MP
        }

        // todo configurar recordario por mail y wp
    }
}
