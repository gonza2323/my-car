package com.gpadilla.mycar.entity;

import com.gpadilla.mycar.enums.TipoMensaje;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_destinatario", length = 200)
    private String nombre;

    @Column(name = "email_destinatario", length = 320)
    private String email;

    @Column(name = "titulo", length = 200)
    private String asunto;

    @Column(name = "cuerpo_html", length = 8000)
    private String cuerpoHtml;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_mensaje", nullable = false, length = 20)
    private TipoMensaje tipo;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Column(name = "adjunto_nombre", length = 255)
    private String adjuntoNombre;

    @Column(nullable = false)
    private boolean eliminado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
