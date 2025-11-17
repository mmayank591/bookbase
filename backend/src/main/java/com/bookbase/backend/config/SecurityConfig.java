package com.bookbase.backend.config;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
 
@Configuration
@EnableWebSecurity
public class SecurityConfig {
 
    @Autowired
    private UserDetailsService userDetailsService;
 
    @Autowired
    private JwtFilter jwtFilter;
 
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
 
        return http.csrf(customizer -> customizer.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // Public and Swagger
                        .requestMatchers("/auth/login", "/auth/register", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // MEMBERS
                        .requestMatchers(HttpMethod.GET,    "/member/getbyusername/*").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers(HttpMethod.GET,    "/member/getallusers").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/member/getbymemberid/*").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers(HttpMethod.POST,   "/member/createnew").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/member/updateput/*").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers(HttpMethod.PATCH,  "/member/updatepatch/*").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers(HttpMethod.DELETE, "/member/deleteuser/*").hasRole("ADMIN")

                        // BOOKS
                        .requestMatchers(HttpMethod.GET,    "/book/getallbooks").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers(HttpMethod.GET,    "/book/getbyid/*").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers(HttpMethod.POST,   "/book/createnew").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/book/updateput/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,  "/book/availablecount/*").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers(HttpMethod.DELETE, "/book/deletebook/*").hasRole("ADMIN")

                        // TRANSACTIONS
                        .requestMatchers(HttpMethod.GET,    "/transaction/getalltransactions").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers(HttpMethod.GET,    "/transaction/getbyid/*").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers(HttpMethod.GET,    "/transaction/getbymemberid/*").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers(HttpMethod.POST,   "/transaction/createnew").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers(HttpMethod.PATCH,  "/transaction/updatestatus/*").hasAnyRole("ADMIN", "MEMBER")

                        // FINES
                        .requestMatchers(HttpMethod.GET,    "/fine/getallfines").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers(HttpMethod.GET,    "/fine/getbyid/*").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers(HttpMethod.GET,    "/fine/getbymemberid/*").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers(HttpMethod.GET,    "/fine/getbystatus/*").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers(HttpMethod.GET,    "/fine/getbytransactionid/*").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers(HttpMethod.POST,   "/fine/createnew").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers(HttpMethod.PUT,    "/fine/updateput/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,  "/fine/updatepatch/*").hasAnyRole("ADMIN", "MEMBER")

                        // NOTIFICATIONS
                        .requestMatchers(HttpMethod.GET,    "/notification/getallnotifications").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers(HttpMethod.GET,    "/notification/getbyid*").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers(HttpMethod.GET,    "/notification/getbymemberid/*").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers(HttpMethod.POST,   "/notification/createnew").hasAnyRole("ADMIN", "MEMBER")
                        .requestMatchers(HttpMethod.DELETE, "/notification/delete/*").hasAnyRole("ADMIN", "MEMBER")

                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
 
 
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
 
        return provider;
    }
 
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}