package com.boo.controller;

import com.boo.entity.ResponseResult;
import com.boo.entity.prod.Order;
import com.boo.entity.prod.PdClass;
import com.boo.entity.prod.PdSku;
import com.boo.entity.prod.Product;
import com.boo.service.IPdClassService;
import com.boo.service.IPdSkuService;
import com.boo.service.ProdService;
import com.boo.service.user.IOrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author song
 * @date 2022/4/19 15:57
 */
@RestController
@Tag(name = "ProdController", description = "商品相关接口")
public class ProdController {

    @Autowired
    private ProdService prodService;

    /**
     * 缓存秒杀商品的sku stock
     *
     * @return ResponseResult
     */
    @PreAuthorize("hasAuthority('sys.update')")
    @GetMapping("/cache")
    @Operation(summary = "缓存所有秒杀商品信息到redis",
            description = "需要有sys.update权限")
    public ResponseResult cacheSecProd() {
        //TODO 目前还没写缓存还原到数据库的代码
        Integer num = prodService.cacheSecProduct();
        return new ResponseResult(200, "ok", num);
    }

    /**
     * @return 查看所有商品
     */
    @Operation(summary = "查看所有商品")
    @GetMapping("/prods")
    public ResponseResult getAllProductSummary() {
        return new ResponseResult(200, "ok", prodService.summaryList());
    }

    @Autowired
    IPdClassService pdClassService;
    @Autowired
    IPdSkuService pdSkuService;

    /**
     * @param id pid
     * @return product  list
     */
    @Operation(summary = "特定商品参数{商品,分类列表}",
            description = "sku列表交给前端再此获取一次，否则响应速度慢")
    @GetMapping("/prod/{id}")
    public ResponseResult getSpecificProd(@PathVariable("id") Long id) {
        //秒杀内容去缓存中找
        HashMap<String, Object> map = new HashMap<>(2);
        Product product = prodService.getById(id);
        map.put("product", product);
        List<PdClass> list = pdClassService.getClassListByPid(id);
        map.put("classList", list);
        return new ResponseResult(200, "ok", map);
    }

    @Operation(summary = "获取商品的sku列表")
    @GetMapping("/prod/sku/{pid}")
    public ResponseResult getSkuList(@PathVariable Long pid) {
        List<PdSku> skuList = pdSkuService.getSkuByPid(pid);
        boolean flag = prodService.getSecFlagByPid(pid);
        if (flag) {
            skuList.forEach(sku -> {
                int secStock = pdSkuService.getSecStock(pid.toString(), sku.getMask());
                sku.setStock(secStock);
            });
        }
        return new ResponseResult(200, skuList);
    }


    @Operation(summary = "获取秒杀商品货存", description = "从redis获取")
    @PostMapping("/prod/getSecStock/")
    public ResponseResult getSecStock(@RequestBody Map<String, Object> map) {
        int stock = pdSkuService.getSecStock((map.get("pid").toString()),
                map.get("mask").toString());
        return new ResponseResult(200, "ok", stock);
    }


    @Resource
    ObjectMapper obM;

    /**
     * 防止秒杀订单被购买
     *
     * @param order order
     * @return 购买普通商品
     * @throws JsonProcessingException JsonProcessingException
     */
    @PostMapping("/prods/creatOrder")
    @Operation(summary = "购买普通商品,创建订单")
    public ResponseResult buySku(@RequestBody Order order) throws JsonProcessingException {
        PdSku sku = obM.readValue(order.getSkuSerialized(), PdSku.class);
        boolean f = prodService.getSecFlagByPid(sku.getPid());
        if (f) {
            return new ResponseResult(201, "此订单现为秒杀订单不可购买");
        }
        orderService.addOrdinaryOrder(order);
        return new ResponseResult(200, "ok", order);
    }

    @Operation(summary = "购买秒杀商品,创建订单")
    @PostMapping("/prods/creatSecOrder")
    public ResponseResult secKillSku(@RequestBody Order order) throws JsonProcessingException {
        String skuSerialized = order.getSkuSerialized();
        if (!StringUtils.hasText(skuSerialized)) {
            prodService.cacheSecProduct();
        }
        PdSku sku = obM.readValue(skuSerialized, PdSku.class);
        boolean f = prodService.getSecFlagByPid(sku.getPid());
        if (!f) {
            return new ResponseResult(201, "此订单非秒杀订单");
        }
        return orderService.snapOrder(order, sku);
    }

    @Autowired
    IOrderService orderService;

    @Operation(summary = "获取普通商品订单状态")
    @GetMapping("/prods/getOrderStatus/{oid}")
    public ResponseResult getOrderStatus(@PathVariable Long oid) {
        return new ResponseResult(200, orderService.getStatus(oid));
    }

    @Operation(summary = "获取秒杀商品订单状态")
    @GetMapping("/prods/getSecOrderStatus/{oid}")
    public ResponseResult getSecOrderStatus(@PathVariable Long oid) {
        return new ResponseResult(200, orderService.getSecStatus(oid));
    }

    @Operation(summary = "获取商品是否可修改", description = "可修改就是没有新建sku")
    @GetMapping("/prods/getModifiable/{pid}")
    public ResponseResult<Boolean> getModifiable(@PathVariable Long pid) {
        return new ResponseResult(200, "ok", pdSkuService.getModifiable(pid));
    }


    @Operation(summary = "虚假支付，后期删掉")
    @GetMapping("/prod/simulatePayment/{oid}")
    public ResponseResult simulatePayment(@PathVariable Long oid) throws JsonProcessingException {
        orderService.simulatePayment(oid);
        return new ResponseResult(200, "ok");
    }

    @Operation(summary = "设置秒杀时间")
    @PostMapping("/prod/setSecTime")
    public ResponseResult setSecTime(@RequestBody ProdService.SecTimeInfo secTimeInfo) {
        prodService.setSecTime(secTimeInfo);
        return new ResponseResult(200, secTimeInfo);
    }

    @Operation(summary = "获取秒杀时间")
    @GetMapping("/prod/secTimeInfo/{pid}")
    public ResponseResult getSecTime(@PathVariable long pid) {
        String secTimeInfo = prodService.getSecTime(pid);
        return new ResponseResult(200, secTimeInfo);
    }


}
