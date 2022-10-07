package com.boo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boo.entity.Merchant;
import com.boo.entity.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

public interface ProdService extends IService<Product> {
    List<Product> summaryList();

    Integer cacheSecProduct();


    List<Product> getMerchantPds(Merchant merchant);


    boolean getSecFlagByPid(Long pid);

    void setSecTime(SecTimeInfo secTimeInfo);

    String getSecTime(long pid);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class SecTimeInfo {
        private long pid;
        private boolean secFlag;
        private Timestamp startStp;
        private Timestamp endStp;
    }
}
