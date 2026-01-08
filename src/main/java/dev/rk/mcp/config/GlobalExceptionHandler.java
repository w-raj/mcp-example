package dev.rk.mcp.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        System.err.println("\n\n!!! GLOBAL EXCEPTION CAUGHT !!!");
        System.err.println("Exception type: " + ex.getClass().getName());
        System.err.println("Message: " + ex.getMessage());
        System.err.println("Stack trace:");
        ex.printStackTrace();
        System.err.println("!!! END EXCEPTION !!!\n\n");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\":\"" + ex.getMessage() + "\"}");
    }
    
    @ExceptionHandler(com.fasterxml.jackson.core.JsonProcessingException.class)
    public ResponseEntity<String> handleJsonProcessingException(com.fasterxml.jackson.core.JsonProcessingException ex) {
        System.err.println("\n\n!!! JACKSON SERIALIZATION EXCEPTION !!!");
        System.err.println("Message: " + ex.getMessage());
        ex.printStackTrace();
        System.err.println("!!! END JACKSON EXCEPTION !!!\n\n");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\":\"JSON serialization failed: " + ex.getMessage() + "\"}");
    }
}
