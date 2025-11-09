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

    @ManyToOne(optional = false)
    private Factura factura;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Alquiler alquiler;
}
