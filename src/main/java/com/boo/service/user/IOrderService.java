package com.boo.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boo.entity.ResponseResult;
import com.boo.entity.prod.Order;
import com.boo.entity.prod.PdSku;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

/**
 * @author song
 */
public interface IOrderService extends IService<Order> {
    /**
     * @param oid 订单id
     * @return 获取订单状态
     */
    String getStatus(Long oid);

    /**
     * @return 获取本次请求对应用户的所有订单
     */
    List<Order> orders();

    /**
     * 增加普通订单
     *
     * @param order 订单
     */
    void addOrdinaryOrder(Order order);

    /**
     * 增加秒杀订单
     *
     * @param order 订单
     */
    void addSecOrder(Order order);

    /**
     * 用户秒杀访问的服务
     *
     * @param order 秒杀订单
     * @param sku   sku
     * @return 消息
     * @throws JsonProcessingException JsonProcessingException
     */
    ResponseResult snapOrder(Order order, PdSku sku) throws JsonProcessingException;

    /**
     * @param oid 订单id
     * @return 获取订单状态
     */
    String getSecStatus(Long oid);

    /**
     * 虚假的支付模拟，后面会删掉
     *
     * @param oid 订单id
     * @throws JsonProcessingException JsonProcessingException
     */
    void simulatePayment(Long oid) throws JsonProcessingException;
}
