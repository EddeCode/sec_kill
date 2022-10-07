package com.boo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boo.entity.Order;
import com.boo.entity.PdSku;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author song
 * @since 2022-05-07
 */
public interface IPdSkuService extends IService<PdSku> {

    List<PdSku> getSkuByPid(Long id);

    void reduceStock(Order order);

    void revertStock(Order order);

    boolean getModifiable(Long pid);

    int getSecStock(String pid, String mask);

    void updateSecurity(PdSku sku);

    boolean getSecFlagByPid(long pid);

    boolean checkEditorByPid(Long pid);
}
