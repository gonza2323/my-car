package com.gpadilla.mycar.service;

import com.gpadilla.mycar.entity.Alquiler;
import com.gpadilla.mycar.error.BusinessException;
import com.gpadilla.mycar.repository.AlquilerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlquilerService {
    private final AlquilerRepository alquilerRepository;

    @Transactional(readOnly = true)
    public Alquiler find(Long id) {
        return alquilerRepository.findByIdAndEliminadoFalse(id)
                .orElseThrow(() -> new BusinessException("Alquiler no encontrado"));
    }
}
