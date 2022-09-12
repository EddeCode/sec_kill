package com.boo.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用作消息队列的数据传输格式
 * 是否有必要，如此设计可以共有消息队列
 * 一个任务一个消息队列就不用规范格式
 * @author song
 * @date 2022/8/18 14:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO<T> {

    /*
    消息类型
     */
    private String type;

    /*
    消息体
     */
    private T body;



}
