package com.gpadilla.mycar.dtos.reportes;

import java.io.Serializable;

public record RecaudacionCerradosRow(
        String modelo,
        Long cantidad,
        Double total
) implements Serializable {}