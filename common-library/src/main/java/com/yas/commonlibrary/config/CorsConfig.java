package com.yas.commonlibrary.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ConfigurationProperties
public class CorsConfig {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public WebMvcConfigurer corsConfigure() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(allowedOrigins)
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("Content-Type", "Authorization");

                // String[] origins = allowedOrigins == null ? new String[0] :
                // allowedOrigins.split("\\s*,\\s*");
                // registry.addMapping("/**")
                // .allowedOrigins(origins) // or allowedOriginPatterns(...) if you want
                // patterns
                // .allowedMethods("*")
                // .allowedHeaders("*");
            }
        };
    }
}
