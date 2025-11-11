package com.gpadilla.mycar.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleFactura extends BaseEntity<Long> {

    @Column(nullable = false)
    private Double subtotal;

    @ManyToOne(optional = false)
    private Factura factura;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Alquiler alquiler;

    @ManyToOne
    private Promocion promocion;
}
