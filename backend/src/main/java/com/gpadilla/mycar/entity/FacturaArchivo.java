package com.gpadilla.mycar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacturaArchivo extends BaseEntity<Long> {

    @Column(nullable = false)
    private String nombreArchivo;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGBLOB") // MySQL / Postgres compatible
    private byte[] contenidoPdf;

    @Column(nullable = false, length = 1024)
    private String rutaArchivo;

    @OneToOne(optional = false)
    private Factura factura;

    private boolean eliminado;
}
