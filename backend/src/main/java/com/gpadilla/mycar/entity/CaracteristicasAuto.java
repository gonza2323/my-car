package com.gpadilla.mycar.entity;

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
public class CaracteristicasAuto extends BaseEntity<Long> {

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private int cantidadPuertas;

    @Column(nullable = false)
    private int anio;

    @Column(nullable = false)
    private int cantidadAsientos;

    @Column(nullable = false)
    private int cantTotalVehiculos;

    @Column(nullable = false)
    private int cantidadAlquilados;

    // 🔹 Relación Uno a Muchos con Imagen
    @OneToMany(mappedBy = "caracteristicasAuto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Imagen> imagenes = new java.util.ArrayList<>();

    // 🔹 Relación 1:N con Vehiculo
    @OneToMany(mappedBy = "caracteristicasVehiculo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Auto> vehiculos = new ArrayList<>();

    // 🔹 Relación 1:N con CostoVehiculo
    @OneToMany(mappedBy = "caracteristicasVehiculo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CostoAuto> costos = new ArrayList<>();
}
