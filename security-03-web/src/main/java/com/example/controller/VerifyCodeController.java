package com.example.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 验证码控制器
 *
 * @author xiaoning
 * @date 2022/10/02
 */
@Controller
public class VerifyCodeController {

    @GetMapping("/verifyCode.jpg")
    public void getVerifyCodeImage(HttpServletResponse response, HttpSession session) {

        try {
            // 定义图形验证码的长、宽、验证码字符数、干扰线宽度
            ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200, 100, 4, 4);
            // 真实的验证码
            String realCode = captcha.getCode();
            session.setAttribute("realVerifyCode", realCode);
            // 将验证码图片输入到浏览器
            captcha.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
