package com.gpadilla.mycar.entity;

import com.gpadilla.mycar.enums.EstadoPagoAlquiler;
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
public class Alquiler extends BaseEntity<Long> {

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoPagoAlquiler estado;

    @ManyToOne(optional = false)
    private Cliente cliente;
}
