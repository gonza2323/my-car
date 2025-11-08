package com.gpadilla.mycar.entity.geo;

import com.gpadilla.mycar.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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
public class Departamento extends BaseEntity<Long> {

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(optional = false)
    private Provincia provincia;
}
