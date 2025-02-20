package com.client.onboarding.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class AppConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth
		    .requestMatchers("/api/**").authenticated()
		    .anyRequest().permitAll()
		)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(new JwtValidator(), BasicAuthenticationFilter.class)
        .httpBasic(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
			
			@Override 
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration cfg = new CorsConfiguration();
				cfg.setAllowedOrigins(Arrays.asList(
						"http://localhost:5173",
						"http://localhost:5174",
						"http://localhost:5175",
						"http://localhost:4200"
				));
				
				cfg.setAllowedMethods(Collections.singletonList("*"));
				cfg.setAllowCredentials(true);
				cfg.setAllowedHeaders(Collections.singletonList("*"));
				cfg.setExposedHeaders(Arrays.asList("Authorization"));
				cfg.setMaxAge(3600L);
				
				return cfg;
			}
		}));        
        return http.build();
    }


    @Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
}
