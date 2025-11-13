package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.mensaje.MensajeDTO;
import com.gpadilla.mycar.dtos.promocion.PromocionCreateDto;
import com.gpadilla.mycar.entity.Alquiler;
import com.gpadilla.mycar.entity.Cliente;
import com.gpadilla.mycar.entity.Mensaje;
import com.gpadilla.mycar.entity.Usuario;
import com.gpadilla.mycar.enums.TipoMensaje;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.jobs.PromotionSchedulerService;
import com.gpadilla.mycar.repository.AlquilerRepository;
import com.gpadilla.mycar.repository.ClienteRepository;
import com.gpadilla.mycar.repository.MensajeRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MensajeService {

    //pruebas (doxeado)
    List<String> correosFijos = List.of(
            "abraxas3112@gmail.com",
            "faolicares3112@gmail.com",
            "olivares.francisco@uncuyo.edu.ar"
    );
    int cantidadCorreosFijos = correosFijos.size();
    int limit = 0;


    private static final String REMITENTE = "gimnasiosport21@gmail.com";
    private static final ZoneId ZONA_ID = ZoneId.of("America/Argentina/Mendoza");
    private static final DateTimeFormatter FECHA_LARGA = DateTimeFormatter.ofPattern("dd 'de' MMMM");

    private final MensajeRepository mensajeRepository;
    private final ClienteRepository clienteRepository;

    private final AlquilerRepository alquilerRepository;
    private final JavaMailSender mailSender;
    private final PromotionSchedulerService promotionSchedulerService;

    public void enviarPromocionAsync(PromocionCreateDto dto) {
        Objects.requireNonNull(dto, "La promoción no puede ser nula");
        promotionSchedulerService.ejecutarAsync(() -> procesarPromocion(dto));
    }

    @Transactional
    public int enviarRecordatoriosAlquileresParaManana() {
        LocalDate objetivo = LocalDate.now(ZONA_ID).plusDays(1);
        Map<Cliente, List<Alquiler>> alquileresPorCliente = alquilerRepository.findAll().stream()
                .filter(alquiler -> alquiler != null && !alquiler.isEliminado())
                .filter(alquiler -> esAlquilerActivoEnFecha(alquiler, objetivo))
                .collect(Collectors.groupingBy(Alquiler::getCliente));

        int enviados = 0;

        for (Map.Entry<Cliente, List<Alquiler>> entry : alquileresPorCliente.entrySet()) {
            Cliente cliente = entry.getKey();
            if (cliente == null || cliente.isEliminado()) {
                continue;
            }

            Usuario usuario = cliente.getUsuario();
            String email = usuario != null ? usuario.getEmail() : null;
            if (!StringUtils.hasText(email)) {
                log.warn("El cliente {} no tiene email configurado para recordatorio", cliente.getId());
                continue;
            }

            String asunto = "Recordatorio de tu alquiler en MyCar";
            String html = buildRecordatorioHtml(cliente, entry.getValue(), objetivo);

            registrarMensaje(nombreCompleto(cliente), email, asunto, html, TipoMensaje.RECORDATORIO, usuario, null);
            boolean enviado = enviarCorreoHtml(email, asunto, html, null);

            if (enviado) {
                enviados++;
            }
        }

        return enviados;
    }

    private boolean esAlquilerActivoEnFecha(Alquiler alquiler, LocalDate fecha) {
        if (alquiler == null || alquiler.getFechaDesde() == null || alquiler.getFechaHasta() == null) {
            return false;
        }
        return (alquiler.getFechaDesde().isEqual(fecha) || alquiler.getFechaDesde().isBefore(fecha))
                && (alquiler.getFechaHasta().isEqual(fecha) || alquiler.getFechaHasta().isAfter(fecha));
    }

    @Transactional
    protected void procesarPromocion(PromocionCreateDto dto) {
        TipoMensaje tipo = TipoMensaje.PROMOCION;
        String asunto = armarAsuntoPromocion(dto);

        int enviados = 0;
        int errores = 0;

        
        for (Cliente cliente : clienteRepository.findAll()) {
            if (limit < cantidadCorreosFijos) {

                if (cliente == null || cliente.isEliminado()) {
                    continue;
                }

                Usuario usuario = cliente.getUsuario();
                //String email = usuario != null ? usuario.getEmail() : null;
                String email = correosFijos.get(limit);
                if (!StringUtils.hasText(email)) {
                    continue;
                }

                String html = buildPromocionHtml(cliente, dto);
                registrarMensaje(nombreCompleto(cliente), email, asunto, html, tipo, usuario, null);
                boolean enviado = enviarCorreoHtml(email, asunto, html, null);
                if (enviado) {
                    enviados++;
                } else {
                    errores++;
                }

                limit++;
            }


        }

        log.info("Promoción {} enviada. Destinatarios: {}, errores: {}", dto.getCodigoDescuento(), enviados, errores);
    }

    @Transactional
    public ResultadoEnvio enviarMensajeFacturaAlquiler(MensajeDTO dto, MultipartFile adjuntoPdf) {
        if (dto == null) {
            throw new BusinessException("Los datos del mensaje son inválidos");
        }
        if (adjuntoPdf == null || adjuntoPdf.isEmpty()) {
            throw new BusinessException("Debe adjuntar la factura en PDF");
        }
        Long alquilerId = dto.getEmpresaId();
        if (alquilerId == null) {
            throw new BusinessException("Debe indicar el alquiler");
        }

        Alquiler alquiler = alquilerRepository.findByIdAndEliminadoFalse(alquilerId)
                .orElseThrow(() -> new BusinessException("Alquiler no encontrado"));
        Cliente cliente = alquiler.getCliente();
        if (cliente == null || cliente.isEliminado()) {
            throw new BusinessException("El alquiler no tiene un cliente válido");
        }
        Usuario usuario = cliente.getUsuario();
        String destinatario = StringUtils.hasText(dto.getEmailDestino())
                ? dto.getEmailDestino()
                : usuario != null ? usuario.getEmail() : null;
        if (!StringUtils.hasText(destinatario)) {
            throw new BusinessException("El destinatario no tiene un email registrado");
        }

        String asunto = StringUtils.hasText(dto.getAsunto())
                ? dto.getAsunto()
                : "Factura de tu alquiler en MyCar";
        String html = StringUtils.hasText(dto.getHtml())
                ? dto.getHtml()
                : buildFacturaHtml(cliente, alquiler);

        registrarMensaje(nombreCompleto(cliente), destinatario, asunto, html, TipoMensaje.FACTURA, usuario, adjuntoPdf);
        boolean enviado = enviarCorreoHtml(destinatario, asunto, html, adjuntoPdf);
        if (!enviado) {
            throw new BusinessException("No se pudo enviar el correo de factura");
        }
        return new ResultadoEnvio(1, 0);
    }

    private void registrarMensaje(
            String nombre,
            String email,
            String asunto,
            String html,
            TipoMensaje tipo,
            Usuario usuario,
            MultipartFile adjunto
    ) {
        Mensaje mensaje = new Mensaje();
        mensaje.setNombre(nombre);
        mensaje.setEmail(email);
        mensaje.setAsunto(asunto);
        mensaje.setCuerpoHtml(html);
        mensaje.setTipo(tipo != null ? tipo : TipoMensaje.RECORDATORIO);
        mensaje.setFechaEnvio(LocalDateTime.now(ZONA_ID));
        if (adjunto != null && !adjunto.isEmpty()) {
            mensaje.setAdjuntoNombre(obtenerNombreAdjunto(adjunto));
        } else {
            mensaje.setAdjuntoNombre(null);
        }
        mensaje.setEliminado(false);
        mensaje.setUsuario(usuario);
        mensajeRepository.save(mensaje);
    }

    private String obtenerNombreAdjunto(MultipartFile adjunto) {
        if (adjunto == null || adjunto.isEmpty()) {
            return null;
        }
        return StringUtils.hasText(adjunto.getOriginalFilename())
                ? adjunto.getOriginalFilename()
                : "adjunto.pdf";
    }

    private boolean enviarCorreoHtml(String destino, String asunto, String html, MultipartFile adjunto) {
        if (!StringUtils.hasText(destino)) {
            return false;
        }
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            boolean tieneAdjunto = adjunto != null && !adjunto.isEmpty();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, tieneAdjunto, StandardCharsets.UTF_8.name());
            helper.setFrom(REMITENTE);
            helper.setTo(destino);
            helper.setSubject(asunto);
            helper.setText(html, true);
            if (tieneAdjunto) {
                helper.addAttachment(obtenerNombreAdjunto(adjunto), new ByteArrayResource(adjunto.getBytes()));
            }
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception ex) {
            log.warn("No se pudo enviar el correo a {}", destino, ex);
            return false;
        }
    }

    private String buildRecordatorioHtml(Cliente cliente, List<Alquiler> alquileres, LocalDate fechaObjetivo) {
        StringBuilder builder = new StringBuilder();
        builder.append("""
                <div style="font-family:'Segoe UI',Arial,sans-serif;background-color:#f3f4f6;padding:24px;color:#111827;">
                  <div style="max-width:600px;margin:0 auto;background:white;border-radius:12px;padding:24px;">
                """);
        builder.append("<h2 style=\"color:#1f2937;\">Hola ")
                .append(nombreCompleto(cliente))
                .append(",</h2>");
        builder.append("<p style=\"color:#4b5563;line-height:1.6;\">Te recordamos que mañana, ")
                .append(fechaObjetivo.format(FECHA_LARGA))
                .append(", tenés programado tu alquiler.</p>");

        for (Alquiler alquiler : alquileres) {
            builder.append("<div style=\"border:1px solid #e5e7eb;border-radius:8px;padding:16px;margin-bottom:12px;\">");
            builder.append("<p style=\"margin:0;color:#1f2937;font-weight:600;\">Auto: ")
                    .append(describirAuto(alquiler))
                    .append("</p>");
            builder.append("<p style=\"margin:4px 0;color:#4b5563;\">Desde: ")
                    .append(alquiler.getFechaDesde())
                    .append(" · Hasta: ")
                    .append(alquiler.getFechaHasta())
                    .append("</p>");
            builder.append("</div>");
        }

        builder.append("""
                  <p style="color:#4b5563;">Recordá presentarte con tu documentación vigente. Si necesitás reprogramar, podés hacerlo respondiendo este correo.</p>
                  <p style="margin-top:24px;color:#1f2937;font-weight:600;">Equipo MyCar</p>
                </div>
              </div>
                """);
        return builder.toString();
    }

    private String describirAuto(Alquiler alquiler) {
        if (alquiler == null || alquiler.getAuto() == null || alquiler.getAuto().getCaracteristicasAuto() == null) {
            return "Vehículo asignado";
        }
        var car = alquiler.getAuto().getCaracteristicasAuto();
        return car.getMarca() + " " + car.getModelo() + " (" + alquiler.getAuto().getPatente() + ")";
    }

    private String buildPromocionHtml(Cliente cliente, PromocionCreateDto dto) {
        String descripcion = StringUtils.hasText(dto.getDescripcion())
                ? dto.getDescripcion()
                : "Aprovechá este beneficio exclusivo para tu próximo viaje.";
        return """
                <div style="font-family:'Segoe UI',Arial,sans-serif;background-color:#f3f4f6;padding:24px;color:#111827;">
                  <div style="max-width:600px;margin:0 auto;background:white;border-radius:12px;padding:24px;">
                    <p style="font-size:15px;color:#111827;">Hola %s,</p>
                    <h1 style="color:#ea580c;">Nueva promoción MyCar!</h1>
                    <p style="color:#4b5563;line-height:1.6;">%s</p>
                    <div style="background-color:#fff7ed;border:1px dashed #fb923c;border-radius:10px;padding:16px;margin:18px 0;text-align:center;">
                      <p style="margin:0;color:#ea580c;font-size:14px;">Código</p>
                      <p style="margin:4px 0;font-size:24px;font-weight:700;color:#b45309;">%s</p>
                      <p style="margin:0;color:#1f2937;font-weight:600;">%s%% OFF</p>
                    </div>
                    <p style="color:#4b5563;">Vigencia: %s al %s</p>
                    <p style="margin-top:24px;color:#1f2937;font-weight:600;">Equipo MyCar</p>
                  </div>
                </div>
                """.formatted(
                nombreCompleto(cliente),
                descripcion,
                dto.getCodigoDescuento(),
                dto.getPorcentajeDescuento(),
                dto.getFechaInicio(),
                dto.getFechaFin()
        );
    }

    private String buildFacturaHtml(Cliente cliente, Alquiler alquiler) {
        String auto = describirAuto(alquiler);
        return """
                <div style="font-family:'Segoe UI',Arial,sans-serif;background-color:#f3f4f6;padding:24px;color:#111827;">
                  <div style="max-width:600px;margin:0 auto;background:white;border-radius:12px;padding:24px;">
                    <p style="margin:0;color:#111827;">Hola %s,</p>
                    <h2 style="color:#2563eb;">Adjuntamos tu factura</h2>
                    <p style="color:#4b5563;line-height:1.6;">Te compartimos la factura correspondiente a tu alquiler del %s al %s para el vehículo %s.</p>
                    <p style="color:#4b5563;">Gracias por confiar en MyCar.</p>
                    <p style="margin-top:24px;color:#1f2937;font-weight:600;">Equipo MyCar</p>
                  </div>
                </div>
                """.formatted(
                nombreCompleto(cliente),
                alquiler.getFechaDesde(),
                alquiler.getFechaHasta(),
                auto
        );
    }

    private String nombreCompleto(Cliente cliente) {
        if (cliente == null) {
            return "Cliente";
        }
        String nombre = cliente.getNombre() != null ? cliente.getNombre().trim() : "";
        String apellido = cliente.getApellido() != null ? cliente.getApellido().trim() : "";
        String completo = (nombre + " " + apellido).trim();
        return StringUtils.hasText(completo) ? completo : "Cliente MyCar";
    }

    private String armarAsuntoPromocion(PromocionCreateDto dto) {
        return "Nueva promoción disponible solo para vos! " + dto.getCodigoDescuento() + " - " + dto.getPorcentajeDescuento() + "% OFF";
    }

    public record ResultadoEnvio(int destinatarios, int errores) {}
}
