package com.gpadilla.mycar.entity;

import com.gpadilla.mycar.enums.TipoDePago;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FormaDePago extends BaseEntity<Long> {

    private String observacion;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoDePago tipoDePago;
}
