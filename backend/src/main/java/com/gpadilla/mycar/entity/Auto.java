package com.gpadilla.mycar.entity;

import com.gpadilla.mycar.enums.EstadoAuto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Auto extends BaseEntity<Long> {
    @Column(nullable = false, unique = true)
    private String patente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAuto estadoAuto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caracteristicas_auto_id", nullable = false)
    private CaracteristicasAuto caracteristicasAuto;

    @OneToMany(mappedBy = "auto", fetch = FetchType.LAZY)
    private List<Alquiler> alquileres = new ArrayList<>();
}
