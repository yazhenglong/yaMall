package com.infosys.ymall.service;

import com.infosys.ymall.bean.PmsProductImage;
import com.infosys.ymall.bean.PmsProductInfo;
import com.infosys.ymall.bean.PmsProductSaleAttr;

import java.util.List;

public interface SpuService {
    List<PmsProductInfo> spuList(String catalog3Id);

    void saveSpuInfo(PmsProductInfo pmsProductInfo);

    List<PmsProductSaleAttr> spuSaleAttrList(String spuId);

    List<PmsProductImage> spuImageList(String spuId);

    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId);



}
