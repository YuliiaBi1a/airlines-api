package com.yuliia.airlines_api.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {

    private static final Dotenv dotenv = Dotenv.load();

    @Bean
    public String jwtSecret() {
        return dotenv.get("JWT_SECRET");
    }
}