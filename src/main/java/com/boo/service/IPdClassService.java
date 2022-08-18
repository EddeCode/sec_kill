package com.boo.service;

import com.boo.entity.prod.PdClass;
import com.baomidou.mybatisplus.extension.service.IService;
import com.boo.entity.prod.PdSku;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author song
 * @since 2022-05-07
 */
public interface IPdClassService extends IService<PdClass> {

    List<PdClass> getClassListByPid(Long id);

    void saveClassList(List<PdClass> classList, List<PdSku> skuList);
}
