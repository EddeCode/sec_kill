package com.boo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

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
    Boolean status;
    String prodName;
    List<Map<String,List<String>>> params;
    @TableField(exist = false)
    String img;
    Timestamp createTime;

}
