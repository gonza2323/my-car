package com.gpadilla.mycar.entity;

import com.gpadilla.mycar.entity.geo.Direccion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Table(name = "empresas")
public class Empresa extends BaseEntity<Long> {

    @NotEmpty(message = "La razon social no puede estar vacío")
    @Size(max = 50, message = "La razon social no puede superar los 50 caracteres")
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_direccion", nullable = false, foreignKey = @ForeignKey(name = "fk_empresa_direccion"))
    private Direccion direccion;
}