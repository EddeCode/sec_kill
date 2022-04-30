package com.boo.config;

import com.boo.entity.user.LoginUserDetails;
import com.boo.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        /**
         * 不是url，而是主题，是websocket连接成功后进行订阅
         */
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        /**
         * 如果代码中添加了withSockJS()如下，服务器也会自动降级为轮询。
         * path为访问前缀
         * EndPoint为访问点，是基于ws真实映射到url上的
         */
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*");
    }


    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @SneakyThrows
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    //1
                    MessageHeaders messageHeaders = message.getHeaders();
                    log.info("{}",messageHeaders);
                    Map<String,Object> nativeHeaders = messageHeaders.get("nativeHeaders", Map.class);
                    List<String> token = (List<String>) nativeHeaders.get("token");
                    String jwtToken = token.get(0);
                    log.info("token:{}",jwtToken);
                    if (!StringUtils.hasText(jwtToken)) {
                        log.info("no token");
                        return message;
                    }
                    log.info("jwtToken:{}",jwtToken);
                    //2
                    String s = null;
                    try {
                        Claims claims = JwtUtil.parseJWT(jwtToken);
                        s = claims.getSubject();
                    } catch (ExpiredJwtException e) {
                        // e.printStackTrace();
                        log.warn("JWT ExpiredJwtException");
                    }
                    //4 从redis中获取信息
                    String userSerialization = redisTemplate.opsForValue().get("login:" + s);
                    //如果redis中不存在 ...
                    if (!StringUtils.hasText(userSerialization)) {
                        log.info("user isn't login");
                    }
                    LoginUserDetails userDetails = objectMapper.readValue(userSerialization,
                            LoginUserDetails.class);

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null,
                                    userDetails.getAuthorities());
                    Authentication user = authenticationToken; // access authentication header(s)
                    accessor.setUser(user);
                    log.info("授权成功");
                }
                return message;
            }
        });
    }

}