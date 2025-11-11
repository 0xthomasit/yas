package com.yas.product;

import com.yas.commonlibrary.config.CorsConfig;
import com.yas.commonlibrary.config.ServiceUrlConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = {"com.yas.product", "com.yas.commonlibrary"})
@EnableConfigurationProperties({ServiceUrlConfig.class, CorsConfig.class})
public class ProductApplication {
    // 🎯 IMPORTANT: This is the main entry point for the Product microservice
    // - Scans both product and common-library packages for Spring components
    // - Enables configuration properties for service URLs and CORS settings
    // - Each microservice has its own independent application like this

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }

}
