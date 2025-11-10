package com.gpadilla.mycar.dtos.auto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoCreateDto {
    private String patente;
    private Long caracteristicasAutoId;
}

