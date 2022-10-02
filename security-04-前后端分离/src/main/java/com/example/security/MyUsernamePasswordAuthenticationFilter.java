package com.example.security;

import cn.hutool.core.util.StrUtil;
import com.example.security.exception.VerifyCodeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

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

    public static final String FORM_VERIFY_CODE = "verifyCode";

    public String verifyCodeParameter = FORM_VERIFY_CODE;

    public String getVerifyCodeParameter() {
        return verifyCodeParameter;
    }

    public void setVerifyCodeParameter(String verifyCode) {
        this.verifyCodeParameter = verifyCode;
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
            // 3、检查验证码、提取 用户名、 密码
            try {
                Map<String, String> map = new ObjectMapper().readValue(request.getInputStream(), Map.class);

                // 1、获取请求中的验证码
                String verifyCode = map.get(this.getVerifyCodeParameter());
                // 2、获取正确的验证码
                String realVerifyCode = (String) request.getSession().getAttribute("realVerifyCode");
                // 3、检查验证码是否正确
                if (StrUtil.isBlank(verifyCode) || StrUtil.isEmpty(realVerifyCode) || !verifyCode.equalsIgnoreCase(realVerifyCode)) {
                    throw new VerifyCodeException("验证码不正确！！！");
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
