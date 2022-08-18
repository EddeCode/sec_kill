package com.boo.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boo.entity.prod.PdClass;
import com.boo.entity.prod.PdSku;
import com.boo.entity.prod.PdTag;
import com.boo.entity.prod.Product;
import com.boo.mapper.PdClassMapper;
import com.boo.service.IPdClassService;
import com.boo.service.IPdSkuService;
import com.boo.service.IPdTagService;
import com.boo.service.ProdService;
import com.boo.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-05-07
 */
@Service
public class PdClassServiceImpl extends ServiceImpl<PdClassMapper, PdClass> implements IPdClassService {

    @Autowired
    UserService userService;

    @Autowired
    IPdTagService pdTagService;

    @Override
    public List<PdClass> getClassListByPid(Long pid) {
        /**
         * 查询一次即可遍历出PdClass以及PdClass.pdTagList
         */
        List<Map<String, Object>> list = baseMapper.getClassJoinTagByPid(pid);
        List<PdClass> pdClassList = new LinkedList<>();
        Map<String, PdClass> map = new TreeMap<>();
        //筛选出
        list.forEach(temp -> {
            /*
            1 假如不存在则创建
            2 放入tagList中
             */
            if (!map.containsKey(temp.get("cid").toString())) {
                map.put(temp.get("cid").toString(),
                        new PdClass(
                                Long.parseLong(temp.get("cid").toString()),
                                Long.parseLong(temp.get("pid").toString()),
                                Integer.parseInt(temp.get("mask_sec").toString()),
                                temp.get("class_name").toString(),
                                new LinkedList<PdTag>())
                );
            }
            map.get(temp.get("cid").toString()).getPdTagList()
                    .add(new PdTag(
                            Long.parseLong(temp.get("cid").toString()),
                            Integer.parseInt(temp.get("tag_sec").toString()),
                            temp.get("content").toString()
                    ));

        });
        map.entrySet().forEach(
                e -> {
                    pdClassList.add(e.getValue());
                }
        );
        return pdClassList;
    }

    @Autowired
    IPdSkuService skuService;
    @Autowired
    ProdService prodService;
    @Autowired
    IPdSkuService pdSkuService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveClassList(List<PdClass> classList, List<PdSku> skuList) {
        /*
        保证修改的是自己店铺的
         */
        long tempPid = classList.get(0).getPid();
        boolean self = pdSkuService.checkEditorByPid(tempPid);
        boolean modifiable = pdSkuService.getModifiable(classList.get(0).getPid());
        if (!modifiable || !self) {
            return;
        }
        /*
         TODO 用假数据尝试事务
         1  保证都成功或者都失败
         */
        this.saveBatch(classList);
        for (PdClass pdClass : classList) {
            //检验每个class里的pid是否相等 不相等回退
            if (pdClass.getPid() != tempPid) {
                throw new RuntimeException("非法修改");
            }
            List<PdTag> tagList = pdClass.getPdTagList();
            for (PdTag tag : tagList) {
                tag.setCid(pdClass.getCid());
            }
            pdTagService.saveBatch(tagList);
        }
        skuService.saveBatch(skuList);
        LambdaUpdateWrapper<Product> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Product::getStatus, true);
        updateWrapper.eq(Product::getId, classList.get(0).getPid());
        prodService.update(null, updateWrapper);
    }
}
