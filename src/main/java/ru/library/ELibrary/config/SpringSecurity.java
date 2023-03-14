package ru.library.ELibrary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers("/admin/*", "/admin").hasRole("ADMIN")
                .requestMatchers("/index").permitAll()
                .requestMatchers("/auth/login", "/auth/registration","/error").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/profile", true)
                .failureUrl("/auth/login?error")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/index");
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity,
                                                       UserDetailsService userDetailsService,
                                                       PasswordEncoder encoder) throws Exception{
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(encoder).and().build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
