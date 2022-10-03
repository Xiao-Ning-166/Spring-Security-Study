package com.example.controller;

import com.example.entity.SysUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xiaoning
 * @date 2022/10/02
 */
@Controller
public class UserController {

    @ResponseBody
    @GetMapping("/info")
    public String getUserInfo() {
        // 1、获取 authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 2、通过 authentication 获取用户信息
        SysUser user = (SysUser) authentication.getPrincipal();
        System.out.println("username ==> " + user.getUsername());
        System.out.println("authorities ==> " + user.getAuthorities());

        return "Hello " + user.getUsername() + " ！";
    }

}
