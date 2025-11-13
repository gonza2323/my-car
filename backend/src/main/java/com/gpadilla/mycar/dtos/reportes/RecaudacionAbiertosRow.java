package com.gpadilla.mycar.dtos.reportes;

import java.io.Serializable;
import java.time.LocalDate;

public record RecaudacionAbiertosRow(
        String modelo,
        LocalDate desde,
        LocalDate hasta,
        Double costoPorDia
) implements Serializable {}