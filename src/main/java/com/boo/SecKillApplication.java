package com.boo;

import lombok.extern.slf4j.Slf4j;
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
@MapperScan("com.boo.*.mapper")
@Slf4j
public class SecKillApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SecKillApplication.class, args);
        log.info("Run Successful");
        String port = run.getEnvironment().getProperty("server.port");
        log.info("swagger : http://localhost:{}/swagger-ui/index.html",port);
        log.info("目前改造计划");
        log.info("1.保证数据安全、数据一致性");
        log.info("2.添加购物车功能");
        log.info("3.添加商品搜索功能");
        
    }

}
