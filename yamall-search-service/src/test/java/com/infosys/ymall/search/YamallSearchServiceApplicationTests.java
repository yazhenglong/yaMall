package com.infosys.ymall.search;

import com.alibaba.dubbo.config.annotation.Reference;
import com.infosys.ymall.bean.PmsSearchSkuInfo;
import com.infosys.ymall.bean.PmsSkuInfo;
import com.infosys.ymall.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YamallSearchServiceApplicationTests {

	@Reference
	SkuService skuService;

	@Autowired
	JestClient jestClient;

    @Test
    public void contextLoads1() throws IOException {

        //jest的dsl工具
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        //filter
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId",142);
        TermQueryBuilder termQueryBuilder1 = new TermQueryBuilder("skuAttrValueList.valueId",138);
        //TermsQueryBuilder termsQueryBuilder = new TermsQueryBuilder(null,"");

        boolQueryBuilder.filter(termQueryBuilder).filter(termQueryBuilder1);

        //must
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName","乐视");

        boolQueryBuilder.must(matchQueryBuilder);

        //query
        searchSourceBuilder.query(boolQueryBuilder);

        //from
        searchSourceBuilder.from(0);

        //size
        searchSourceBuilder.size(20);

        //higlight
        searchSourceBuilder.highlight(null);

        String dslStr = searchSourceBuilder.toString();

        System.err.println(dslStr);

        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();

        Search search = new Search.Builder(dslStr).addIndex("yamall").addType("PmsSkuInfo").build();
        SearchResult execute = jestClient.execute(search);

        List<SearchResult.Hit<PmsSearchSkuInfo,Void>> hits = execute.getHits(PmsSearchSkuInfo.class);

        for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
            PmsSearchSkuInfo source = hit.source;

            pmsSearchSkuInfos.add(source);

        }

        System.out.println(pmsSearchSkuInfos.size());
    }

    public void put() throws IOException {

        List<PmsSkuInfo> pmsSkuInfoLists = new ArrayList<>();
        pmsSkuInfoLists = skuService.getAllSku(null);

        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();

        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfoLists) {
            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();
            BeanUtils.copyProperties(pmsSkuInfo,pmsSearchSkuInfo);

            pmsSearchSkuInfos.add(pmsSearchSkuInfo);
        }

        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
            Index put = new Index.Builder(pmsSearchSkuInfo).index("yamall").type("PmsSkuInfo").id(pmsSearchSkuInfo.getId()).build();

            jestClient.execute(put);
        }
    }


    @Test
	public void contextLoads(){

        try {
            put();
        } catch (IOException e) {
           System.out.println("es导入数据失败:"+e.getMessage());
        }

    }

}
