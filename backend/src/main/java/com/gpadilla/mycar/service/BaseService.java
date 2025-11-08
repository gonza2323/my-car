package com.gpadilla.mycar.service;

import com.gpadilla.mycar.entity.BaseEntity;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.mapper.BaseMapper;
import com.gpadilla.mycar.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

public abstract class BaseService<
        E extends BaseEntity<ID>,
        ID extends Serializable,
        R extends BaseRepository<E, ID>,
        DetailDto,
        SummaryDto,
        CreateDto,
        UpdateDto,
        M extends BaseMapper<E, DetailDto, SummaryDto, CreateDto, UpdateDto>> {

    private final String entityName;
    protected final R repository;
    protected final M mapper;

    protected BaseService(String entityName, R repository, M mapper) {
        this.entityName = entityName;
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public E find(ID id) {
        return repository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new BusinessException( "La " + entityName + " solicitada no existe o fue eliminada."));
    }

    @Transactional(readOnly = true)
    public DetailDto findDto(ID id) {
        E entity = find(id);
        return toDetailDto(entity);
    }

    @Transactional(readOnly = true)
    public Page<SummaryDto> findDtos(Pageable pageable) {
        Page<E> entities = repository.findByEliminadoFalse(pageable);
        return entities.map(this::toSummaryDto);
    }

    @Transactional
    public E create(CreateDto dto) {
        validateCreate(dto);
        E entity = toEntity(dto);
        preCreate(dto, entity);
        entity.setId(null);
        entity.setEliminado(false);
        repository.save(entity);
        postCreate(dto, entity);
        return entity;
    }

    @Transactional
    public E update(ID id, UpdateDto dto) {
        validateUpdate(id, dto);
        E entity = find(id);
        preUpdate(dto, entity);
        updateEntity(dto, entity);
        repository.save(entity);
        postUpdate(dto, entity);
        return entity;
    }

    @Transactional
    public void delete(ID id) {
        E entity = find(id);
        preDelete(entity);
        entity.setEliminado(true);
        repository.save(entity);
        postDelete(entity);
    }

    @Transactional
    public DetailDto updateAndReturnDto(ID id, UpdateDto dto) {
        E entity = update(id, dto);
        return toDetailDto(entity);
    }

    @Transactional
    public DetailDto createAndReturnDto(CreateDto dto) {
        E entity = create(dto);
        return toDetailDto(entity);
    }


    // sobreescribir si los mapeos son más complejos

    protected E toEntity(CreateDto dto) {
        return mapper.toEntity(dto);
    }

    protected void updateEntity(UpdateDto dto, E entity) {
        mapper.updateEntity(dto, entity);
    }

    protected DetailDto toDetailDto(E entity) {
        return mapper.toDto(entity);
    }

    protected SummaryDto toSummaryDto(E entity) {
        return mapper.toSummaryDto(entity);
    }


    // sobreescribir para agregar funcionalidad
    // ej. creación de entidades hijas
    // las funciones pre_() ocurren antes de persistir la entidad
    // las funciones post_() ocurren después de persistir la entidad

    protected void validateCreate(CreateDto dto) {}
    protected void validateUpdate(ID id, UpdateDto updateDto) {}

    protected void preCreate(CreateDto dto, E entity) {}
    protected void postCreate(CreateDto dto, E entity) {}

    protected void preUpdate(UpdateDto dto, E entity) {}
    protected void postUpdate(UpdateDto dto, E entity) {}

    protected void preDelete(E entity) {}
    protected void postDelete(E entity) {}
}
