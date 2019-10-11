package com.infosys.ymall.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.infosys.ymall.bean.PmsBaseAttrInfo;
import com.infosys.ymall.bean.PmsSearchParam;
import com.infosys.ymall.bean.PmsSearchSkuInfo;
import com.infosys.ymall.bean.PmsSkuAttrValue;
import com.infosys.ymall.service.AttrService;
import com.infosys.ymall.service.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class SearchController {


    @Reference
    SearchService searchService;

    @Reference
    AttrService attrService;


    @RequestMapping("list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap modelMap){
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = searchService.list(pmsSearchParam);

        modelMap.put("skuLsInfoList",pmsSearchSkuInfos);

        //抽取检索结果所包含的平台属性（去重）
        Set<String> valueIdSet = new HashSet<>();
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                valueIdSet.add(pmsSkuAttrValue.getValueId());
            }

        }

        List<PmsBaseAttrInfo>  pmsBaseAttrInfos = attrService.getAttrValueListByValueId(valueIdSet);


        if (pmsBaseAttrInfos != null){
            modelMap.put("attrList",pmsBaseAttrInfos);
        }


        return "list";
    }

    @RequestMapping("index.html")
    public String index(){
        return "index";
    }
}
