package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.auth.CurrentUser;
import com.gpadilla.mycar.dtos.pagos.ManualPaymentRequest;
import com.gpadilla.mycar.dtos.pagos.PaymentResponse;
import com.gpadilla.mycar.facade.PagosFacade;
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
@RequestMapping("/api/v1/admin/payments")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class AdminPagoController {
    private final PagosFacade pagosFacade;

    @PostMapping
    @PreAuthorize("hasAnyRole('JEFE', 'ADMINISTRATIVO')")
    public ResponseEntity<PaymentResponse> registrarPagoManual(
            @RequestBody ManualPaymentRequest request,
            @AuthenticationPrincipal CurrentUser user) {

        PaymentResponse response = pagosFacade.registrarPagoManual(request.getAlquilerId(), request.getMetodo());

        return ResponseEntity.ok(response);
    }
}
