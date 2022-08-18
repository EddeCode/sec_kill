package com.boo.entity.prod;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.sql.Blob;

import com.baomidou.mybatisplus.annotation.Version;
import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author song
 * @since 2022-05-07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_pd_sku")
public class PdSku implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    private Long pid;

    /**
     * 商品掩码
     */
    private String mask;

    /**
     * 图片
     */
    private String img;

    /**
     * 存货
     */
    @Version
    private Integer stock;

    /**
     * 价格
     */
    private Double price;


}
