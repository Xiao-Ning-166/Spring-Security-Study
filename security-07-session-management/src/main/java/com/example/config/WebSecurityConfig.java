package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义Spring Security配置
 *
 * @author xiaoning
 * @date 2022/10/03
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private FindByIndexNameSessionRepository findByIndexNameSessionRepository;

    @Bean
    public SpringSessionBackedSessionRegistry mySessionRepository() {
        SpringSessionBackedSessionRegistry sessionRegistry = new SpringSessionBackedSessionRegistry(findByIndexNameSessionRepository);
        return sessionRegistry;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin();

        // 开启会话管理
        http.sessionManagement()
            // 设置会话的最大数量。
            .maximumSessions(1)

            /**
             * 单体架构时，会话被挤下线的处理方式。
             * 指定会话被挤下线时的跳转
             */
            // .expiredUrl("/login");

            /**
             * 前后端分离时的处理方式，返回json
             */
            .expiredSessionStrategy(event -> {
                HttpServletResponse response = event.getResponse();
                Map<String, Object> map = new HashMap<>();
                map.put("ststus", 500);
                map.put("messsage", "会话失效，请重新登录");
                String json = new ObjectMapper().writeValueAsString(map);
                // 设置响应类型
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.getWriter().println(json);
            })
            // 达到会话最大值时，禁止继续登录
            .maxSessionsPreventsLogin(true)
            // 将session交给redis共享
            .sessionRegistry(mySessionRepository());

        http.authorizeRequests()
            .anyRequest().authenticated();

        http.csrf().disable();

    }
}
