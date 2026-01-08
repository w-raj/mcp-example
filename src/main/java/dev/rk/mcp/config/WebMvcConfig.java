package dev.rk.mcp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;

    public WebMvcConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // In Spring Boot 4.0, message converters are auto-configured with the ObjectMapper bean
        // The shared ObjectMapper will be used automatically
        System.out.println("=== MVC Message Converters Configured ===");
        System.out.println("Using shared ObjectMapper for HTTP responses");
    }
}
