package com.gpadilla.mycar.entity;

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
public class CostoAuto extends BaseEntity<Long>{
    @Column(nullable = false)
    private LocalDate fechaDesde;

    @Column(nullable = false)
    private LocalDate fechaHasta;

    @Column(nullable = false)
    private Double costoTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caracteristicas_auto_id", nullable = false)
    private CaracteristicasAuto caracteristicasAuto;
}
