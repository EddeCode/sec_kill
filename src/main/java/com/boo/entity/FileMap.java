package com.boo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author song
 * @date 2022/5/13 20:43
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("sys_file_map")
public class FileMap {
    private Long uid;
    private String fileSeq;
    private String extension;
    private Boolean status = true;
}
