package com.yas.commonlibrary.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ConfigurationProperties(prefix = "cors")
public class CorsConfig {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    /*
        @Bean: đánh dấu method này trả về một Bean mà Spring Boot sẽ quản lý.
        Bean có kiểu WebMvcConfigurer, do Spring MVC framework sd để tùy chỉnh cấu hình web(CORS, interceptors, view resolver, v.v…).
    */
    // Cấu hình chính sách CORS cho toàn hệ thống web
    // Define các quy tắc CORS — cho phép FE đang chạy ở Origin khác (=protocol + domain + port) gọi đến API backend này
    @Bean
    public WebMvcConfigurer corsConfigure() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/**") // Cho phép CORS với tất cả endpoint/URL (/** =  mọi đường dẫn) trong hệ thống
                        .allowedMethods(allowedOrigins) // Every HTTP method (GET, POST, PUT, DELETE..) can be used by Client
                        .allowedOrigins(allowedOrigins) // Every domain(every origin) can send request to this API
                        .allowedHeaders(allowedOrigins); // Every Header type(Content-Type, Authorization, Accept, …) can be sent from Client
            }
        };
    }
}
