package com.shoppin.ecommerce.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.shoppin.ecommerce.filter.JwtFilter;
import com.shoppin.ecommerce.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Autowired
	private JwtFilter jwtFilter;
    
    @Bean
    public AuthenticationProvider authProvider() {
    	
    	DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
    	provider.setPasswordEncoder(passwordEncoder());
    	return provider;
    }
    
    @Bean
    public SecurityFilterChain authFilter(HttpSecurity http) throws Exception{
    	return http
    			.csrf(csrf -> csrf
    		            .ignoringRequestMatchers("/h2-console/**")
    		            .disable()
    		        )
    		        .headers(headers -> headers
    		            .frameOptions(frame -> frame.sameOrigin())
    		        )
    			.authorizeHttpRequests(req->req
    					.requestMatchers("/public/**").permitAll()
    					.requestMatchers("/h2-console/**").permitAll()
    					.requestMatchers("/auth/admin/**").hasAuthority("ADMIN")
    					.requestMatchers("/auth/user/**").hasAnyAuthority("CONSUMER","ADMIN")
    					.requestMatchers("/auth/se/**").hasAnyAuthority("SELLER","ADMIN")
    					.anyRequest().authenticated())
    			.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
    			.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    	return config.getAuthenticationManager();
    }
	
	
}
