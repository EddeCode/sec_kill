package com.boo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 高级的发布确认
 * @author song
 */
@Slf4j
@Component
public class CustomCallBack
        implements RabbitTemplate.ConfirmCallback
        , RabbitTemplate.ReturnsCallback {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @PostConstruct
    private void init() {
        /*
         * true：交换机无法将消息进行路由时，会将该消息返回给生产者
         * false：如果发现消息无法进行路由，则直接丢弃
         */
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * @param correlationData 相关数据
     * @param ack             <b>交换机</b>是否确认收到
     * @param cause           成功:null or 失败原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("id:{};接受成功", correlationData.getId());
        } else {
            log.error("id:{};接受失败，原因为：{}", correlationData.getId(), cause);
        }
    }


    /**
     * <b style="color:blue">消息无法被路由时的返回消息</b>
     *
     * @param returnedMessage returnedMessage
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error("消息：{}，被交换机 {} 退回，原因：{}，路由key：{},code:{}",
                new String(returnedMessage.getMessage().getBody()), returnedMessage.getExchange(),
                returnedMessage.getReplyText(), returnedMessage.getRoutingKey(),
                returnedMessage.getReplyCode());
    }
}