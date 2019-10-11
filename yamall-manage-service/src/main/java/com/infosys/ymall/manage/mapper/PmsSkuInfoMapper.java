package com.infosys.ymall.manage.mapper;

import com.infosys.ymall.bean.PmsSkuInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsSkuInfoMapper extends Mapper<PmsSkuInfo>{
    List<PmsSkuInfo> selectSkuSaleAttrValueslist(@Param("productId") String productId);
}
