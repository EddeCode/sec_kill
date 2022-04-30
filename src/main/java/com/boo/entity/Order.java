package com.boo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.boo.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author song
 * @date 2022/4/29 21:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    private User user;
    private Long pid;
    private LocalDateTime dateTime;

    public Order(User user, Long pid, LocalDateTime dateTime) {
        this.user=user;
        this.pid=pid;
        this.dateTime=dateTime;
    }
}
