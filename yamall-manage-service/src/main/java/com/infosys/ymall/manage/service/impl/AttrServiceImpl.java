package com.infosys.ymall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.infosys.ymall.bean.PmsBaseAttrInfo;
import com.infosys.ymall.bean.PmsBaseAttrValue;
import com.infosys.ymall.bean.PmsBaseSaleAttr;
import com.infosys.ymall.manage.mapper.PmsBaseAttrInfoMapper;
import com.infosys.ymall.manage.mapper.PmsBaseAttrValueMapper;
import com.infosys.ymall.manage.mapper.PmsBaseSaleAttrMapper;
import com.infosys.ymall.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;
import java.util.List;
import java.util.Set;

@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Autowired
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;

    @Override
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);
        for (PmsBaseAttrInfo baseAttrInfo : pmsBaseAttrInfos) {
            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setAttrId(baseAttrInfo.getId());
            List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
            baseAttrInfo.setAttrValueList(pmsBaseAttrValues);
        }

        return pmsBaseAttrInfos;
    }

    @Override
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {

        String id = pmsBaseAttrInfo.getId();
        if (StringUtils.isEmpty(id)) {
            //保存属性
            pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);

            //保存属性值
            List<PmsBaseAttrValue> attrValues = pmsBaseAttrInfo.getAttrValueList();
            for (PmsBaseAttrValue pmsBaseAttrValue : attrValues) {
                pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
            }

        } else {
            Example example = new Example(PmsBaseAttrValue.class);
            example.createCriteria().andEqualTo("id",pmsBaseAttrInfo.getId());
            pmsBaseAttrInfoMapper.updateByExampleSelective(pmsBaseAttrInfo,example);  //修改平台属性


            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
            pmsBaseAttrValueMapper.delete(pmsBaseAttrValue);     //先删除掉所有

            //再将所有值插入
            for(PmsBaseAttrValue pmsBaseAttrValue1 : pmsBaseAttrInfo.getAttrValueList()){
                pmsBaseAttrValue1.setAttrId(id);
                pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue1);
            }

        }

        return "success";
    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        Example example = new Example(PmsBaseAttrValue.class);
        example.createCriteria().andEqualTo("attrId",attrId);
        return pmsBaseAttrValueMapper.selectByExample(example);
    }

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {

        return pmsBaseSaleAttrMapper.selectAll();
    }

    /**
     * 根据平台属性值id获取平台属性和平台属性值。
     * */
    @Override
    public List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> valueIdSet) {
        String valueIdStr = StringUtils.join(valueIdSet, ",");

        if (StringUtils.isNotBlank(valueIdStr)){
            List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.selectBaseAttrInfoByValueId(valueIdStr);
            return pmsBaseAttrInfos;
        }

        return  null;
    }


}
