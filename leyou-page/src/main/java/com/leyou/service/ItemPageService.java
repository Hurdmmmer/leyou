package com.leyou.service;

import com.leyou.client.BrandClient;
import com.leyou.client.CategoryClient;
import com.leyou.client.GoodsClient;
import com.leyou.client.SpecificatonClient;
import com.leyou.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于后台查询数据, 填充到商品详情页面
 * @author shen youjian
 * @date 2018/7/29 19:23
 */
@Service
public class ItemPageService {
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpecificatonClient specificatonClient;

    public Map<String, Object> builderGoods(Long spuId) {
        Map<String, Object> result = new HashMap<>();
        //1. 商品的分类信息
        //1.1 查询 spu 数据, 获取分类的信息
        Spu spu = goodsClient.querySpuBySpuId(spuId);
        // 1.2 查询所有的分类信息
        List<Category> categories = categoryClient.queryCategoryByCids(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //2. 商品的品牌信息
        // 2.1 查询商品的品牌
        Brand brand = brandClient.queryBrandByBid(spu.getBrandId());
        //3. 商品的标题在 spu 中
        //4. 商品的 sku 信息
        List<Sku> skus = goodsClient.querySkusById(spuId);
        // 4.1 查询库存并封装到 sku 中
        for (Sku sku : skus) {
            Stock stock = goodsClient.queryStockBySkuId(sku.getId());
            sku.setStock(stock.getStock());
        }
        //5. 商品的详情
        SpuDetail spuDetail = goodsClient.querySpuDetailById(spuId);
        // 把查询的数据封装到 spu 对象中
        spu.setSkus(skus);
        spu.setSpuDetail(spuDetail);
        //6. 商品的规格
        // 6.1 查询分类下的所有组数据
        List<SpecGroup> specGroups = specificatonClient.queryGroupsByCid(spu.getCid3());
        // 6.2 根据组 id 查询该组下所有的相信规格 key
        for (SpecGroup specGroup : specGroups) {
            List<SpecParam> specParams = goodsClient.querySpecParamByWhere(specGroup.getId(), spu.getCid3(), null, null);
            specGroup.setSpecParamList(specParams);
        }

        // 7 封装数据
        result.put("spu", spu);
        result.put("specs", specGroups);
        result.put("categories", categories);
        result.put("brand", brand);
        return result;
    }



}
