package com.gpadilla.mycar.init;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.Random;

@Configuration
public class FakerConfig {

    @Bean
    public Faker faker() {
        long seed = 123L;
        return new Faker(Locale.of("es", "AR"), new Random(seed));
    }
}
