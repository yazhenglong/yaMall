package com.infosys.ymall.service;

import com.infosys.ymall.bean.PmsSearchParam;
import com.infosys.ymall.bean.PmsSearchSkuInfo;

import java.util.List;

public interface SearchService {

    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);

}
