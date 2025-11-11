package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.dtos.pagos.MercadoPagoWebhook;
import com.gpadilla.mycar.service.pagos.MercadoPagoWebhookHandler;
import com.mercadopago.net.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/webhook/mercadopago")
@RequiredArgsConstructor
public class MercadoPagoWebhookController {

    private final MercadoPagoWebhookHandler webhookHandler;

    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestBody MercadoPagoWebhook webhook) {
        try {
            webhookHandler.procesarNotificacionDePago(webhook);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR");
        }
    }
}