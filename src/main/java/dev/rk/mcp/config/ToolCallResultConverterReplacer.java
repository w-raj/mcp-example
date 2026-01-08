package dev.rk.mcp.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class ToolCallResultConverterReplacer implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // Log ALL beans to see what's being created
        String className = bean.getClass().getName();
        if (className.contains("Tool") || className.contains("Mcp") || className.contains("Converter")) {
            System.out.println(">>> Bean created: " + beanName + " -> " + className);
        }
        
        // Intercept ALL ObjectMapper instances and reconfigure them
        if (bean instanceof ObjectMapper && !beanName.equals("objectMapper")) {
            ObjectMapper mapper = (ObjectMapper) bean;
            System.out.println("\n!!! INTERCEPTED ObjectMapper bean: " + beanName + " !!!");
            System.out.println("Reconfiguring to disable FAIL_ON_EMPTY_BEANS and FAIL_ON_UNKNOWN_PROPERTIES");
            
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            
            System.out.println("ObjectMapper reconfigured successfully!\n");
        }
        
        // Try to find and reconfigure ObjectMappers in other beans via reflection
        if (className.contains("ToolCallResultConverter") || className.contains("McpSync") || className.contains("McpAsync")) {
            System.out.println("\n=== FOUND MCP/Tool bean: " + className + " ===");
            
            try {
                reconfigureObjectMappersInBean(bean, bean.getClass(), 0);
            } catch (Exception e) {
                System.err.println("ERROR accessing fields: " + e.getMessage());
            }
        }
        
        return bean;
    }
    
    private void reconfigureObjectMappersInBean(Object bean, Class<?> clazz, int depth) throws Exception {
        if (depth > 3) return; // Prevent infinite recursion
        
        Field[] fields = clazz.getDeclaredFields();
        String indent = "  ".repeat(depth);
        
        System.out.println(indent + "Checking fields in " + clazz.getSimpleName() + ":");
        for (Field f : fields) {
            System.out.println(indent + "  - " + f.getName() + " : " + f.getType().getName());
            
            if (f.getType().equals(ObjectMapper.class)) {
                f.setAccessible(true);
                ObjectMapper fieldMapper = (ObjectMapper) f.get(bean);
                if (fieldMapper != null) {
                    System.out.println(indent + "  >>> FOUND ObjectMapper in field: " + f.getName());
                    fieldMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                    fieldMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    System.out.println(indent + "  >>> RECONFIGURED ObjectMapper successfully!");
                }
            } else if (!f.getType().isPrimitive() && !f.getType().getName().startsWith("java.lang")) {
                // Recurse into complex fields
                f.setAccessible(true);
                Object fieldValue = f.get(bean);
                if (fieldValue != null && !fieldValue.getClass().getName().startsWith("java.")) {
                    System.out.println(indent + "  -> Recursing into: " + f.getName());
                    reconfigureObjectMappersInBean(fieldValue, fieldValue.getClass(), depth + 1);
                }
            }
        }
    }
}
