package com.boo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * TODO
 * 后面可以把消费者模块单独分离出来
 *
 * @author song
 * @date 2022/4/29 21:11
 */
@Configuration
public class RabbitMqConfig {
    public static final String DELAY_QUEUE = "snap.delay.queue";
    public static final String DELAY_EXCHANGE = "snap.delay.exchange";
    public static final String DELAY_ROUTING_KEY = "snap.delay.routing";

    @Bean
    public Queue insertQueue() {
        return QueueBuilder.durable(DELAY_QUEUE).build();
    }

    @Bean
    public CustomExchange snapExchange() {
        return new CustomExchange(DELAY_EXCHANGE,
                "x-delayed-message",
                true, false,
                Collections.singletonMap("x-delayed-type", "direct"));
    }

    @Bean
    public Binding insertQueueBindingSnapExchange() {
        return BindingBuilder
                .bind(insertQueue())
                .to(snapExchange())
                .with(DELAY_ROUTING_KEY)
                .noargs();
    }

}
