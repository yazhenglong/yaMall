package com.infosys.ymall.manage.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.infosys.ymall.bean.PmsSkuAttrValue;
import com.infosys.ymall.bean.PmsSkuImage;
import com.infosys.ymall.bean.PmsSkuInfo;
import com.infosys.ymall.bean.PmsSkuSaleAttrValue;
import com.infosys.ymall.manage.mapper.PmsSkuAttrValueMapper;
import com.infosys.ymall.manage.mapper.PmsSkuImageMapper;
import com.infosys.ymall.manage.mapper.PmsSkuInfoMapper;
import com.infosys.ymall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.infosys.ymall.service.SkuService;
import com.infosys.ymall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.UUID;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;



    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {

        pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
        String skuId = pmsSkuInfo.getId();

        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
            pmsSkuAttrValue.setSkuId(skuId);
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }

        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
            pmsSkuSaleAttrValue.setSkuId(skuId);
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        }

        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : skuImageList) {
            pmsSkuImage.setSkuId(skuId);
            pmsSkuImage.setProductImgId(pmsSkuImage.getSpuImgId());
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }

    }


    public PmsSkuInfo getSkuByIdDb(String skuId) {

        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        PmsSkuInfo pmsSkuInfo1 = pmsSkuInfoMapper.selectOne(pmsSkuInfo);

        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        List<PmsSkuImage> pmsSkuImages = pmsSkuImageMapper.select(pmsSkuImage);
        pmsSkuInfo1.setSkuImageList(pmsSkuImages);


        return pmsSkuInfo1;
    }


    @Override
    public PmsSkuInfo getSkuById(String skuId) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();

        //链接缓存
        Jedis jedis = redisUtil.getJedis();

        //查询缓存
        String skuKey = "sku:"+skuId+":info";
        String skuJson = jedis.get(skuKey);


        if (StringUtils.isNotBlank(skuJson)){  //if(skuJson != null && skuJson.equals(""))
            System.out.println(Thread.currentThread().getName()+"=====从缓存中获取数据！========");
            pmsSkuInfo = JSON.parseObject(skuJson, PmsSkuInfo.class);
        }else{
            //如果缓存中没有，查询数据库

            //设置分布式锁
            String token = UUID.randomUUID().toString();
            String OK = jedis.set("sku:" + skuId + ":lock", token, "nx", "px", 10 * 1000);
            System.out.println(Thread.currentThread().getName()+"======"+OK+"======");

            if (StringUtils.isNotBlank(OK) && OK.equals("OK")){
                System.out.println(Thread.currentThread().getName()+"查询数据库！============");
                pmsSkuInfo = getSkuByIdDb(skuId);

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //数据库查询结果存入redis
                if(pmsSkuInfo != null){
                    jedis.set(skuKey,JSON.toJSONString(pmsSkuInfo));
                }else{
                    jedis.setex(skuKey,60*3,JSON.toJSONString(""));
                }

                //释放锁
                String lockToken = jedis.get("sku:" + skuId + ":lock");
                if (StringUtils.isNotBlank(lockToken) && lockToken.equals(token)){
                    System.out.println("=============释放锁=========");
                    jedis.del("sku:"+skuId+":lock");
                }


            }else {
                System.out.println(Thread.currentThread().getName()+"======没有拿到锁，开始自旋===========！");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return  getSkuById(skuId);
            }




        }

        jedis.close();

        return pmsSkuInfo;
    }

    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueslist(String productId) {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectSkuSaleAttrValueslist(productId);

        return pmsSkuInfos;
    }

    @Override
    public List<PmsSkuInfo> getAllSku(String catalog3Id) {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectAll();

        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            String skuId = pmsSkuInfo.getId();

            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(skuId);
            List<PmsSkuAttrValue> pmsSkuAttrValues = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);

            pmsSkuInfo.setSkuAttrValueList(pmsSkuAttrValues);
        }

        return pmsSkuInfos;
    }


}
