package com.infosys.ymall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.infosys.ymall.bean.PmsProductImage;
import com.infosys.ymall.bean.PmsProductInfo;
import com.infosys.ymall.bean.PmsProductSaleAttr;
import com.infosys.ymall.service.SpuService;
import com.infosys.ymall.utils.PmsUploadUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
public class SpuCotroller {

    @Reference
    SpuService spuService;

    @RequestMapping("spuList")
    public List<PmsProductInfo> spuList(String catalog3Id){

        List<PmsProductInfo> pmsProductInfos = spuService.spuList(catalog3Id);
        return pmsProductInfos;
    }

    /**
     * 保存spu信息
     * @param pmsProductInfo
     * @return
     */
    @RequestMapping("saveSpuInfo")
    public String spuList(@RequestBody PmsProductInfo pmsProductInfo){

        spuService.saveSpuInfo(pmsProductInfo);
        return "success";
    }


    /**
     * 上传图片
     * @param multipartFile
     * @return
     */
    @RequestMapping("fileUpload")
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile){

        String imageUrl = PmsUploadUtil.uploadImage(multipartFile);
        ;
        return imageUrl;
    }

    @RequestMapping("spuSaleAttrList")
    public List<PmsProductSaleAttr>  spuSaleAttrList(String spuId) {

        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrList(spuId);
        return pmsProductSaleAttrs;
    }


    @RequestMapping("spuImageList")
    public List<PmsProductImage> spuImageList(String spuId){

        List<PmsProductImage> pmsProductImages = spuService.spuImageList(spuId);
        return pmsProductImages;
    }

}
