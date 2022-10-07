package com.boo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author song
 * @date 2022/4/29 21:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_pd_order")
public class Order {
    @JsonIgnore
    private static ObjectMapper ob = new ObjectMapper();
    /**
     * 订单id
     */
    @TableId(type = IdType.AUTO)
    private Long oid;
    /**
     * 用户id
     */
    private Long uid;
    /**
     * 订单类型
     * 1. normal (default)  默认
     * 2. sec 秒杀订单
     */
    private String type;
    /**
     * 购买数量
     */
    private Integer num;
    /**
     * 是否已支付
     */
    private Integer isPayed = 0;
    /**
     * sku 序列化
     */
    private String skuSerialized;
    /**
     * 购买时间 来自前端
     */
    private Timestamp time;



    public enum Type{

        NORMAL("normal"),SEC("sec");
        private String type;

        Type(String type) {
            this.type = type;
        }
        @Override
        public String toString(){
            return type;
        }
    }


}
