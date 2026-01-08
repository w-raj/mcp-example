package dev.rk.mcp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class McpRouterConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(McpRouterConfig.class);
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @EventListener(ApplicationReadyEvent.class)
    public void logEndpoints() {
        logger.info("==========================================");
        logger.info("ALL REGISTERED ENDPOINTS:");
        
        try {
            RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
            mapping.getHandlerMethods().forEach((key, value) -> {
                logger.info("  {} -> {}", key, value);
            });
        } catch (Exception e) {
            logger.error("Error getting RequestMappingHandlerMapping", e);
        }
        
        logger.info("RouterFunction beans:");
        applicationContext.getBeansOfType(RouterFunction.class).forEach((name, bean) -> {
            logger.info("  Bean: {} -> {}", name, bean.getClass().getName());
            logger.info("  ToString: {}", bean.toString());
        });
        
        logger.info("==========================================");
    }
}
