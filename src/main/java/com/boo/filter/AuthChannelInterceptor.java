package com.boo.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @author song
 * @date 2022/4/25 18:29
 */
@Deprecated
@Slf4j
// @Component
public class AuthChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        MessageHeaders headers = message.getHeaders();
        Object simpMessageType = headers.get("simpMessageType");
        log.info("{}",simpMessageType);
        if(!Objects.isNull(simpMessageType)){
            return ChannelInterceptor.super.preSend(message, channel);
        }else {
            return null;
        }


    }
}
