package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaoning
 * @date 2022/10/04
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public String testCORS() {

        return "test ok";
    }

}
