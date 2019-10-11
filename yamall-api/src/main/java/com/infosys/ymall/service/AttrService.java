package com.infosys.ymall.service;

import com.infosys.ymall.bean.PmsBaseAttrInfo;
import com.infosys.ymall.bean.PmsBaseAttrValue;
import com.infosys.ymall.bean.PmsBaseSaleAttr;

import java.util.List;
import java.util.Set;

public interface AttrService {
    List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);

    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseAttrValue> getAttrValueList(String attrId);

    List<PmsBaseSaleAttr> baseSaleAttrList();

    List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> valueIdSet);

}
