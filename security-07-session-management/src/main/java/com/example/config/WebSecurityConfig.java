package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 自定义Spring Security配置
 *
 * @author xiaoning
 * @date 2022/10/03
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin();

        // 开启会话管理
        http.sessionManagement()
            // 设置会话的最大数量。
            .maximumSessions(1);

        http.authorizeRequests()
            .anyRequest().authenticated();

        http.csrf().disable();

    }
}
