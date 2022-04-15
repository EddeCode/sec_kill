package com.boo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author song
 * @Date 2022/4/10 15:17
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("mall_user")
public class UserInfo {
    @TableId(type=IdType.AUTO)
    private Long id;
    private String name;
    private String pass;
    private String email;
    @TableField(exist = false)
    private String role = "common";

}
