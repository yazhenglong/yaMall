package com.infosys.ymall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.infosys.ymall.bean.PmsBaseCatalog1;
import com.infosys.ymall.bean.PmsBaseCatalog2;
import com.infosys.ymall.bean.PmsBaseCatalog3;
import com.infosys.ymall.service.CatalogService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class CatalogController {

    @Reference
    CatalogService catalogService;

    @RequestMapping("getCatalog3")
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id){

        List<PmsBaseCatalog3> pmsBaseCatalog3s = catalogService.getCatalog3(catalog2Id);
        return pmsBaseCatalog3s;
    }

    @RequestMapping("getCatalog2")
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id){

        List<PmsBaseCatalog2> pmsBaseCatalog2s = catalogService.getCatalog2(catalog1Id);
        return pmsBaseCatalog2s;
    }

    @RequestMapping("getCatalog1")
    public List<PmsBaseCatalog1> getCatalog1(){

        List<PmsBaseCatalog1> pmsBaseCatalogs = catalogService.getCatalog1();
        return pmsBaseCatalogs;
    }

}
