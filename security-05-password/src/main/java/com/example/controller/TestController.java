package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xiaoning
 * @date 2022/10/03
 */
@Controller
public class TestController {

    @ResponseBody
    @GetMapping("/test")
    public String test() {
        return "test ok!";
    }

}
