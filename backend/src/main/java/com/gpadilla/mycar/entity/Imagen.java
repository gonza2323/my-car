package com.gpadilla.mycar.entity;

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
public class Imagen extends BaseEntity<Long>{
    @Column(nullable = false)
    private String nombre;
    @Lob //ver si guardar la foto o la url / direccion para mas velocidad
    @Column(nullable = false)
    private byte[] contenido;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoImagen tipoImagen;

    // 🔹 Relación Muchos a Uno con CaracteristicasAuto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caracteristicas_auto_id")
    private CaracteristicasAuto caracteristicasAuto;

}
