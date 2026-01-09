package dev.rk.mcp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.rk.mcp.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Register JWT authentication filter
     * This filter will validate Bearer JWT tokens for all incoming requests
     */
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter() {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtAuthenticationFilter);
        
        // Apply filter to all URLs except excluded ones
        registrationBean.addUrlPatterns("/abc","/debug/*");
        
        // Set order to execute early in the filter chain
        registrationBean.setOrder(1);
        
        System.out.println("\n=== JWT Authentication Filter Registered ===");
        System.out.println("Filter will validate Bearer tokens for all requests");
        System.out.println("Excluded paths: /debug/*, /actuator/health, /health");
        System.out.println();
        
        return registrationBean;
    }
}
