package com.boo.entity.prod;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author song
 * @since 2022-05-07
 */
@Getter
@Setter
@TableName("sys_merchant")
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商家id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商家拥有者id
     */
    private Long uid;

    /**
     * 状态 正常1禁用0
     */
    private Boolean status;

    /**
     * 评分
     */
    private Double score;

    /**
     * 商家名
     */
    private String name;


    private Integer ownPhone;

    @TableField(exist = false)
    private String nameOrPhone ;

}
