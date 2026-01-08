package dev.rk.mcp.config;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class RestTemplateConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        System.out.println("\n=== Created @Primary ObjectMapper bean ===");
        System.out.println("FAIL_ON_UNKNOWN_PROPERTIES: false");
        System.out.println("FAIL_ON_EMPTY_BEANS: false\n");
        return mapper;
    }

    // Jackson customization is now done directly in the ObjectMapper bean
    // Spring Boot 4.0 removed Jackson2ObjectMapperBuilderCustomizer

    @Bean
    public RestTemplate restTemplate(ObjectMapper objectMapper) throws Exception {
        System.out.println("\n=== Creating RestTemplate ===");
        System.out.println("Using shared ObjectMapper for REST client\n");
        
        // Create SSL context that trusts all certificates
        SSLContext sslContext = SSLContextBuilder
                .create()
                .loadTrustMaterial((chain, authType) -> true)
                .build();

        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
                sslContext,
                (hostname, session) -> true
        );

        HttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder
                .create()
                .setSSLSocketFactory(sslSocketFactory)
                .build();

        HttpClient httpClient = HttpClientBuilder
                .create()
                .setConnectionManager(connectionManager)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = 
                new HttpComponentsClientHttpRequestFactory(httpClient);

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        
        // The shared ObjectMapper will be used automatically by Spring Boot's message converters
        // No need to manually configure it in Spring Boot 4.0
        
        return restTemplate;
    }
}
