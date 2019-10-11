package com.infosys.ymall.manage;

import com.infosys.ymall.bean.PmsBaseCatalog2;
import com.infosys.ymall.bean.PmsSkuInfo;
import com.infosys.ymall.conf.YmallRedissonConfig;
import com.infosys.ymall.service.CatalogService;
import com.infosys.ymall.service.SkuService;
import com.infosys.ymall.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YamallManageServiceApplicationTests {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    YmallRedissonConfig ymallRedissonConfig;

	@Test
	public void contextLoads() {
        RedissonClient redissonClient = ymallRedissonConfig.redissonClient();
        System.out.println("======"+redissonClient);
    }

}
