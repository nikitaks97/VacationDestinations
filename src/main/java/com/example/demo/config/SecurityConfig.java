package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    private static final String[] PUBLIC_URLS = {
        "/",
        "/destination/*",
        "/register",
        "/login",
        "/css/**",
        "/js/**",
        "/api/monitoring/**",
        "/actuator/health"
    };

    private static final String[] ADMIN_URLS = {
        "/destination/new",
        "/admin/**"
    };

    private static final String[] AUTHENTICATED_URLS = {
        "/destination/*/like",
        "/destination/*/comment"
    };

    private final DataSource dataSource;

    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    @Profile("!dev") // Production security settings
    public SecurityFilterChain productionFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_URLS).permitAll()
                .requestMatchers(ADMIN_URLS).hasRole("ADMIN")
                .requestMatchers(AUTHENTICATED_URLS).authenticated()
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .rememberMe(remember -> remember
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(86400) // 1 day
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .deleteCookies("JSESSIONID")
                .permitAll()
            );
        
        return http.build();
    }

    @Bean
    @Profile("dev") // Development security settings
    public SecurityFilterChain developmentFilterChain(HttpSecurity http) throws Exception {
        String[] devPublicUrls = new String[PUBLIC_URLS.length + 2];
        System.arraycopy(PUBLIC_URLS, 0, devPublicUrls, 0, PUBLIC_URLS.length);
        devPublicUrls[PUBLIC_URLS.length] = "/h2-console/**";
        devPublicUrls[PUBLIC_URLS.length + 1] = "/actuator/**";

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(devPublicUrls).permitAll()
                .requestMatchers(ADMIN_URLS).hasRole("ADMIN")
                .requestMatchers(AUTHENTICATED_URLS).authenticated()
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
}
