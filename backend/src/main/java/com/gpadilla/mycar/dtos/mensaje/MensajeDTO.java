package com.gpadilla.mycar.dtos.mensaje;

import com.gpadilla.mycar.enums.TipoMensaje;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensajeDTO {

    private Long id;

    @Size(max = 200)
    private String asunto;

    @Size(max = 8000)
    private String html;

    private TipoMensaje tipo = TipoMensaje.RECORDATORIO;

    private Long usuarioId;

    private Long empresaId;

    @Email
    @Size(max = 320)
    private String emailDestino;

    @Size(max = 200)
    private String nombreDestino;
}
