package com.example.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author xiaoning
 * @date 2022/10/02
 */
@RestController
public class VerifyCodeController {

    @GetMapping("/verifyCode")
    public String getVerifyCodeImage(HttpSession session) {
        // 定义图形验证码的长、宽、验证码字符数、干扰元素个数
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);
        // 得到正确验证码，存储起来
        String realVerifyCode = captcha.getCode();
        session.setAttribute("realVerifyCode", realVerifyCode);
        // 得到 Base64字符串返回
        String imageBase64 = captcha.getImageBase64Data();

        return imageBase64;
    }

}
