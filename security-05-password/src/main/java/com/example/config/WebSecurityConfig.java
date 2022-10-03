package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * 自定义Spring Security配置类
 *
 * @author xiaoning
 * @date 2022/10/03
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public UserDetailsService userDetailsManager() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        /**
         * 第一种加密方式：DelegatingPasswordEncoder进行委托代理，可以兼容多种加密方式的密码
         */
        // inMemoryUserDetailsManager.createUser(User.withUsername("root").password("{noop}root").roles("admin").build());
        inMemoryUserDetailsManager.createUser(User.withUsername("root").password("{bcrypt}$2a$10$ru0tbxVFazpCugyAUB36i.tklGW.OFzAqsaPj048E.o.hN2Mrw7d6").roles("admin").build());
        return inMemoryUserDetailsManager;
    }

    /**
     * 设置全局自定义的AuthenticationManager
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsManager());
    }

    /**
     *
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 表单认证方式
        http.formLogin();

        http.authorizeRequests()
            // 请求需要认证才能访问
            .anyRequest().authenticated();

        // 关闭csrf防护
        http.csrf().disable();
    }
}
