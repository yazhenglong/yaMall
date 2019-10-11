package com.infosys.ymall.manage.mapper;

import com.infosys.ymall.bean.PmsProductSaleAttr;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsProductSaleAttrMapper extends Mapper<PmsProductSaleAttr>{

    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(@Param("productId") String productId, @Param("skuId") String skuId);

}
