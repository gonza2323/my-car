package com.gpadilla.mycar.entity.geo;

import com.gpadilla.mycar.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Localidad extends BaseEntity<Long> {

    @Column(nullable = false)
    private String nombre;

    private String codigoPostal;

    @ManyToOne(optional = false)
    private Departamento departamento;
}
