package dev.rk.mcp.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class ResponseLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpResponse);
        
        long startTime = System.currentTimeMillis();
        chain.doFilter(requestWrapper, responseWrapper);
        long duration = System.currentTimeMillis() - startTime;
        
        // Log request/response for MCP endpoints
        if (httpRequest.getRequestURI().contains("/mcp")) {
            // Log Request
            byte[] requestBody = requestWrapper.getContentAsByteArray();
            String requestString = new String(requestBody, StandardCharsets.UTF_8);
            
            System.out.println("\n========== MCP REQUEST ==========");
            System.out.println("Method: " + httpRequest.getMethod());
            System.out.println("URI: " + httpRequest.getRequestURI());
            System.out.println("Content-Type: " + httpRequest.getContentType());
            System.out.println("Request Body length: " + requestBody.length);
            if (requestBody.length > 0 && requestBody.length < 5000) {
                System.out.println("Request Body:\n" + requestString);
            }
            
            // Log Response
            byte[] responseBody = responseWrapper.getContentAsByteArray();
            String responseString = new String(responseBody, StandardCharsets.UTF_8);
            
            System.out.println("\n========== MCP RESPONSE ==========");
            System.out.println("Status: " + responseWrapper.getStatus());
            System.out.println("Content-Type: " + responseWrapper.getContentType());
            System.out.println("Response Body length: " + responseBody.length + " bytes");
            System.out.println("Duration: " + duration + "ms");
            if (responseBody.length > 0 && responseBody.length < 5000) {
                System.out.println("Response Body:\n" + responseString);
            } else if (responseBody.length >= 5000) {
                System.out.println("Response Body (first 1000 chars):\n" + responseString.substring(0, 1000));
            } else {
                System.out.println("Response Body: **EMPTY** <- This is the problem!");
            }
            System.out.println("========================================\n");
        }
        
        responseWrapper.copyBodyToResponse();
    }
}
