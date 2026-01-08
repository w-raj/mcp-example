package dev.rk.mcp.config;

import io.modelcontextprotocol.server.McpSyncServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/debug")
public class McpDebugController {
    
    private static final Logger logger = LoggerFactory.getLogger(McpDebugController.class);
    
    @Autowired
    private McpSyncServer mcpSyncServer;
    
    @GetMapping("/mcp-status")
    public ResponseEntity<Map<String, Object>> getMcpStatus() throws Exception {
        Map<String, Object> status = new HashMap<>();
        
        // Check if MCP server is initialized
        Field asyncServerField = mcpSyncServer.getClass().getDeclaredField("asyncServer");
        asyncServerField.setAccessible(true);
        Object asyncServer = asyncServerField.get(mcpSyncServer);
        
        status.put("mcpSyncServer", mcpSyncServer.getClass().getName());
        status.put("asyncServer", asyncServer != null ? asyncServer.getClass().getName() : "null");
        
        // Get transport info
        Field delegateField = asyncServer.getClass().getDeclaredField("delegate");
        delegateField.setAccessible(true);
        Object delegate = delegateField.get(asyncServer);
        
        Field mcpSessionField = delegate.getClass().getDeclaredField("mcpSession");
        mcpSessionField.setAccessible(true);
        Object mcpSession = mcpSessionField.get(delegate);
        
        Field transportField = mcpSession.getClass().getDeclaredField("transport");
        transportField.setAccessible(true);
        Object transport = transportField.get(mcpSession);
        
        status.put("transport", transport.getClass().getName());
        
        // Get endpoint info
        Field messageEndpointField = transport.getClass().getDeclaredField("messageEndpoint");
        messageEndpointField.setAccessible(true);
        String messageEndpoint = (String) messageEndpointField.get(transport);
        
        Field sseEndpointField = transport.getClass().getDeclaredField("sseEndpoint");
        sseEndpointField.setAccessible(true);
        String sseEndpoint = (String) sseEndpointField.get(transport);
        
        status.put("messageEndpoint", messageEndpoint);
        status.put("sseEndpoint", sseEndpoint);
        status.put("status", "MCP Server is initialized and configured");
        
        return ResponseEntity.ok(status);
    }
    
    @PostMapping(value = "/test-json", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> testJson(@RequestBody String requestBody) {
        logger.info("========== JSON ENDPOINT TEST ==========");
        logger.info("Request body: {}", requestBody);
        
        String response = "{\"status\":\"success\",\"message\":\"JSON endpoint works\",\"receivedLength\":" + requestBody.length() + "}";
        logger.info("Sending response: {}", response);
        logger.info("========================================");
        
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
    }
}
