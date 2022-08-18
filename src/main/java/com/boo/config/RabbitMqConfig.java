package com.boo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * TODO 发布确认 消费者确认
 * 后面可以把消费者模块单独分离出来
 *
 * @author song
 * @date 2022/4/29 21:11
 */
@Configuration
public class RabbitMqConfig {
    /**
     * 通用延迟队列
     */
    public static final String DELAY_QUEUE = "delay.queue";
    public static final String DELAY_SEC_QUEUE = "delay.sec.queue";
    public static final String DELAY_EXCHANGE = "delay.exchange";
    public static final String DELAY_ROUTING_KEY = "delay.routing";
    public static final String DELAY_SEC_ROUTING_KEY = "delay.sec.routing";

    @Bean
    public Queue buyQueue() {
        return QueueBuilder.durable(DELAY_QUEUE).build();
    }

    @Bean
    public Queue secQueue() {
        return QueueBuilder.durable(DELAY_SEC_QUEUE).build();
    }

    @Bean
    public CustomExchange delayExchange() {
        return new CustomExchange(DELAY_EXCHANGE,
                "x-delayed-message",
                true, false,
                Collections.singletonMap("x-delayed-type", "direct"));
    }


    @Bean
    public Binding waitQueueBindingSnapExchange() {
        return BindingBuilder
                .bind(buyQueue())
                .to(delayExchange())
                .with(DELAY_ROUTING_KEY)
                .noargs();
    }

    @Bean
    public Binding secQueueBindingSnapExchange() {
        return BindingBuilder
                .bind(secQueue())
                .to(delayExchange())
                .with(DELAY_SEC_ROUTING_KEY)
                .noargs();
    }

}
