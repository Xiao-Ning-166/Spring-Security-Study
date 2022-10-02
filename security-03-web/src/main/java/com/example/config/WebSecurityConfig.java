package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 自定义前后端不分离的Spring Security配置
 *
 * @author xiaoning
 * @date 2022/10/02
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService myUserDetailServiceImpl;

    /**
     * 使用自定义的 UserDetailsService
     *
     * @param auth the {@link AuthenticationManagerBuilder} to use
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailServiceImpl);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 表单提交的认证方式
        http.formLogin()
            // 自定义认证页面
            .loginPage("/login.html")
            // 指定请求认证的url
            .loginProcessingUrl("/doLogin")
            // 指定认证成功后的跳转
            .defaultSuccessUrl("/index.html")
            // 认证失败后重定向
            .failureUrl("/login.html");

        // 退出登录配置
        http.logout()
            // 退出登录请求的url
            .logoutUrl("/doLogout")
            // 退出成功后的请求
            .logoutSuccessUrl("/login.html");

        http.authorizeRequests()
            // 放行 login.html 页面
            .mvcMatchers("/login.html").permitAll()
            .anyRequest().authenticated();

        http.csrf().disable();
    }
}
