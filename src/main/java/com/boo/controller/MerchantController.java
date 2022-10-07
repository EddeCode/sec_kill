package com.boo.controller;


import com.boo.common.ResponseResult;
import com.boo.entity.PdClass;
import com.boo.entity.PdSku;
import com.boo.entity.Product;
import com.boo.service.IPdClassService;
import com.boo.service.IPdSkuService;
import com.boo.service.MerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author song
 * @since 2022-05-07
 */
@RestController
@RequestMapping("/merchant")
@PreAuthorize("hasAuthority('sys.mscan')")
@Tag(name = "MerchantController",description = "店铺管理接口")
public class MerchantController {
    @Autowired
    MerchantService merchantService;

    @Operation(summary = "获取用户对应的店铺下的所有商品")
    @GetMapping("/getMyPds")
    public ResponseResult getMyPds() {
        List<Product> list = merchantService.getMyPds();
        return new ResponseResult<>(200, "ok", list);
    }
    @Operation(summary = "申请新商品")
    @PreAuthorize("hasAuthority('sys.mowner')")
    @PostMapping("/aplNewPd")
    public ResponseResult aplNewPd(@RequestBody Product product) {

        return new ResponseResult<>(200, "apply ok", merchantService.aplNewPd(product));
    }

    @Operation(summary = "修改商品封面")
    @PreAuthorize("hasAuthority('sys.mowner')")
    @PostMapping("/replaceImg")
    public ResponseResult replaceImg(@RequestBody Product product) {
        merchantService.replaceImg(product);
        return new ResponseResult<>(200, "apply ok");
    }

    @Autowired
    IPdSkuService skuService;

    @PreAuthorize("hasAuthority('sys.mowner')")
    @PostMapping("/updateSku")
    @Operation(summary = "更新商品sku")
    public ResponseResult updateSku(@RequestBody PdSku sku) {
        //更新完后同时更新数据库（except stock）以及缓存（stock）
        skuService.updateSecurity(sku);
        return new ResponseResult(200, "ok");
    }

    @Autowired
    IPdClassService pdClassService;

    @PreAuthorize("hasAuthority('sys.mowner')")
    @PostMapping("/addSku")
    @Operation(summary = "新增商品class，tag，sku")
    public ResponseResult addSku(@RequestBody EntireProd entire) {
        /*
         * 防止恶意更改 确保是更改自己的sku
         * 这里获取的cid是未知的，先保存class
         * 对每个保存好的class对tagList加入cid
         */
        pdClassService.saveClassList(entire.getClassList(), entire.getSkuList());
        return new ResponseResult(200, "ok", entire);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class EntireProd {
        private List<PdSku> skuList;
        private List<PdClass> classList;
    }


}
