package com.example.security;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 自定义前后端分离的 UsernamePasswordAuthenticationFilter
 *
 * @author xiaoning
 * @date 2022/10/02
 */
@Slf4j
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String DEFAULT_REMEMBER_ME_PARAMETER = "remember-me";

    /**
     * 记住我参数的名字
     */
    public String rememberMeParameter = DEFAULT_REMEMBER_ME_PARAMETER;

    public String getRememberMeParameter() {
        return rememberMeParameter;
    }

    public void setRememberMeParameter(String rememberMeParameter) {
        this.rememberMeParameter = rememberMeParameter;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("=================使用自定义的UsernamePasswordAuthenticationFilter================");
        // 1、判断请求方式是否是POST
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("认证请求方式不支持: " + request.getMethod());
        }
        // 2、判断请求体类型是否是json
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            // 3、提取 用户名、 密码、是否记住我
            try {
                Map<String, String> map = new ObjectMapper().readValue(request.getInputStream(), Map.class);

                // 是否记住我
                String rememberMeParameter = map.get(this.getRememberMeParameter());
                if (!ObjectUtils.isEmpty(rememberMeParameter)) {
                    // 存储到request作用域中
                    request.setAttribute(this.getRememberMeParameter(), rememberMeParameter);
                }

                // 、获取请求中用户名、密码
                String username = map.get(this.getUsernameParameter());
                String password = map.get(this.getPasswordParameter());

                log.info("用户名：{}，密码：{}", username, password);

                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
                // Allow subclasses to set the "details" property
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // 如果请求体不是json格式，则调用父类即本来的 UsernamePasswordAuthenticationFilter 中的方法
        return super.attemptAuthentication(request, response);
    }
}
