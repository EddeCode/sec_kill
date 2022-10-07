package com.boo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * 购物车实体
 * @author song
 * @date 2022/8/19 17:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 添加的数量
     */
    private Integer num;
    /**
     * 添加时间
     */
    private Timestamp addTime;
    /**
     * sku 序列化
     */
    private String skuSerialized;
    /**
     * 是否已经清空
     */
    private boolean clear;
    /**
     * 是否已经失效(商品不存在，商品下架)
     */
    private boolean valid;
}
