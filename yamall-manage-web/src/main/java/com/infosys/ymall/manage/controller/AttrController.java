package com.infosys.ymall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.infosys.ymall.bean.PmsBaseAttrInfo;
import com.infosys.ymall.bean.PmsBaseAttrValue;
import com.infosys.ymall.bean.PmsBaseSaleAttr;
import com.infosys.ymall.service.AttrService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class AttrController {

    @Reference
    AttrService attrService;

    @RequestMapping("attrInfoList")
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id){

        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrService.attrInfoList(catalog3Id);
        return pmsBaseAttrInfos;
    }

    @RequestMapping("saveAttrInfo")
    public String  saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){

        return attrService.saveAttrInfo(pmsBaseAttrInfo);
    }

    @RequestMapping("getAttrValueList")
    public List<PmsBaseAttrValue> AttrInfoList(String attrId){

        List<PmsBaseAttrValue> pmsBaseAttrValues = attrService.getAttrValueList(attrId);
        return pmsBaseAttrValues;
    }



    /*c查询平台销售属性*/
    @RequestMapping("baseSaleAttrList")
    public List<PmsBaseSaleAttr> spuList(String catalog3Id){

        List<PmsBaseSaleAttr> pmsBaseSaleAttrs = attrService.baseSaleAttrList();
        return pmsBaseSaleAttrs;
    }


}
