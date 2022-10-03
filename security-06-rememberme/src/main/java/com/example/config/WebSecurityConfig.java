package com.example.config;

import com.example.security.MyRememberMeService;
import com.example.security.MyUsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 自定义前后端分离的Spring Security配置
 *
 * @author xiaoning
 * @date 2022/10/02
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService myUserDetailServiceImpl;

    /**
     * 实例化、配置自定义的 UsernamePasswordAuthenticationFilter
     * @return
     */
    @Bean
    public MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter() throws Exception {
        MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter = new MyUsernamePasswordAuthenticationFilter();
        // 设置自定义的 AuthenticationManager
        myUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        // 设置认证的RememberMeService
        myUsernamePasswordAuthenticationFilter.setRememberMeServices(rememberMeServices());
        // 设置自定义的认证成功处理
        myUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            Map<String, Object> res = new HashMap<>();
            res.put("message", "登录成功！");
            res.put("userInfo", authentication.getPrincipal());
            // 设置响应类型
            response.setContentType("application/json;charset=UTF-8");
            // 设置响应状态码
            response.setStatus(HttpStatus.OK.value());
            String json = new ObjectMapper().writeValueAsString(res);
            response.getWriter().println(json);
        });
        // 设置自定义的认证失败处理
        myUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler((request, response, exception) -> {
            Map<String, Object> res = new HashMap<>();
            res.put("message", "登录失败！");
            res.put("cause", exception.getMessage());
            // 设置响应类型
            response.setContentType("application/json;charset=UTF-8");
            // 设置响应状态码
            response.setStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value() );
            String json = new ObjectMapper().writeValueAsString(res);
            response.getWriter().println(json);
        });

        return myUsernamePasswordAuthenticationFilter;
    }

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
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 实例化自定义的前后端分离的RememberMeService实现类
     *
     * @return
     */
    @Bean
    public RememberMeServices rememberMeServices() {
        MyRememberMeService myRememberMeService = new MyRememberMeService(UUID.randomUUID().toString(), myUserDetailServiceImpl, new InMemoryTokenRepositoryImpl());
        return myRememberMeService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin();

        // 开启记住我功能
        http.rememberMe()
                // 设置登录时的rememberMeService
                .rememberMeServices(rememberMeServices());

        // 异常处理
        http.exceptionHandling()
                // 没有认证就访问受限资源的异常处理
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                        Map<String, Object> res = new HashMap<>();
                        res.put("message", "尚未认证！");
                        res.put("cause", authException.getMessage());
                        // 设置响应类型
                        response.setContentType("application/json;charset=UTF-8");
                        // 设置响应状态码
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        String json = new ObjectMapper().writeValueAsString(res);
                        response.getWriter().println(json);
                    }
                });

        http.logout()
                // 退出登录成功处理
                .logoutSuccessHandler((request, response, authentication) -> {
                    Map<String, Object> res = new HashMap<>();
                    res.put("message", "注销成功！");
                    res.put("userInfo", authentication.getPrincipal());
                    // 设置响应类型
                    response.setContentType("application/json;charset=UTF-8");
                    // 设置响应状态码
                    response.setStatus(HttpStatus.OK.value());
                    String json = new ObjectMapper().writeValueAsString(res);
                    response.getWriter().println(json);
                });

        /**
         * At：替换
         * Before：插入之前
         * After：插入之后
         * 用自定义的UsernamePasswordAuthenticationFilter替换原来的UsernamePasswordAuthenticationFilter
         */
        http.addFilterAt(myUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .anyRequest().authenticated();

        http.csrf().disable();
    }
}
