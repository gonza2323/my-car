package com.gpadilla.mycar.controller;

import com.gpadilla.mycar.dtos.mensaje.MensajeDTO;
import com.gpadilla.mycar.dtos.mensaje.PromocionDTO;
import com.gpadilla.mycar.service.MensajeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/mensajes")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class MensajeController {

    private final MensajeService mensajeService;

    @PostMapping(value = "/promociones", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('JEFE','ADMINISTRATIVO')")
    public ResponseEntity<Map<String, String>> enviarPromocion(@Valid @RequestBody PromocionDTO dto) {
        mensajeService.enviarPromocionAsync(dto);
        return ResponseEntity.accepted().body(Map.of("status", "EN_PROCESO"));
    }

    @PostMapping(value = "/alquiler/{alquilerId}/factura", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('JEFE','ADMINISTRATIVO')")
    public ResponseEntity<MensajeService.ResultadoEnvio> enviarFactura(
            @PathVariable Long alquilerId,
            @RequestPart("dto") @Valid MensajeDTO dto,
            @RequestPart("adjuntoPdf") MultipartFile adjuntoPdf
    ) {
        dto.setEmpresaId(alquilerId);
        MensajeService.ResultadoEnvio resultado = mensajeService.enviarMensajeFacturaAlquiler(dto, adjuntoPdf);
        return ResponseEntity.ok(resultado);
    }


    // --- Envío de promoción a un cliente específico ---
    @PostMapping("/promociones/{clienteId}")
    public ResponseEntity<?> enviarPromocionACliente(
            @PathVariable Long clienteId,
            @RequestBody PromocionDTO dto
    ) {
        try {
            boolean enviado = mensajeService.procesarPromocionParaCliente(dto, clienteId);
            if (enviado) {
                return ResponseEntity.ok(Map.of(
                        "status", "OK",
                        "message", "Promoción enviada correctamente al cliente " + clienteId
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "status", "ERROR",
                        "message", "No se pudo enviar la promoción al cliente " + clienteId
                ));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "ERROR",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "ERROR",
                    "message", "Ocurrió un error al enviar la promoción: " + e.getMessage()
            ));
        }
    }
}
