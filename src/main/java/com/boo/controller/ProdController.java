package com.boo.controller;

import com.boo.entity.ResponseResult;
import com.boo.service.ProdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author song
 * @date 2022/4/19 15:57
 */
@RestController
public class ProdController {

    @Autowired
    private ProdService prodService;


    @RequestMapping("/prods")
    public ResponseResult getAllProductSummary() {
        return new ResponseResult(200, "ok", prodService.summaryList());
    }

    @RequestMapping("/prods/{id}")
    public ResponseResult getSpecProd(@PathVariable("id") Long id)
    {
        return new ResponseResult(200,"ok",prodService.getParamsMap(id));
    }

    @RequestMapping("/prods/sub/{id}")
    public ResponseResult getSubProdById(@PathVariable("id") Long id)
    {
        return null;
    }


}
