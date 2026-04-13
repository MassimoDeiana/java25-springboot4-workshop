package com.bankflow.workshop.exercise7_versioning;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SOLUTION: Exercise 7 (Bonus) — Native API Versioning
 *
 * Central versioning config. URLs stay /api/v1/... and /api/v2/...
 */
@Configuration
public class ApiVersionConfig implements WebMvcConfigurer {

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        configurer.usePathSegment();
    }
}
