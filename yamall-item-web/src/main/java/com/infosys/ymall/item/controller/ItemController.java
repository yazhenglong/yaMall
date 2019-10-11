package com.infosys.ymall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.infosys.ymall.bean.PmsProductSaleAttr;
import com.infosys.ymall.bean.PmsSkuInfo;
import com.infosys.ymall.bean.PmsSkuSaleAttrValue;
import com.infosys.ymall.service.SkuService;
import com.infosys.ymall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class ItemController {

    @Reference
    SkuService skuService;

    @Reference
    SpuService spuService;

    /**
     * 安id查找sku
     * @param skuId
     * @param modelMap
     * @return
     */
    @RequestMapping("{skuId}.html")
    public String item(@PathVariable String skuId,ModelMap modelMap) {

        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId);
        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(),skuId);

        modelMap.put("skuInfo",pmsSkuInfo);
        modelMap.put("spuSaleAttrListCheckBySku",pmsProductSaleAttrs);

        HashMap<String, String> skuSaleAttrHash = new HashMap<>();
        List<PmsSkuInfo> pmsSkuInfoList = skuService.getSkuSaleAttrValueslist(pmsSkuInfo.getProductId());

        for (PmsSkuInfo skuInfo : pmsSkuInfoList) {
            String k = "";
            String v = skuInfo.getId();

            List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();

            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : pmsSkuSaleAttrValueList) {
                k += pmsSkuSaleAttrValue.getSaleAttrValueId()+"|";
            }
            skuSaleAttrHash.put(k,v);
        }

        String skuSaleAttrStr = JSON.toJSONString(skuSaleAttrHash);

        modelMap.put("valuesSku",skuSaleAttrStr);
        return "item";
    }

    @RequestMapping("index")
    public String index(ModelMap modelMap) {

        List<String> list = new ArrayList<>();
        for(int i=0;i<5;i++){
            list.add("循环五次："+1);
        }

        modelMap.put("check",1);
        modelMap.put("list",list);
        modelMap.put("hello","hello");
        return "index";
    }

}
