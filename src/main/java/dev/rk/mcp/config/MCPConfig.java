package dev.rk.mcp.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.rk.mcp.service.UserService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.execution.ToolCallResultConverter;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MCPConfig {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Autowired
    public MCPConfig(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Bean
    ToolCallbackProvider userTools() {
        return MethodToolCallbackProvider
                .builder()
                .toolObjects(userService)
                .build();
    }

    @Bean
    @org.springframework.context.annotation.Primary
    public ToolCallResultConverter toolCallResultConverter() {
        System.out.println("\n\n=== CREATING CUSTOM ToolCallResultConverter WITH SHARED ObjectMapper ===");
        System.out.println("=== This converter will be used for ALL tool result serialization ===");
        System.out.println("\n");
        return new ToolCallResultConverter() {
            @Override
            public String convert(Object result, java.lang.reflect.Type targetType) {
                System.out.println("\n=== CUSTOM ToolCallResultConverter.convert() CALLED ===");
                System.out.println("Result type: " + (result != null ? result.getClass().getName() : "null"));
                System.out.println("Target type: " + targetType);
                
                if (result == null) {
                    System.out.println("Result is NULL, returning \"null\"");
                    return "null";
                }
                try {
                    String json = objectMapper.writeValueAsString(result);
                    System.out.println("Successfully serialized to JSON (" + json.length() + " chars)");
                    System.out.println("JSON preview: " + json.substring(0, Math.min(200, json.length())));
                    System.out.println("=== END ToolCallResultConverter ===");
                    return json;
                } catch (JsonProcessingException e) {
                    System.err.println("ERROR serializing tool result: " + e.getMessage());
                    e.printStackTrace();
                    return "{\"error\":\"Serialization failed: " + e.getMessage() + "\"}";
                }
            }
        };
    }

}
