package com.gpadilla.mycar.jobs;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class PromotionSchedulerService {

    private final TaskScheduler taskScheduler;

    public void ejecutarAsync(Runnable tarea) {
        taskScheduler.schedule(tarea, Date.from(Instant.now()));
    }

    public void programar(ZonedDateTime fechaEjecucion, Runnable tarea) {
        taskScheduler.schedule(tarea, Date.from(fechaEjecucion.toInstant()));
    }
}
