package com.gpadilla.mycar.init;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class FakerConfig {

    @Bean
    public Faker faker() {
        long seed = 42L;
        return new Faker(new Random(seed));
    }
}
