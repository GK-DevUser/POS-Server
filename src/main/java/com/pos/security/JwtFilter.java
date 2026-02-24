package com.pos.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LogManager.getLogger(JwtFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/api/auth/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            try {
                SecurityContextHolder.clearContext();

                Claims claims = jwtUtil.parseToken(header.substring(7));

                JwtUser principal = new JwtUser(
                        claims.get("uid", String.class),
                        claims.get("oid", String.class),
                        claims.get("rid", Integer.class),
                        claims.get("role", String.class) // Raw: "SUPER_ADMIN"
                );

                // ✅ RAW AUTHORITY - NO ROLE_ prefix
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        principal, null, principal.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
                logger.info("JWT authenticated: {} with role: {}", principal.getUid(), principal.getRoleName());

            } catch (Exception e) {
                logger.error("JWT validation failed: {}", e.getMessage());
                sendErrorResponse(response, "Invalid or expired JWT token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> error = Map.of(
                "success", false,
                "message", message,
                "data", null
        );
        objectMapper.writeValue(response.getWriter(), error);
    }
}
