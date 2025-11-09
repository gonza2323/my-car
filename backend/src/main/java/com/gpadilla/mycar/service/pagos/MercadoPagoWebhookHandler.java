package com.gpadilla.mycar.service.pagos;

import com.gpadilla.mycar.dtos.pagos.MercadoPagoWebhook;
import com.gpadilla.mycar.facade.PagosFacade;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.payment.PaymentItem;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MercadoPagoWebhookHandler {

    private final PagosFacade pagosFacade;
    private final MercadoPagoService mercadoPagoService;

    @Async
    public void procesarNotificacionDePago(MercadoPagoWebhook webhook) throws Exception {
        if (!"payment".equals(webhook.getType())) {
            return;
        }
        Long id = Long.parseLong(webhook.getData().getId());

        System.out.println("Procesando pago con ID: " + id + "...");
        Payment payment = mercadoPagoService.getPaymentDetails(id);

        if (!payment.getStatus().equals("approved"))
            return;

        List<PaymentItem> items = payment.getAdditionalInfo().getItems();
        Long alquilerId = Long.parseLong(items.getFirst().getId());

        pagosFacade.confirmarPagoMercadoPago(alquilerId);
    }
}
