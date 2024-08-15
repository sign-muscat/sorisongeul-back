package com.sorisonsoon.common.config;

import com.sorisonsoon.common.security.filter.CustomAuthenticationFilter;
import com.sorisonsoon.common.security.filter.JwtAuthenticationFilter;
import com.sorisonsoon.common.security.hadler.JwtAccessDeniedHandler;
import com.sorisonsoon.common.security.hadler.JwtAuthenticationEntryPoint;
import com.sorisonsoon.common.security.util.TokenUtils;
import com.sorisonsoon.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final TokenUtils tokenUtils;

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
                                        "/api/v1/swagger-ui/**", "/api/swagger-ui/**", "/api/v1/api-docs/**", "/api/v1",
                                        "/images/**", "/api/v1/community/list",
                                        "/api/v1/sentence/game_start", "/api/v1/sign/game_start", "/api/v1/voice/question"
                                    ).permitAll();
                    auth.requestMatchers(HttpMethod.POST,
                                         "/api/v1/login", "/api/v1/token/issue",
                                         "/api/v1/users/new", "/api/v1/users/mailConfirm", "/api/v1/users/verifyCode",
                                         "/api/v1/verify/**").permitAll();

                    auth.requestMatchers(HttpMethod.GET,
                                        "/api/v1/search/**",
                                        "/api/v1/friends/**",
                                        "/api/v1/sentence/**", "/api/v1/sign/**",
                                        "/api/v1/voice/**","/api/v1/page/**"
                                    ).hasAnyAuthority("FREE_USER", "PREMIUM_USER", "ADMIN");

                    auth.requestMatchers(HttpMethod.POST,
                                        "/api/v1/logout",
                                        "api/v1/friends/**", "/api/v1/page/**",
                                        "api/v1/sentence/**", "api/v1/sign/**","/api/v1/voice/**" )
                                            .hasAnyAuthority("FREE_USER", "PREMIUM_USER", "ADMIN");

                    auth.requestMatchers(HttpMethod.POST,
                                        "api/v1/premium/**")
                                            .hasAuthority("FREE_USER");

                    auth.requestMatchers("/api/v1/notice/**").hasAuthority("ADMIN");
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.accessDeniedHandler(jwtAccessDeniedHandler());
                    exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint());
                })
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .build();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        /* TODO :: 추후 설정 */
//        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000/"));
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "DELETE"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Origin",
                "Access-Control-Allow-Headers",
                "Content-Type",
                "Authorization",
                "X-Requested-With",
                "Access-Token",
                "Refresh-Token"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Access-Token", "Refresh-Token"));
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(authService);
        return new ProviderManager(provider);
    }

    @Bean
    CustomAuthenticationFilter customAuthenticationFilter() {

        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter();
        customAuthenticationFilter.setAuthenticationManager(authenticationManager());

        return customAuthenticationFilter;
    }

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(authService, tokenUtils);
    }

    @Bean
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    JwtAccessDeniedHandler jwtAccessDeniedHandler() {
        return new JwtAccessDeniedHandler();
    }

}
