package com.dna_testing_system.dev.security;

import com.dna_testing_system.dev.service.impl.UserDetailsServiceImpl;
import com.dna_testing_system.dev.utils.PasswordUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserDetailsServiceImpl userDetailsService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/css/**", "/js/**", "/images/**", "/webjars/**",
                                "/", "/register", "/login", "/error", "/assets/**",
                                "/uploads/**"
                                // XÓA BỎ "/layouts/**" KHỎI ĐÂY
                        ).permitAll()

                        .requestMatchers("/user/**").authenticated()
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/register")
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/user/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .rememberMe(remember -> remember
                        .key("uniqueAndSecretKey")
                        .tokenValiditySeconds(86400)
                )
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedPage("/access-denied")
                )
                // --- BẮT ĐẦU PHẦN THÊM MỚI ---
                // Thêm cấu hình headers để kiểm soát cache, giúp trình duyệt hoạt động đúng
                .headers(headers -> headers
                        .cacheControl(cache -> {}) // Kích hoạt quản lý Cache-Control mặc định
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()) // Chống clickjacking
                );
        // --- KẾT THÚC PHẦN THÊM MỚI ---

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(PasswordUtil.getPasswordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}