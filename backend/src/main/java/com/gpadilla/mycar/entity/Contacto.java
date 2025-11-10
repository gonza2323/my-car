package com.gpadilla.mycar.entity;

import com.gpadilla.mycar.enums.TipoContacto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter @Setter
@Table(name = "contacto")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Contacto extends BaseEntity<Long> {

    @Enumerated(EnumType.STRING)
    private TipoContacto tipoContacto; // PERSONAL o LABORAL

    private String observacion;

    // RELACIONES:
    // Contacto puede pertenecer a una Empresa o a una Persona.
    // (en el diagrama parece que ambas usan Contacto)
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "persona_id")
    private Persona persona;
}
