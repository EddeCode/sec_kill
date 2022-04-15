package com.boo;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author song
 * @Date 2022/4/11 21:25
 * @Description
 */
public class WithoutSpringBootTests {
    @Test
    void tt()
    {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("123456"));
    }
}
