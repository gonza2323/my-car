package com.gpadilla.mycar.entity;

import com.gpadilla.mycar.enums.EstadoFactura;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Factura extends BaseEntity<Long> {

    @Column(nullable = false, unique = true)
    private Long numeroFactura;

    @Column(nullable = false)
    private LocalDate fechaFactura;

    @Column(nullable = false)
    private Double totalPagado;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoFactura estado;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleFactura> detalles = new ArrayList<>();


    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    private FormaDePago formaDePago;
}
