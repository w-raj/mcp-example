package dev.rk.mcp.config;

/**
 * Custom MCP HTTP Controller - DISABLED
 * 
 * This controller is not needed as Spring AI MCP provides built-in SSE transport support
 * through WebMvcSseServerTransport which automatically registers:
 * - GET /sse - SSE connection endpoint for MCP communication
 * - POST /mcp/message - Message handling endpoint (used internally by SSE transport)
 * 
 * The built-in implementation handles all MCP protocol requirements including:
 * - Server-Sent Events for bi-directional communication
 * - JSON-RPC 2.0 message handling
 * - Proper async/reactive processing with Reactor
 * 
 * Configuration is controlled via application.yml:
 *   spring.ai.mcp.server.enabled: true
 *   spring.ai.mcp.server.type: SYNC
 *   spring.ai.mcp.server.stdio: false
 */
public class McpHttpController {
    // This class is kept for reference but is not active
    // All MCP HTTP/SSE functionality is provided by Spring AI MCP auto-configuration
}
