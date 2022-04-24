package com.boo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author song
 * @date 2022/4/18 11:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_prod")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {
    @TableId(type = IdType.AUTO)
    Long id;
    String prodName;
    Boolean status;
    Boolean secFlag;
    Timestamp createTime;
    Double price;
    String img;
    Long stock;
}
