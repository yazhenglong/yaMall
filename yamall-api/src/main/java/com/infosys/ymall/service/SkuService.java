package com.infosys.ymall.service;

import com.infosys.ymall.bean.PmsSkuInfo;

import java.util.List;

public interface SkuService {

    void saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getSkuByIdDb(String skuId);

    PmsSkuInfo getSkuById(String skuId);

    List<PmsSkuInfo> getSkuSaleAttrValueslist(String productId);

    List<PmsSkuInfo> getAllSku(String catalog3Id);
}
