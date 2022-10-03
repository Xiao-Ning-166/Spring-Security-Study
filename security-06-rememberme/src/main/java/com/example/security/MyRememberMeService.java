package com.example.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义记住我的实现类
 *
 * @author xiaoning
 * @date 2022/10/03
 */
public class MyRememberMeService extends PersistentTokenBasedRememberMeServices {

    public MyRememberMeService(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
    }

    /**
     * 基于前后端分离的记住我功能实现
     */
    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        // 从request作用域中获取参数
        String paramValue = request.getAttribute(parameter).toString();
        if (paramValue != null) {
            if (paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("on")
                    || paramValue.equalsIgnoreCase("yes") || paramValue.equals("1")) {
                return true;
            }
        }

        return false;
    }
}
