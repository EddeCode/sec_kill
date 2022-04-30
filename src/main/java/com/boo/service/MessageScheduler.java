package com.boo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author song
 * @date 2022/4/25 11:21
 */
@Component
@EnableScheduling
public class MessageScheduler {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // @Scheduled(fixedRate = 10000L)
    private void timelySend() {
        messagingTemplate.convertAndSend("/topic/sub", LocalDateTime.now());
    }
}
