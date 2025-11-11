package com.gpadilla.mycar.facade;

import com.gpadilla.mycar.dtos.alquiler.AlquilerCreateRequestDto;
import com.gpadilla.mycar.entity.Alquiler;
import com.gpadilla.mycar.entity.Auto;
import com.gpadilla.mycar.entity.Cliente;
import com.gpadilla.mycar.entity.Factura;
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

        // todo codigo promocion
        Factura factura = facturaService.crearFacturaDeAlquiler(alquiler, estadoFactura, request.getFormaDePago());

        // todo enviar pdf por mail si está pagada, si no, no

        // todo configurar recordario por mail y wp
    }
}
