//package com.pgapp.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // disable CSRF for testing with Postman
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/owners/register", "/api/owners/login").permitAll() // public endpoints
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(Customizer.withDefaults()); // optional: basic auth for other endpoints
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}


package com.pgapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // enable CORS support (uses corsConfigurationSource bean below)
                .cors(Customizer.withDefaults())

                // For development/testing: you can disable CSRF (or ignore only specific endpoints)
                .csrf(csrf -> csrf.disable())

                // allow H2 console frames
                .headers(headers -> headers.frameOptions().sameOrigin())

                .authorizeHttpRequests(auth -> auth
                        // public endpoints (no authentication required)
                        .requestMatchers("/api/owners/register", "/api/owners/login").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/pgs/**").permitAll()
                        .requestMatchers("/api/pgs/localities/**").permitAll()
                        .requestMatchers("/api/floors/**").permitAll()
                        .requestMatchers("/api/rooms/**").permitAll()
                        .requestMatchers("/api/beds/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/api/tenants/**").permitAll()
                        .requestMatchers("/api/applications/**").permitAll()

                        .requestMatchers("/api/daily-bookings/**").permitAll()
                        .requestMatchers("/api/complaints/**").permitAll()


                        .requestMatchers("/images/**", "/css/**", "/js/**").permitAll()

                        // everything else requires authentication
                        .anyRequest().authenticated()
                )

                // keep HTTP Basic for other endpoints (optional; remove if you DON'T want browser basic-auth)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
//@Bean
//public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http
//            .cors(Customizer.withDefaults())
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // all endpoints public
//    return http.build();
//}

    // CORS configuration allowing Angular dev server
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // allow only your Angular dev origin
//        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedOrigins(List.of(
                "http://localhost:4200",  // owner app
                "http://localhost:4300"   // tenant app
        ));

        // methods you use
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // allow all headers (or list the ones you need)
        config.setAllowedHeaders(List.of("*"));

        // allow credentials (cookies / auth headers) if needed
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // apply to all endpoints
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
