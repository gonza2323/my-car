package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.auth.CurrentUser;
import com.gpadilla.mycar.dtos.pagos.PaymentRequest;
import com.gpadilla.mycar.dtos.pagos.PaymentResponse;
import com.gpadilla.mycar.facade.PagosFacade;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class PagosController {
    private final PagosFacade pagosFacade;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<PaymentResponse> registrarPagoManual(
            @RequestBody PaymentRequest request,
            @AuthenticationPrincipal CurrentUser user) throws MPException, MPApiException {

        PaymentResponse response = pagosFacade.generarLinkDePagoMPClientes(request.getAlquilerId(), user.getId());

        return ResponseEntity.ok(response);
    }
}
