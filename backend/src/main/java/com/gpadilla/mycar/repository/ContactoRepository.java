package com.gpadilla.mycar.repository;

import com.gpadilla.mycar.entity.Contacto;
import com.gpadilla.mycar.entity.ContactoCorreoElectronico;
import com.gpadilla.mycar.entity.ContactoTelefonico;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactoRepository extends BaseRepository<Contacto, Long> {

    @Query("select ct from ContactoTelefonico ct " +
            "where ct.usuario.id = :usuarioId and ct.eliminado = false " +
            "order by ct.id desc")
    Optional<ContactoTelefonico> findTelefonoVigenteByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("select ct from ContactoTelefonico ct " +
            "where ct.empresa.id = :empresaId and ct.eliminado = false " +
            "order by ct.id desc")
    Optional<ContactoTelefonico> findTelefonoVigenteByEmpresaId(@Param("empresaId") Long empresaId);

    @Query("select cc from ContactoCorreoElectronico cc " +
            "where cc.usuario.id = :usuarioId and cc.eliminado = false " +
            "order by cc.id desc")
    Optional<ContactoCorreoElectronico> findCorreoVigenteByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("select cc from ContactoCorreoElectronico cc " +
            "where cc.empresa.id = :empresaId and cc.eliminado = false " +
            "order by cc.id desc")
    Optional<ContactoCorreoElectronico> findCorreoVigenteByEmpresaId(@Param("empresaId") Long empresaId);
}
