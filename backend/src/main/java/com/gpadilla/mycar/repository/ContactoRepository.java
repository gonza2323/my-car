package com.gpadilla.mycar.repository;

import com.gpadilla.mycar.entity.Contacto;
import com.gpadilla.mycar.entity.ContactoCorreoElectronico;
import com.gpadilla.mycar.entity.ContactoTelefonico;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactoRepository extends BaseRepository<Contacto, Long> {

    @Query("select t from ContactoTelefonico t where t.eliminado=false and t.telefono=:telefono")
    Optional<ContactoTelefonico> findTelefono(String telefono);

    @Query("select c from ContactoCorreoElectronico c where c.eliminado=false and lower(c.email)=lower(:email)")
    Optional<ContactoCorreoElectronico> findCorreo(String email);

    List<Contacto> findAllByEmpresaIdAndEliminadoFalse(Long empresaId);
    List<Contacto> findAllByPersonaIdAndEliminadoFalse(Long personaId);
}
