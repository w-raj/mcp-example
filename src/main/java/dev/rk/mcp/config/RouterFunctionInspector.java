package dev.rk.mcp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import jakarta.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Map;

@Component
public class RouterFunctionInspector {
    
    private static final Logger logger = LoggerFactory.getLogger(RouterFunctionInspector.class);
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @PostConstruct
    public void inspectRouters() {
        logger.info("==========================================");
        logger.info("Inspecting ALL RouterFunction beans...");
        
        Map<String, RouterFunction> routerFunctions = applicationContext.getBeansOfType(RouterFunction.class);
        logger.info("Found {} RouterFunction beans:", routerFunctions.size());
        
        routerFunctions.forEach((name, router) -> {
            logger.info("  - Bean name: {}", name);
            logger.info("    Type: {}", router.getClass().getName());
            
            // Try to invoke toString to see the routes
            logger.info("    Routes: {}", router.toString());
            
            // Test if it matches /mcp/message
            try {
                // Create a mock request to test routing
                logger.info("    Testing route matching...");
            } catch (Exception e) {
                logger.error("    Error inspecting router: {}", e.getMessage());
            }
        });
        
        logger.info("==========================================");
    }
}
