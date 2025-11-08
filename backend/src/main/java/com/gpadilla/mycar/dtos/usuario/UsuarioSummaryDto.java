package com.gpadilla.mycar.dtos.usuario;

import com.gpadilla.mycar.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioSummaryDto {
    private Long id;
    private String email;
    private UserRole rol;
}
