package com.boo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boo.entity.prod.PdSku;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author song
 * @since 2022-05-07
 */
@Mapper
public interface PdSkuMapper extends BaseMapper<PdSku> {
    int getUidByPid(Long pid);
}
