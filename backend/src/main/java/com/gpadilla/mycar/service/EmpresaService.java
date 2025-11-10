package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.empresa.EmpresaCreateOrUpdateDto;
import com.gpadilla.mycar.dtos.empresa.EmpresaDetailDto;
import com.gpadilla.mycar.entity.Empresa;
import com.gpadilla.mycar.entity.geo.Direccion;
import com.gpadilla.mycar.mapper.EmpresaMapper;
import com.gpadilla.mycar.repository.EmpresaRepository;
import com.gpadilla.mycar.repository.geo.DireccionRepository;
import org.springframework.data.domain.PageRequest;
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
        EmpresaDetailDto,            // Summary (reutilizado)
        EmpresaCreateOrUpdateDto,    // Create
        EmpresaCreateOrUpdateDto,    // Update
        EmpresaMapper> {

    private final DireccionRepository direccionRepository;

    public EmpresaService(
            EmpresaRepository repository,
            EmpresaMapper mapper,
            DireccionRepository direccionRepository
    ) {
        super("Empresa", repository, mapper);
        this.direccionRepository = direccionRepository;
    }

    /* ==================== Validaciones mínimas ==================== */

    @Override
    protected void validateCreate(EmpresaCreateOrUpdateDto dto) {
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new RuntimeException("El nombre de la empresa es obligatorio.");
        }
        if (dto.getDireccionId() == null) {
            throw new RuntimeException("La dirección es obligatoria.");
        }
    }

    @Override
    protected void validateUpdate(Long id, EmpresaCreateOrUpdateDto dto) {
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new RuntimeException("El nombre de la empresa es obligatorio.");
        }
        if (dto.getDireccionId() == null) {
            throw new RuntimeException("La dirección es obligatoria.");
        }
    }

    /* ==================== Hooks (resolver direccionId) ==================== */

    @Override
    protected void preCreate(EmpresaCreateOrUpdateDto dto, Empresa entity) {
        entity.setDireccion(resolveDireccion(dto.getDireccionId()));
    }

    @Override
    protected void preUpdate(EmpresaCreateOrUpdateDto dto, Empresa entity) {
        if (dto.getDireccionId() != null) {
            entity.setDireccion(resolveDireccion(dto.getDireccionId()));
        }
    }

    private Direccion resolveDireccion(Long id) {
        return direccionRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new RuntimeException("La dirección no existe o fue eliminada."));
    }

    /* ==================== Mapping a Detail ==================== */

    @Override
    protected EmpresaDetailDto toDetailDto(Empresa entity) {
        // Si luego querés enriquecer con teléfono/email principal, hacelo acá antes de retornar.
        return mapper.toDto(entity);
    }

    /* ==================== “Empresa única” – métodos públicos ==================== */

    /** Inicializa si no existe; si existe, devuelve la actual. Idempotente. */
    public EmpresaDetailDto createIfAbsentAndReturnDto(EmpresaCreateOrUpdateDto dto) {
        Optional<Empresa> existente = findSingletonEntity();
        if (existente.isPresent()) {
            return toDetailDto(existente.get());
        }
        Empresa creada = create(dto);                // usa BaseService.create(...)
        return toDetailDto(creada);
    }

    /** Devuelve el detalle de la única empresa. */
    public EmpresaDetailDto findSingletonDto() {
        Empresa e = findSingletonEntity()
                .orElseThrow(() -> new RuntimeException("La empresa aún no fue inicializada."));
        return toDetailDto(e);
    }

    /** Actualiza la única empresa (nombre/direccionId). */
    public EmpresaDetailDto updateSingletonAndReturnDto(EmpresaCreateOrUpdateDto dto) {
        Empresa e = findSingletonEntity()
                .orElseThrow(() -> new RuntimeException("La empresa aún no fue inicializada."));
        Empresa actualizada = update(e.getId(), dto); // usa BaseService.update(...)
        return toDetailDto(actualizada);
    }

    /* ==================== Utilidad interna ==================== */

    /** Obtiene la “única” empresa activa: el primer registro no eliminado. */
    private Optional<Empresa> findSingletonEntity() {
        return repository.findByEliminadoFalse(PageRequest.of(0, 1))
                .stream()
                .findFirst();
    }

    /* ==================== Opcional: sin eliminación en singleton ==================== */

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("No se permite eliminar la empresa en este modo singleton.");
    }
}
