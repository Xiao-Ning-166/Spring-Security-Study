package com.example.security.filter;

import cn.hutool.core.util.StrUtil;
import com.example.security.exception.VerifyCodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义验证码的filter
 *
 * @author xiaoning
 * @date 2022/10/02
 */
@Slf4j
public class VerifyCodeFilter extends UsernamePasswordAuthenticationFilter {

    public static final String FORM_VERIFY_CODE = "verifyCode";

    public String verifyCode = FORM_VERIFY_CODE;

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.warn("======自定义验证码Filter=====");
        // 1、判断请求方式
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("认证请求方式不支持: " + request.getMethod());
        }
        // 2、获取提交的验证码
        String verifyCode = request.getParameter(this.getVerifyCode());
        // 3、获取真实的验证码
        String realVerifyCode = (String) request.getSession().getAttribute("realVerifyCode");
        // 4、判断验证码是否正确
        if (StrUtil.isNotBlank(verifyCode) && StrUtil.isNotBlank(realVerifyCode) && verifyCode.equalsIgnoreCase(realVerifyCode)) {
            // 验证码正确，调用父类方法验证用户名密码
            return super.attemptAuthentication(request, response);
        }
        // 5、验证码不正确，抛出异常
        throw new VerifyCodeException("验证码不正确！！");
    }
}
