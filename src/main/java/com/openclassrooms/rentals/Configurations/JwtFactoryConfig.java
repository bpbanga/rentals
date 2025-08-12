package com.openclassrooms.rentals.Configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.openclassrooms.rentals.security.HmacJwtFactory;

@Configuration
public class JwtFactoryConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public HmacJwtFactory jwtFactory() {
        return new HmacJwtFactory(secret);
    }
}