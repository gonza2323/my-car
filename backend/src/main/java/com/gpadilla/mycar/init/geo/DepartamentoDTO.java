package com.gpadilla.mycar.init.geo;

import lombok.Data;

@Data
public class DepartamentoDTO {
    private String id;
    private String nombre;
    private ProvinciaDTO provincia;

    public Long getIdAsLong() {
        return Long.parseLong(id);
    }
}