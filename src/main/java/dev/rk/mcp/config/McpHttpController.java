package dev.rk.mcp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpSyncServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@RestController
@Order(1) // Ensure this takes priority over RouterFunctions
public class McpHttpController {
    
    private static final Logger logger = LoggerFactory.getLogger(McpHttpController.class);
    
    @Autowired
    private McpSyncServer mcpSyncServer;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    // Override the SSE endpoint to provide synchronous HTTP transport
    @PostMapping(value = "/mcp/message", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleHttpMcpRequest(@RequestBody String requestBody) {
        logger.info("========== HTTP MCP REQUEST (streamable) ==========");
        logger.info("Request: {}", requestBody);
        
        try {
            // Get the async server from sync server via reflection
            Field asyncServerField = mcpSyncServer.getClass().getDeclaredField("asyncServer");
            asyncServerField.setAccessible(true);
            Object asyncServer = asyncServerField.get(mcpSyncServer);
            
            // Call handleRequest on async server
            Method handleRequestMethod = asyncServer.getClass().getMethod("handleRequest", Object.class);
            Object responseMono = handleRequestMethod.invoke(asyncServer, objectMapper.readValue(requestBody, Object.class));
            
            // Get Mono.block() to make it synchronous
            Method blockMethod = responseMono.getClass().getMethod("block");
            Object responseObj = blockMethod.invoke(responseMono);
            
            // Convert response to JSON
            String response = objectMapper.writeValueAsString(responseObj);
            
            logger.info("Response: {}", response);
            logger.info("Response length: {} bytes", response.length());
            logger.info("===================================================");
            
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
        } catch (Exception e) {
            logger.error("Error processing MCP HTTP request", e);
            String errorResponse = String.format(
                "{\"jsonrpc\":\"2.0\",\"error\":{\"code\":-32603,\"message\":\"%s\"},\"id\":null}",
                e.getMessage().replace("\"", "\\\"")
            );
            return ResponseEntity.status(500)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
        }
    }
}
