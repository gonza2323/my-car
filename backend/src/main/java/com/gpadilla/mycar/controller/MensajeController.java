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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/mensajes")
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class MensajeController {

    private final MensajeService mensajeService;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATIVO', 'JEFE')")
    public ResponseEntity<String> list() {

        return ResponseEntity.ok("Listado de mensajes");
    }

    //SE MANEJA EN EL CONTROLADOR ESPECIFICO DE PROMOCIONES
//    @PostMapping(value = "/promociones", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasAnyRole('JEFE','ADMINISTRATIVO')")
//    public ResponseEntity<Map<String, String>> enviarPromocion(@Valid @RequestBody PromocionDTO dto) {
//        mensajeService.enviarPromocionAsync(dto);
//        return ResponseEntity.accepted().body(Map.of("status", "EN_PROCESO"));
//    }


    //SE MANEJA EN EL CONTROLADOR ESPECIFICO DE ALQUILER
//    @PostMapping(value = "/alquiler/{alquilerId}/factura", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PreAuthorize("hasAnyRole('JEFE','ADMINISTRATIVO')")
//    public ResponseEntity<MensajeService.ResultadoEnvio> enviarFactura(
//            @PathVariable Long alquilerId,
//            @RequestPart("dto") @Valid MensajeDTO dto,
//            @RequestPart("adjuntoPdf") MultipartFile adjuntoPdf
//    ) {
//        dto.setEmpresaId(alquilerId);
//        MensajeService.ResultadoEnvio resultado = mensajeService.enviarMensajeFacturaAlquiler(dto, adjuntoPdf);
//        return ResponseEntity.ok(resultado);
//    }


    // --- Envío de promoción a un cliente específico ---
//    @PostMapping("/promociones/{clienteId}")
//    public ResponseEntity<Void> enviarPromocionACliente(
//            @PathVariable Long clienteId,
//            @RequestBody PromocionDTO dto
//    ) {
//        return mensajeService.procesarPromocionParaCliente(dto, clienteId)
//                ? ResponseEntity.ok().build()
//                : ResponseEntity.badRequest().build();
//    }

}
