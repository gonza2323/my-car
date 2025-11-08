package com.gpadilla.mycar.entity;

import com.gpadilla.mycar.entity.geo.Direccion;
import com.gpadilla.mycar.enums.TipoDocumento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona extends BaseEntity<Long> {

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    @Column(nullable = false)
    private String numeroDocumento;

    @ManyToOne(optional = false)
    private Direccion direccion;

    @ManyToOne(optional = false)
    private Usuario usuario;
}
