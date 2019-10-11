package com.infosys.ymall.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.infosys.ymall.bean.PmsSearchParam;
import com.infosys.ymall.bean.PmsSearchSkuInfo;
import com.infosys.ymall.bean.PmsSkuAttrValue;
import com.infosys.ymall.service.SearchService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService{

    @Autowired
    JestClient jestClient;

    @Override
    public List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam) {
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();

        String dslStr = getSearchDsl(pmsSearchParam);

        System.err.println(dslStr);

        Search search = new Search.Builder(dslStr).addIndex("yamall").addType("PmsSkuInfo").build();

        SearchResult execute = null;

        try {
            execute = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<SearchResult.Hit<PmsSearchSkuInfo,Void>> hits = execute.getHits(PmsSearchSkuInfo.class);

        for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
            PmsSearchSkuInfo source = hit.source;
            String skuName = hit.highlight.get("skuName").get(0);
            source.setSkuName(skuName);
            pmsSearchSkuInfos.add(source);
        }

        return pmsSearchSkuInfos;
    }


    /**
     * es查询的dsl字符串
     * */
    private String getSearchDsl(PmsSearchParam pmsSearchParam) {

        //jest的dsl工具
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        //filter
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        if(StringUtils.isNotBlank(catalog3Id)){
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id",catalog3Id);
            boolQueryBuilder.filter(termQueryBuilder);
        }

        List<PmsSkuAttrValue> skuAttrValueList = pmsSearchParam.getSkuAttrValueList();
        if(skuAttrValueList != null){
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId",pmsSkuAttrValue.getValueId());
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }

        //must
        String keyword = pmsSearchParam.getKeyword();
        if(StringUtils.isNotBlank(keyword)){
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName",keyword);
            boolQueryBuilder.must(matchQueryBuilder);
        }


        //query
        searchSourceBuilder.query(boolQueryBuilder);

        //from
        searchSourceBuilder.from(0);

        //size
        searchSourceBuilder.size(20);

        //higlight
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.field("skuName");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlight(highlightBuilder);


        searchSourceBuilder.sort("id.keyword", SortOrder.DESC);

        return searchSourceBuilder.toString();
    }

}
