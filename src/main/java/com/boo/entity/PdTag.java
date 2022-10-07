package com.boo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

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
@ToString
@TableName("t_pd_tag")
@AllArgsConstructor
@NoArgsConstructor
public class PdTag implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * class id
     */
    private Long cid;

    private Integer tagSec;

    private String content;


}
