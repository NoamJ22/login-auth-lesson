package com.lesson.lessons.config;

import com.lesson.lessons.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//marks this class as a Spring configuration class
@EnableWebSecurity
//tells Spring to enable web security for the application and to apply the custom configurations defined here
public class SecurityConfig {
    //this class defines the behavior

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }
    //here I load and inject my custom users details

    @Bean
    //this bean defines the entire HTTP security configuration
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //csrf security is automatic for this version of spring boot, but to test postman
        //I must manually disable it
        http
                //.csrf(csrf -> csrf.disable()) //  DISABLE CSRF ONLY FOR POSTMAN TESTING
                //this is the setup for the URL access
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/about", "/contact", "/css/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                //this decides where to send the user based on his current status (is he logged in/ what type of role he has)
                .formLogin(form -> form
                        .successHandler((request, response, authentication) -> {
                            boolean isAdmin = authentication.getAuthorities().stream()
                                    .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
                            if (isAdmin) {
                                response.sendRedirect("/admin/dashboard");
                            } else {
                                response.sendRedirect("/user/dashboard");
                            }
                        })
                )
                //if a user tries to enter a restricted path for his role, he will be transferred
                //to this path instead
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/access-denied")
                );

        return http.build();
    }

    @Bean
    //this allows Spring Security to validate login attempts using user data
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
        return builder.build();
    }

    @Bean
    //this is for the hashing of passwords
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
