package com.infosys.ymall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.infosys.ymall.bean.PmsSkuInfo;
import com.infosys.ymall.service.SkuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
public class SkuController {

    @Reference
    SkuService skuService;

    @RequestMapping("saveSkuInfo")
    public String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo){

        pmsSkuInfo.setProductId(pmsSkuInfo.getSpuId());
        String skuDefaultImg = pmsSkuInfo.getSkuDefaultImg();
        if(StringUtils.isBlank(skuDefaultImg)){
            skuDefaultImg = pmsSkuInfo.getSkuImageList().get(0).getImgUrl();
        }

        skuService.saveSkuInfo(pmsSkuInfo);
        return "success";
    }


}
