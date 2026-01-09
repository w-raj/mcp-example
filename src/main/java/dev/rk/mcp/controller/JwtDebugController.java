package dev.rk.mcp.controller;

import dev.rk.mcp.security.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/debug")
public class JwtDebugController {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Test endpoint to view JWT claims from the request
     */
    @GetMapping("/jwt-claims")
    public ResponseEntity<Map<String, Object>> getJwtClaims(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        String username = (String) request.getAttribute("jwt.username");
        Claims claims = (Claims) request.getAttribute("jwt.claims");
        String token = (String) request.getAttribute("jwt.token");

        if (claims != null) {
            response.put("username", username);
            response.put("subject", claims.getSubject());
            response.put("issuer", claims.getIssuer());
            response.put("issuedAt", claims.getIssuedAt());
            response.put("expiration", claims.getExpiration());
            response.put("allClaims", claims);
            response.put("tokenPresent", token != null);
        } else {
            response.put("error", "No JWT token found in request");
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Test endpoint to validate a JWT token
     */
    @PostMapping("/validate-token")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String token = payload.get("token");

        if (token == null || token.isEmpty()) {
            response.put("error", "Token is required");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            boolean isValid = jwtUtil.validateToken(token);
            response.put("valid", isValid);

            if (isValid) {
                String username = jwtUtil.extractUsername(token);
                Claims claims = jwtUtil.extractAllClaims(token);

                response.put("username", username);
                response.put("subject", claims.getSubject());
                response.put("issuer", claims.getIssuer());
                response.put("expiration", claims.getExpiration());
                response.put("claims", claims);
            }
        } catch (Exception e) {
            response.put("valid", false);
            response.put("error", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}
