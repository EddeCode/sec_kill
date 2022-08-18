package com.boo;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;


/**
 * mybatis逆向工程
 */
public class CodeGenerator {

    void test() {
        String property = System.getProperty("user.dir");
        System.out.println(property);
    }

    public static void main(String[] args) {
        String userDir = System.getProperty("user.dir");


        FastAutoGenerator
                .create("jdbc:mysql://127.0.0.1:3306/mall",
                        "root", "123456")
                .globalConfig(
                        builder -> {
                            {
                                builder.author("song")
                                        .disableOpenDir()
                                        .outputDir(userDir + "/src/main/java")
                                        .build();
                            }
                        }
                )
                .packageConfig(
                        builder -> {
                            {
                                builder.parent("com.boo")
                                        .entity("entity.prod")
                                        .service("service")
                                        .serviceImpl("service.impl")
                                        .mapper("mapper");
                            }
                        }
                )
                .strategyConfig(
                        builder -> {
                            {
                                builder.enableCapitalMode()
                                        .enableSkipView()
                                        .disableSqlFilter()
                                        .enableCapitalMode()
                                        .addInclude("t_pd_tag")
                                        .addTablePrefix("t_")
                                        .entityBuilder().enableLombok(); // 设置需要生成的表名
                            }
                        }
                ).templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity
                // 引擎模板
                .execute();

    }
}
