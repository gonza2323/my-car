package com.gpadilla.mycar.service;

import com.gpadilla.mycar.dtos.alquiler.AlquilerDetalleDto;
import com.gpadilla.mycar.dtos.alquiler.AlquilerSummaryDto;
import com.gpadilla.mycar.entity.Alquiler;
import com.gpadilla.mycar.entity.Auto;
import com.gpadilla.mycar.entity.Cliente;
import com.gpadilla.mycar.enums.EstadoPagoAlquiler;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.repository.AlquilerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AlquilerService {
    private final AlquilerRepository alquilerRepository;

    @Transactional(readOnly = true)
    public Alquiler find(Long id) {
        return alquilerRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new BusinessException("Alquiler no encontrado"));
    }

    @Transactional
    public Alquiler registrarAlquiler(
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            Double costoPorDia,
            Double monto,
            EstadoPagoAlquiler estado,
            Auto auto,
            Cliente cliente) {

        validarFechas(fechaDesde, fechaHasta);

        Alquiler alquiler = Alquiler.builder()
                .fechaDesde(fechaDesde)
                .fechaHasta(fechaHasta)
                .costoPorDia(costoPorDia)
                .monto(monto)
                .estado(estado)
                .auto(auto)
                .cliente(cliente).build();

        return alquilerRepository.save(alquiler);
    }

    private void validarFechas(LocalDate fechaDesde, LocalDate fechaHasta) {
        if (fechaHasta.isBefore(fechaDesde))
            throw new BusinessException("La fecha de finalización debe ser igual o posterior a la de inicio");
    }

    public Page<AlquilerSummaryDto> findDtos(Pageable pageable) {
        return alquilerRepository.findAllByEliminadoFalse(pageable);
    }

    public Page<AlquilerDetalleDto> listarAlquileresDeUsuario(Pageable pageable, Long userId) {
        return alquilerRepository.listarAlquileresDeUsuario(pageable, userId);
    }
}
