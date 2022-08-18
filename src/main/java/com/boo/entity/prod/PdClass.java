package com.boo.entity.prod;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

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
@TableName("t_pd_class")
@NoArgsConstructor
@AllArgsConstructor
public class PdClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long cid;

    private Long pid;

    private Integer maskSec;

    private String className;

    @TableField(exist = false)
    private List<PdTag> pdTagList;


}
