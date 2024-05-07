package com.backend.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableMongoRepositories
public class ApiApplication {

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
                registry.addMapping("/users")
                        .allowedOrigins(env.getProperty("cors.origins"));
                registry.addMapping("/users/name/{name}")
                        .allowedOrigins(env.getProperty("cors.origins"));
                registry.addMapping("/languages")
                        .allowedOrigins(env.getProperty("cors.origins"));
            }
        };
    }

}
