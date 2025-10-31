package com.gpadilla.mycar.init;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitialization implements CommandLineRunner {

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        crearDatosIniciales();
    }

    @Transactional
    protected void crearDatosIniciales() throws Exception {
//        if (personaRepository.existsByNombreAndEliminadoFalse("NOMBRE 01")) {
//            System.out.println("Datos iniciales ya creados. Salteando creación de datos iniciales. Para forzar su creación, borrar la base de datos");
//            return;
//        }

        System.out.println("Creando datos iniciales...");

        // Creación de datos iniciales
//        crearLocalidades();
//        crearAutores();
//        crearPersonas();
//        crearLibros();
//        crearAdmin();

        // Resetear los permisos
//        SecurityContextHolder.clearContext();

        System.out.println("Datos iniciales creados.");
    }
}
