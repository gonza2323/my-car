package com.gpadilla.mycar.entity;

import com.gpadilla.mycar.enums.TipoEmpleado;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Empleado extends Persona {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoEmpleado tipoEmpleado;
}
