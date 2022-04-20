package com.boo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.lang.reflect.Type;
import java.sql.Timestamp;

/**
 * @author song
 * @date 2022/4/20 11:01
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SubParam {
    @TableId(type = IdType.AUTO)
    Long id;
    Long paramId;
    String param;
    Timestamp createTime;
}
