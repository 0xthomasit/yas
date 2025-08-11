package com.yas.commonlibrary.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "yas.services")
public record ServiceUrlConfig(
        String media,
        String product,
        String rating,
        String customer,
        String order,
        String cart,
        String tax,
        String promotion,
        String location
) {
}
