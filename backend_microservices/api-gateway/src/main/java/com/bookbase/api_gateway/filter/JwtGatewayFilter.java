package com.bookbase.api_gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;


@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {

    @Value("${jwt.secret.key}")
    private String secret;

    private static final String ID_HEADER_NAME = "X-User-ID";
    private static final String ROLES_HEADER_NAME = "X-User-Roles";
    private static final String ROLES_CLAIM = "userRole";

    private final List<String> openApiEndpoints = List.of(
            "/auth/login",
            "/auth/register",
            "/v3/api-docs",
            "/swagger-ui"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        if (isFreeAccess(request.getURI().getPath())) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return this.onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();
            
            Object rolesObject = claims.get(ROLES_CLAIM);
            String rolesString = rolesObject != null ? rolesObject.toString() : "";
            
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header(ID_HEADER_NAME, userId)
                    .header(ROLES_HEADER_NAME, rolesString)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (SignatureException e) {
            return this.onError(exchange, "Invalid JWT signature", HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            return this.onError(exchange, "JWT token expired", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return this.onError(exchange, "Invalid JWT or processing error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        
        String errorBody = "{\"Uncaught Exception\": \"" + message + "\"}";
        
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
        return response.writeWith(Mono.just(response.bufferFactory().wrap(errorBody.getBytes())));
    }

    private boolean isFreeAccess(String path) {
        return openApiEndpoints.stream().anyMatch(path::contains);
    }

    @Override
    public int getOrder() {
        return -1; 
    }
}
