package com.example.config;

import com.example.security.filter.VerifyCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    /**
     * 暴露自定义的全局AuthenticationManager
     *
     * @return the {@link AuthenticationManager}
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 实例化验证码过滤器
     * @return
     */
    @Bean
    public VerifyCodeFilter verifyCodeFilter() throws Exception {
        VerifyCodeFilter verifyCodeFilter = new VerifyCodeFilter();
        // 设置请求url
        verifyCodeFilter.setFilterProcessesUrl("/doLogin");
        // 认证成功的处理
        verifyCodeFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            response.sendRedirect("/index.html");
        });
        // 认证失败的处理
        verifyCodeFilter.setAuthenticationFailureHandler((request, response, exception) -> {
            response.sendRedirect("/login.html");
        });
        // 设置自定义全局的AuthenticationManager
        verifyCodeFilter.setAuthenticationManager(authenticationManagerBean());

        return verifyCodeFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 表单提交的认证方式
        http.formLogin()
            // 自定义认证页面
            .loginPage("/login.html");
            // 指定请求认证的url
            // .loginProcessingUrl("/doLogin")
            // 指定认证成功后的跳转
            // .defaultSuccessUrl("/index.html")
            // 认证失败后重定向
            // .failureUrl("/login.html");

        // 使用 VerifyCodeFilter 替换 UsernamePasswordAuthenticationFilter
        http.addFilterAt(verifyCodeFilter(), UsernamePasswordAuthenticationFilter.class);

        // 退出登录配置
        http.logout()
            // 退出登录请求的url
            .logoutUrl("/doLogout")
            // 退出成功后的请求
            .logoutSuccessUrl("/login.html");

        http.authorizeRequests()
            // 放行 login.html 页面
            .mvcMatchers("/login.html").permitAll()
            // 方向获取验证码的请求
            .mvcMatchers("/verifyCode.jpg").permitAll()
            .anyRequest().authenticated();

        http.csrf().disable();
    }
}
