package ru.bsuedu.cad.sem2.lab3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build();
        UserDetails moderator = User.withDefaultPasswordEncoder()
            .username("moderator")
            .password("password")
            .roles("MODERATOR")
            .build();
        UserDetails creator = User.withDefaultPasswordEncoder()
            .username("creator")
            .password("password")
            .roles("TASK_CREATOR")
            .build();
        return new InMemoryUserDetailsManager(user, moderator, creator);
    }
}