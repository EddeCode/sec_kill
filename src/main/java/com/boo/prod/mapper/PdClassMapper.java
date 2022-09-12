package com.boo.prod.mapper;

import com.boo.prod.entity.PdClass;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-05-07
 */
@Mapper
public interface PdClassMapper extends BaseMapper<PdClass> {
    @MapKey(value = "")
    List<Map<String, Object>> getClassJoinTagByPid(Long pid);
}
