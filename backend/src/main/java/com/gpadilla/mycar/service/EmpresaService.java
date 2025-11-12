package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.empresa.EmpresaCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.empresa.EmpresaDetailDto;
import com.gpadilla.mycar.entity.Empresa;
import com.gpadilla.mycar.mapper.EmpresaMapper;
import com.gpadilla.mycar.repository.EmpresaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class EmpresaService extends BaseService<
        Empresa,
        Long,
        EmpresaRepository,
        EmpresaDetailDto,            // Detail
        EmpresaDetailDto,
        EmpresaCreateOrUpdateDto,    // Create
        EmpresaCreateOrUpdateDto,    // Update
        EmpresaMapper> {

    public EmpresaService(
            EmpresaRepository repository,
            EmpresaMapper mapper
    ) {
        super("Empresa", repository, mapper);
    }

    @Override
    protected void validateUpdate(Long id, EmpresaCreateOrUpdateDto dto) {
        boolean telefonoVacio = dto.getTelefonoPrincipal() == null || dto.getTelefonoPrincipal().isBlank();
        boolean emailVacio = dto.getEmailPrincipal() == null || dto.getEmailPrincipal().isBlank();
        if (telefonoVacio && emailVacio) {
            throw new RuntimeException("Debe indicar al menos un dato de contacto para actualizar.");
        }
    }

    @Override
    protected EmpresaDetailDto toDetailDto(Empresa entity) {
        return mapper.toDto(entity);
    }


    public EmpresaDetailDto findSingletonDto() {
        return findSingletonEntity()
                .map(this::toDetailDto)
                .orElseThrow(() -> new RuntimeException("La empresa aún no fue inicializada."));
    }

    /** Actualiza únicamente los datos de contacto principales. */
    public EmpresaDetailDto updateContacto(EmpresaCreateOrUpdateDto dto) {
        Empresa e = findSingletonEntity()
                .orElseThrow(() -> new RuntimeException("La empresa aún no fue inicializada."));

        EmpresaCreateOrUpdateDto cambios = EmpresaCreateOrUpdateDto.builder()
                .nombre(e.getNombre())
                .direccionId(e.getDireccion() != null ? e.getDireccion().getId() : null) // preservar
                .telefonoPrincipal(dto.getTelefonoPrincipal() != null
                        ? dto.getTelefonoPrincipal()
                        : e.getTelefonoPrincipal().getTelefono())
                .emailPrincipal(dto.getEmailPrincipal() != null
                        ? dto.getEmailPrincipal()
                        : e.getEmailPrincipal().getEmail())
                .build();

        Empresa actualizada = update(e.getId(), cambios); // usa BaseService.update(...)

        return toDetailDto(actualizada);
    }

    /* ==================== Utilidad interna ==================== */

    /** Obtiene la “única” empresa activa: el primer registro no eliminado. */
    private Optional<Empresa> findSingletonEntity() {
        return repository.findFirstByEliminadoFalseOrderByIdAsc();
    }
}
