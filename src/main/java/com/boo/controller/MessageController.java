package com.boo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author song
 * @date 2022/4/25 10:30
 */
@Controller
@Slf4j
public class MessageController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @RequestMapping("/nor")
    @ResponseBody
    public void nor1() {
        messagingTemplate.convertAndSend("/topic/sub","nor");
    }

    /**
     * @return
     */
    @MessageMapping("/chat")
    public String demo(String message) {
        return "test";
    }

    /**
     * 订阅时的触发
     */
    @SubscribeMapping("/sub")
    public String sub() {
        log.info("用户订阅");
        return "订阅成功";
    }


}
