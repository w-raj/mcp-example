package dev.rk.mcp;

import java.util.List;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import dev.rk.mcp.service.UserService;

@SpringBootApplication
public class McpSpringJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(McpSpringJavaApplication.class, args);
	}

	@Bean
    public List<ToolCallback> mcpTools(UserService userService) {
        return List.of(ToolCallbacks.from(userService));
    }

}
