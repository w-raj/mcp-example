package dev.rk.mcp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // setUseTrailingSlashMatch() removed in Spring Boot 4.0
        // Trailing slash matching is now handled differently
    }
}
