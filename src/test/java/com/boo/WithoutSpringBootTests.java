package com.boo;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

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

    @Test
    void strSql() throws FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            stringBuilder.append("INSERT INTO sys_user (user_name, PASSWORD, email) VALUE ('user"+i+"'," +
                    "'$2a$10$hvrXH3RbRl17.eqvbVnh7uBgw0pHfwIotYVUFMC7yUrMyxKcedBee', '"+i+"@ss.com');\n") ;
        }
        PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(//FileOutputStream设置true为追加
                        new FileOutputStream("batch.sql"), StandardCharsets.UTF_8)
                , true);//autoflush
        writer.println(stringBuilder);
    }

}
