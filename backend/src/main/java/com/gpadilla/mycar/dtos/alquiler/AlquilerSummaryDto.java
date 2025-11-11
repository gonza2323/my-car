package com.gpadilla.mycar.dtos.alquiler;

import java.time.LocalDate;

public interface AlquilerSummaryDto {
    Long getId();
    LocalDate getFechaDesde();
    LocalDate getFechaHasta();
    Double getMonto();
}
