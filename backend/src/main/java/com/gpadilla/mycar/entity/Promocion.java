package com.gpadilla.mycar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Promocion extends BaseEntity<Long> {

    @Column(nullable = false)
    private Double porcentajeDescuento;

    @Column(nullable = false)
    private String codigoDescuento;

    @Column
    private String descripcion;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;
}
