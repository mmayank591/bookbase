package com.bookbase.bookservice.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;


public class GatewayHeaderAuthFilter extends OncePerRequestFilter {

    private final String ID_HEADER_NAME = "X-User-ID";
    private final String ROLES_HEADER_NAME = "X-User-Roles";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String userId = request.getHeader(ID_HEADER_NAME);
        String rolesString = request.getHeader(ROLES_HEADER_NAME);

        if (userId == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Collection<? extends GrantedAuthority> authorities = Collections.emptyList();

            if (rolesString != null && !rolesString.isEmpty()) {
                authorities = Arrays.stream(rolesString.split(","))
                        .map(String::trim)
                        .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    authorities
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            logger.error("Error processing Gateway Header Authentication, token may be malformed or missing roles.", e);
        }

        filterChain.doFilter(request, response);
    }
}
