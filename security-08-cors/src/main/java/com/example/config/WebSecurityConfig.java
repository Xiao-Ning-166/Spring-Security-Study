package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * 自定义Spring Security配置（Spring Boot2.7.x版本）
 *
 * @author xiaoning
 * @date 2022/10/04
 */
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // 使用表单认证
        httpSecurity.formLogin();

        httpSecurity.authorizeRequests()
                // 所有请求认证后才能访问
                .anyRequest().authenticated();

        // 开启跨域资源共享
        httpSecurity.cors()
                // 进行跨域配置
                .configurationSource(corsConfigurationSource());

        // 关闭csrf
        httpSecurity.csrf().disable();

        return httpSecurity.build();
    }

    public CorsConfigurationSource corsConfigurationSource() {
        // 跨域配置
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setMaxAge(360L);
        // 对所有请求应用以上配置
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

}
