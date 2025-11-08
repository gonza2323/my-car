package com.gpadilla.mycar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CostoAuto extends BaseEntity<Long>{
    @Temporal(TemporalType.DATE)
    private Date fechaDesde;

    @Temporal(TemporalType.DATE)
    private Date fechaHasta;

    @Column(nullable = false)
    private double costoTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caracteristicas_auto_id", nullable = false)
    private CaracteristicasAuto caracteristicasAuto;
}
