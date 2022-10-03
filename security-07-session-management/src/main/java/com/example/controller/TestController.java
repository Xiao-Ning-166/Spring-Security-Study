package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaoning
 * @date 2022/10/03
 */
@RestController
public class TestController {

    @GetMapping("/")
    public String test() {
        return "test ok!";
    }

}
