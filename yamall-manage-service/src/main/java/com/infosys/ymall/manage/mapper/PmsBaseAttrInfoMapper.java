package com.infosys.ymall.manage.mapper;

import com.infosys.ymall.bean.PmsBaseAttrInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsBaseAttrInfoMapper extends Mapper<PmsBaseAttrInfo>{
    List<PmsBaseAttrInfo> selectBaseAttrInfoByValueId(@Param("valueIdStr") String valueIdStr);
}
