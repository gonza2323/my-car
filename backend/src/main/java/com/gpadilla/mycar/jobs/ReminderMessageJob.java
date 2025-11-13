package com.gpadilla.mycar.jobs;

import com.gpadilla.mycar.service.MensajeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReminderMessageJob {

    // segundos minutos horas dia-mes mes dia-semana
    private static final String RECORDATORIO_CRON = "0 6 22 * * *";
    private static final String RECORDATORIO_ZONE = "America/Argentina/Mendoza";

    @Autowired
    private MensajeService mensajeService;

    @Scheduled(cron = RECORDATORIO_CRON, zone = RECORDATORIO_ZONE)
    public void ejecutarRecordatoriosDiarios() {
        int enviados = mensajeService.enviarRecordatoriosAlquileresParaManana();
        log.info("Recordatorios diarios enviados: {}", enviados);
    }
}
