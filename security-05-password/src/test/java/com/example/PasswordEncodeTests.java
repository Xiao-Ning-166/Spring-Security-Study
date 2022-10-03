package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author xiaoning
 * @date 2022/10/03
 */
@SpringBootTest
public class PasswordEncodeTests {

    @Test
    public void testBCryptPasswordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String res = bCryptPasswordEncoder.encode("root");
        System.out.println(res);
    }

}
