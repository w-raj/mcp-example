package dev.rk.mcp.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        String token = null;
        String username = null;

        // Extract JWT token from Authorization header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            
            try {
                // Validate token signature and extract claims
                if (jwtUtil.validateToken(token)) {
                    username = jwtUtil.extractUsername(token);
                    Claims claims = jwtUtil.extractAllClaims(token);
                    
                    logger.info("JWT Token validated successfully");
                    logger.info("Username: {}", username);
                    logger.info("Issuer: {}", claims.getIssuer());
                    logger.info("Subject: {}", claims.getSubject());
                    logger.info("Expiration: {}", claims.getExpiration());
                    
                    // Add claims to request attributes for downstream use
                    request.setAttribute("jwt.username", username);
                    request.setAttribute("jwt.claims", claims);
                    request.setAttribute("jwt.token", token);
                    
                    // Log custom claims if any
                    claims.forEach((key, value) -> {
                        if (!key.startsWith("iat") && !key.startsWith("exp") && 
                            !key.startsWith("sub") && !key.startsWith("iss")) {
                            logger.info("Custom claim - {}: {}", key, value);
                        }
                    });
                } else {
                    logger.warn("Invalid JWT token - validation failed");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\": \"Invalid or expired JWT token\"}");
                    return;
                }
            } catch (ExpiredJwtException e) {
                logger.error("JWT token expired: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"JWT token has expired\"}");
                return;
            } catch (MalformedJwtException e) {
                logger.error("Malformed JWT token: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Malformed JWT token\"}");
                return;
            } catch (SignatureException e) {
                logger.error("Invalid JWT signature: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Invalid JWT signature\"}");
                return;
            } catch (Exception e) {
                logger.error("Error processing JWT token: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Error processing JWT token\"}");
                return;
            }
        } else {
            logger.info("No JWT token found in Authorization header");
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        
        // Skip JWT validation for debug endpoints and health checks
        return path.startsWith("/debug") || 
               path.equals("/actuator/health") ||
               path.equals("/health");
    }
}
