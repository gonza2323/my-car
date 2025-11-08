package com.gpadilla.mycar.init.geo;

import lombok.Data;

@Data
public class ProvinciaDTO {
    private String id;
    private String nombre;

    public Long getIdAsLong() {
        return Long.parseLong(id);
    }
}
