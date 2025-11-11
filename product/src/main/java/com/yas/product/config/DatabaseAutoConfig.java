package com.yas.product.config;

import java.util.Optional;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// AuditorAware_step_2: At Runtime in user-service:
@Configuration
@EnableJpaRepositories("com.yas.product.repository")
@EntityScan({"com.yas.product.model", "com.yas.product.model.attribute"})

@EnableJpaAuditing(auditorAwareRef = "auditorAware") // Creates "AuditingHandler" bean in THIS service's context
public class DatabaseAutoConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            // Product service specific logic
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null) {
                return Optional.of("");
            }
            return Optional.of(auth.getName());
        };
    }
}

/* NOTES:
    + "auditorAwareRef" parameter: (which Bean we need to use for auditing)
        a. Definition:
            _ Tells the Spring to use the Bean with name of "auditorAware" for auditing.
            _ "Name-based matching" instead of "Type-based matching"
        b. Scenarios:
            1. Multiple AuditorAware Beans in the same context: (error: "Multiple beans of type AuditorAware found")
            2. Non-standard Bean name: (The Bean name doesn't match the Spring's default expectation).
            3.

 */