package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.factura.FacturaDto;
import com.gpadilla.mycar.entity.Factura;
import com.gpadilla.mycar.mapper.FacturaMapper;
import com.gpadilla.mycar.repository.FacturaRepository;
import org.springframework.stereotype.Service;

@Service
public class FacturaService extends BaseService<
        Factura,                  // Entidad
        Long,                     // ID
        FacturaRepository,        // Repositorio
        FacturaDto,               // DetailDto
        FacturaDto,               // SummaryDto
        FacturaDto,               // CreateDto
        FacturaDto,               // UpdateDto
        FacturaMapper             // Mapper
        > {

    public FacturaService(FacturaRepository repository, FacturaMapper mapper) {
        super("Factura", repository, mapper);
    }


    public FacturaDto buscarFacturaDto(Long id) {
        // Usa findDto() del BaseService, que ya aplica eliminado = false
        return findDto(id);
    }

    // 🧩 Ejemplo de hook opcional: validaciones personalizadas
    @Override
    protected void validateCreate(FacturaDto dto) {
        if (dto.getTotalPagado() == null || dto.getTotalPagado() <= 0) {
            throw new IllegalArgumentException("El total pagado debe ser mayor a cero.");
        }
    }
}
