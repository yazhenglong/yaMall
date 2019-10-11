package com.infosys.ymall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.infosys.ymall.bean.PmsProductImage;
import com.infosys.ymall.bean.PmsProductInfo;
import com.infosys.ymall.bean.PmsProductSaleAttr;
import com.infosys.ymall.bean.PmsProductSaleAttrValue;
import com.infosys.ymall.manage.mapper.PmsProductImageMapper;
import com.infosys.ymall.manage.mapper.PmsProductInfoMapper;
import com.infosys.ymall.manage.mapper.PmsProductSaleAttrMapper;
import com.infosys.ymall.manage.mapper.PmsProductSaleAttrValueMapper;
import com.infosys.ymall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SpuServiceImpl implements SpuService{

    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;

    @Autowired
    PmsProductImageMapper pmsProductImageMapper;

    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;


    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {

        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);

        return pmsProductInfoMapper.select(pmsProductInfo);
    }

    @Override
    public void saveSpuInfo(PmsProductInfo pmsProductInfo) {

        pmsProductInfoMapper.insertSelective(pmsProductInfo);

        List<PmsProductImage> spuImageList = pmsProductInfo.getSpuImageList();
        for (PmsProductImage pmsProductImage : spuImageList) {
            pmsProductImage.setProductId(pmsProductInfo.getId());
            pmsProductImageMapper.insertSelective(pmsProductImage);
        }

        List<PmsProductSaleAttr> spuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        for (PmsProductSaleAttr pmsProductSaleAttr : spuSaleAttrList) {
            pmsProductSaleAttr.setProductId(pmsProductInfo.getId());
            pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttr);

            List<PmsProductSaleAttrValue> spuSaleAttrValueList = pmsProductSaleAttr.getSpuSaleAttrValueList();
            for (PmsProductSaleAttrValue pmsProductSaleAttrValue : spuSaleAttrValueList) {
                pmsProductSaleAttrValue.setProductId(pmsProductSaleAttr.getProductId());
                pmsProductSaleAttrValueMapper.insertSelective(pmsProductSaleAttrValue);
            }

        }



    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(spuId);
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);

        for (PmsProductSaleAttr productSaleAttr : pmsProductSaleAttrs) {
            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
            pmsProductSaleAttrValue.setProductId(productSaleAttr.getProductId());
            pmsProductSaleAttrValue.setSaleAttrId(productSaleAttr.getSaleAttrId());
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
            productSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValues);
        }

        return pmsProductSaleAttrs;
    }

    @Override
    public List<PmsProductImage> spuImageList(String spuId) {
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(spuId);
        return pmsProductImageMapper.select(pmsProductImage);
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId) {
        /*PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(productId);
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);

        for (PmsProductSaleAttr productSaleAttr : pmsProductSaleAttrs) {
            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
            pmsProductSaleAttrValue.setProductId(productId);
            pmsProductSaleAttrValue.setSaleAttrId(productSaleAttr.getSaleAttrId());
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValueList = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);

            productSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValueList);
        }*/

        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.spuSaleAttrListCheckBySku(productId,skuId);

        return pmsProductSaleAttrs;
    }
}
