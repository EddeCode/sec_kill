package com.boo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.scripting.support.StaticScriptSource;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author song
 * @date 2022/4/20 22:31
 */
@Configuration
public class RootConfig {
    // @Bean
    // public ServerEndpointExporter serverEndpointExporter() {
    //
    //     return new ServerEndpointExporter();
    // }


    @Bean
    public DefaultRedisScript<Long> redisScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("sec.lua")));
        redisScript.setResultType(Long.class);
        return redisScript;
    }
}
