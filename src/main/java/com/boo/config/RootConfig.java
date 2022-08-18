package com.boo.config;

import com.boo.controller.FileController;
import com.boo.service.ProdService;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author song
 * @date 2022/4/20 22:31
 */
@Configuration
public class RootConfig {


    @Value("${file-storage-directory}")
    String directoryPath;


    @PostConstruct
    public void init() throws IOException {
        String sc = FileController.sc;
        // TODO ${file-storage-directory}/sku-img ${file-storage-directory}/avatar
        String skuDir = directoryPath + sc + "sku-img";
        String avatarDir = directoryPath + sc + "avatar";
        if (!Files.exists(Path.of(directoryPath))) {
            Files.createDirectories(Path.of(directoryPath));
        }
        if (!Files.exists(Path.of(skuDir))) {
            Files.createDirectories(Path.of(skuDir));
        }
        if (!Files.exists(Path.of(avatarDir))) {
            Files.createDirectories(Path.of(avatarDir));
        }
    }

    /**
     * 用来获取content-Type的
     * @return MimeTypes
     */
    @Bean
    public MimeTypes mimeTypes() {
        return MimeTypes.getDefaultMimeTypes();
    }

    @Bean
    public DefaultRedisScript<Long> redisScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("sec.lua")));
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    @Bean
    public Map<String, ProdService.SecTimeInfo> secTimeInfoCache() {
        return new TreeMap<>();
    }
}
