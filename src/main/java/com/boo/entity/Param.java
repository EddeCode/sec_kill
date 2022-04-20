package com.boo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author song
 * @date 2022/4/20 10:43
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Param {
    @TableId(type = IdType.AUTO)
    Long id;
    Long prodId;
    String paramName;
}
