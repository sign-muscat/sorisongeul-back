package com.sorisonsoon.common.config;

import com.sorisonsoon.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthService authService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.
                csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManage -> sessionManage.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                //TODO: REST API 문서 공통 정리 한 후 다시 정리 예정
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET,
                                        "/images/**", "/api/v1/find/**",
                                        "/api/v1/notice/list", "/api/v1/community/list")
                                            .permitAll();
                    auth.requestMatchers(HttpMethod.POST,
                                         "/api/v1/members/new/**", "/api/v1/members/check/**",
                                         "/api/v1/login", "/api/v1/token/issue",
                                         "/api/v1/verify/**")
                                            .permitAll();

                    auth.requestMatchers(HttpMethod.GET,
                                        "api/v1/friends/**", "/api/members/**",
                                        "api/v1/sentence/**", "api/v1/sign/**")
                                            .hasAnyAuthority("FREE_USER", "PREMIUM_USER", "ADMIN");
                    auth.requestMatchers(HttpMethod.POST,
                                        "/api/v1/logout",
                                        "api/v1/friends/**", "/api/members/**",
                                        "api/v1/sentence/**", "api/v1/sign/**", "" )
                                            .hasAnyAuthority("FREE_USER", "PREMIUM_USER", "ADMIN");

                    auth.requestMatchers(HttpMethod.POST,
                                        "api/v1/premium")
                                            .hasAuthority("FREE_USER");

                    auth.requestMatchers("/api/v1/notice/**").hasAuthority("ADMIN");
                    auth.anyRequest().authenticated();
                })
                .build();
    }


}
