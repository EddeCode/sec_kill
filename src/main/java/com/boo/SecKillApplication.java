package com.boo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @author 宋
 */
@ServletComponentScan
@SpringBootApplication
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
@MapperScan("com.boo.mapper")
public class SecKillApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SecKillApplication.class, args);
        System.out.println("Run Successful");
    }

}
