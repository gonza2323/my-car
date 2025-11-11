package com.gpadilla.mycar.entity;

import com.gpadilla.mycar.enums.TipoContacto;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "contacto")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Contacto extends BaseEntity<Long> {

    @Enumerated(EnumType.STRING)
    private TipoContacto tipoContacto; // PERSONAL o LABORAL

    private String observacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public boolean perteneceAUsuario(Long usuarioId) {
        return usuario != null && usuarioId != null && usuarioId.equals(usuario.getId());
    }

    public boolean perteneceAEmpresa(Long empresaId) {
        return empresa != null && empresaId != null && empresaId.equals(empresa.getId());
    }

    public boolean esDeEmpresa() {
        return empresa != null;
    }

    public boolean esDeUsuario() {
        return usuario != null;
    }
}
