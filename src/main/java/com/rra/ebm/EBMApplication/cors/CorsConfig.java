package com.rra.ebm.EBMApplication.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow specific origins
        config.addAllowedOrigin("http://localhost:3000"); // Update with your frontend URL

        // Allow specific methods
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS"); // Allow OPTIONS method

        // Allow credentials
        config.setAllowCredentials(true);

        // Allow specific headers
        config.addAllowedHeader("*"); // This allows all headers, or you can specify them individually

        // Set exposed headers (optional, if needed)
        config.addExposedHeader("Authorization"); // Example of an exposed header

        // Register the configuration to all endpoints
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
