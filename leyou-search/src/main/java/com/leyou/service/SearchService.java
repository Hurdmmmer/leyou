package com.leyou.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.client.BrandClient;
import com.leyou.client.CategoryClient;
import com.leyou.client.GoodsClient;
import com.leyou.common.pojo.PageResult;
import com.leyou.common.utils.JsonUtils;
import com.leyou.dto.SearchRequest;
import com.leyou.dto.SearchResult;
import com.leyou.pojo.*;
import com.leyou.respository.GoodsRepository;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.lucene.util.fst.ListOfOutputs;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shen youjian
 * @date 2018/7/26 14:42
 */
@Service
public class SearchService {
    /**
     * 访问 category 服务客服端
     */
    @Autowired
    private CategoryClient categoryClient;
    /**
     * 访问 brand 服务的客服端
     */
    @Autowired
    private BrandClient brandClient;
    /**
     * 访问商品的服务的客服端
     */
    @Autowired
    private GoodsClient goodsClient;
    /**
     * 对 elasticsearch 简单的crud对象
     */
    @Autowired
    private GoodsRepository goodsRepository;
    /**
     * 用于对elasticsearch聚合查询的模板类
     */
    @Autowired
    private ElasticsearchTemplate template;

    /**
     * 根据 Spu 构建一个 Goods 商品
     */
    public Goods builderGoods(Spu spu) {

        Goods goods = new Goods();
        // 设置商品的 id, 商品 id 就是 spu id
        goods.setId(spu.getId());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());
        // 拼接用于查询搜索的关键字数据
        String all = StringUtils.join(
                categoryClient.queryCategoryNamesByCids(
                        Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3())
                ), "/") +
                "/" + spu.getTitle() + "/"
                + StringUtils.join(
                brandClient.queryBrandByBid(spu.getBrandId()).getName(), "/");

        goods.setAll(all); // 把所有参与搜索的关键字组合成一个字符串进行分词, 用于搜索, 分类名称, 品牌名称, 商品标题
        // 开始设置用于过滤条件的价格
        // 1. 根据 spuId 到sku表中查询所有的价格
        List<Sku> skus = this.goodsClient.querySkusById(spu.getId());
        // 2. 遍历skus 获取所有的价格
        Set<Long> prices = new HashSet<>();  // 不存储相同的价格, 存储到 elasticsearch 中

        // 开始设置用于页面展示的数据
        List<Map<String, Object>> list = new ArrayList<>();
        for (Sku sku : skus) {
            prices.add(sku.getPrice());
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId()); // 用于查询??
            map.put("title", sku.getTitle());
            map.put("price", sku.getPrice());
            map.put("image", StringUtils.isNotBlank(sku.getImages()) ? sku.getImages().split(",")[0] : "");
            list.add(map);
        }
        goods.setPrice(new ArrayList<>(prices)); // 把一个 spu 包含的所有 sku 的价格全部设置到 goods 中 用于搜索中的排序
        goods.setSkus(JsonUtils.serialize(list)); // 把一个 spu 包含的所有 sku商品的信息转换成一个 json 数组, 用于页面展示的数据
        // 根据 spu 中支持搜索的规格字段(通用,特有)为key, 根据每一个字段 id 到每一个商品详情的描述中获取具体的值
        // 根据分类查询支持搜索的规格参数的key
        List<SpecParam> specParams = goodsClient.querySpecParamByWhere(null, spu.getCid3(), true, null);
        // 1. 根据 spuId 查询该商品集中的商品详细描述
        SpuDetail spuDetail = goodsClient.querySpuDetailById(spu.getId());
        // 2. 获取 专有属性值, 该数据为一个 json 格式
        String specialSpec = spuDetail.getSpecialSpec();
        // 3. 获取 公共的属性值, 也是一个 json 数据
        String genericSpec = spuDetail.getGenericSpec();
        // 4. 把 json 字符串装换成对象
        Map<Long, Object> genricMap = JsonUtils.parseMap(genericSpec, Long.class, Object.class);
        // 注意, json 字符串中有数组, 需要使用原生的 Jackson 对象转换成对象
        Map<Long, List<Object>> specialMap = JsonUtils.nativeRead(specialSpec, new TypeReference<Map<Long, List<Object>>>() {
        });
        // 5. 创建一个 map 用于存放规格属性
        Map<String, Object> search = new HashMap<>();
        for (SpecParam specParam : specParams) {
            // 6. 根据具体通用还是特有获取具体的 规格属性值
            Boolean generic = specParam.getGeneric();
            if (generic) {
                Object value = genricMap.get(specParam.getId());
                if (specParam.getNumeric()) {
                    // 如果是数值类型, 就需要把数值转换成一个范围之中, 用于搜索时确定范围,
                    // 不然该就需要 elasticsearch 中使用 range 搜索很麻烦, 我们转换成一个固定的范围的字符串
                    // 就可以在搜索时 使用聚合搜索直接搜索结果
                    value = chooseSegment(value.toString(), specParam);
                }
                search.put(specParam.getName(), value.toString());
            } else {
                // 这里不需要转换成一个范围, 是因为特有属性没有一个数值类型的用于搜索
                search.put(specParam.getName(), specialMap.get(specParam.getId()));
            }
        }
        goods.setSpecs(search);

        return goods;
    }

    /**
     * 根据一个数字类型的字符串, 和它的单位, 返回该商品spu的搜索区间
     */
    private String chooseSegment(String value, SpecParam p) {
        //
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    /**
     * 根据前台页面传递关键字搜索条件查询商品信息
     */
    public SearchResult searchGoodsPage(SearchRequest searchRequest, Integer page, Integer size) {
        SearchResult result = null;
        // 创建一个查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 设置一些排除数据
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"}, null));
        // 设置分页查询
        queryBuilder.withPageable(PageRequest.of(page, size));
        // 设置排序
        if (searchRequest.getNewGoods()) {
            // 如果用户选择了排序那么根据降序排序
            queryBuilder.withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC));
        }
        if (searchRequest.getPrice()) {
            // 如果用户选择了价格排序
            queryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.ASC));
        }
        // 使用 bool 查询进行组合查询
        QueryBuilder query = buildBasicQueryWithFilter(searchRequest);

        queryBuilder.withQuery(query);

        Page<Goods> pageResult = goodsRepository.search(queryBuilder.build());

        if (pageResult == null || CollectionUtils.isEmpty(pageResult.getContent())) {
            return null;
        }
        result = new SearchResult();
        // 聚合获取商品分类, 和品牌信息, 需要根据原有的 elasticsearch 查询的基础上进行聚合获取数据
        String categoryAggName = "category";
        String brandAggName = "brand";
        // 添加聚合, 根据 cid3 参数聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        // 添加聚合, 根据 brandId 聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));
        // 设置聚合查询的结果 为1, 不然会全部查询商品的数据, 这里不能设置为 0 否则报错, 设置这个相当于聚合查询中使用关键字 size=0 的选项
        queryBuilder.withPageable(PageRequest.of(0, 1));
        // 开始聚合查询
        AggregatedPage<Goods> aggResult = template.queryForPage(queryBuilder.build(), Goods.class);
        // 封装 分类 数据到 map 中
        Map<String, Object> categorySpec = categorySpecHandler(categoryAggName, aggResult);
        // 封装到 searchResult 的规格参数(品牌信息, 分类, 规格参数)
        List<Object> specMap = new ArrayList<>();
        specMap.add(categorySpec); // 分类封装聚合中
        // 封装品牌信息
        Map<String, Object> brandSpec = brandSpecHandler(brandAggName, aggResult);
        specMap.add(brandSpec);
        // 根据 分类id 查询支持过滤的字段
        List<Map<String, Object>> specParam = specParamHandler((List<Category>) categorySpec.get("options"), query);
        // 规格参数过滤的参数, 加入到 集合中;
        specMap.add(specParam);
        // 设置总页数
        int total = (int) Math.ceil(pageResult.getTotalElements() / (size * 1.0));
        result.setTotal(total);
        result.setTotalElements(pageResult.getTotalElements());
        result.setData(pageResult.getContent());
        result.setFilterOptions(specMap);
        return result;
    }

    /**
     *  根据页面传递过来的条件进行组合查询, 并且过滤查询
     * */
    private QueryBuilder buildBasicQueryWithFilter(SearchRequest searchRequest) {
        // 使用布尔查询构建器
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 基本查询
        boolQueryBuilder.must(QueryBuilders.matchQuery("all", searchRequest.getKey()).minimumShouldMatch("75%"));
        // 过滤查询
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        Map<String, String> filter = searchRequest.getFilter();

        if (filter != null) {
            // 遍历获取过滤条件的每一个 key 和 value
            Set<Map.Entry<String, String>> entries = filter.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                // 把每一个key 组装成 elasticsearch 中的字段名称
                if (!StringUtils.equals(key, "分类") && !StringUtils.equals(key, "品牌")) {
                    key = "specs." + key + ".keyword";
                } else if (StringUtils.equals(key, "分类")) {
                    key = "cid3";
                } else {
                    key = "brandId";
                }
                // 把每一个组合查询的查询语添加到 一个中
                boolQuery.must(QueryBuilders.termQuery(key, value));
                //boolQueryBuilder.filter(QueryBuilders.termQuery(key, value));
            }
        }
        // 加入过滤条件
        boolQueryBuilder.filter(boolQuery);
        return boolQueryBuilder;
    }

    /**
     * 再原有的查询基础上进行聚合获取 用于过滤的规格参数
     */
    private List<Map<String, Object>> specParamHandler(List<Category> categories, QueryBuilder query) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 设置分页查询模拟 原生聚合查询的设置的 size 提高效率
        queryBuilder.withPageable(PageRequest.of(0, 1));
        // 设置基于原有的查询继续聚合查询
        queryBuilder.withQuery(query);

        List<Long> ids = categories.stream().map(category -> category.getId()).collect(Collectors.toList());
        // 用于存放规格参数映射的集合
        List<Map<String, Object>> result = new ArrayList<>();
        if (categories.size() >= 1) {
            List<SpecParam> specParams = goodsClient.querySpecParamByWhere(null, categories.get(0).getId(), true, null);
            for (SpecParam specParam : specParams) {
                String name = specParam.getName();
                // 设置聚合的名称
                String aggName = "specs." + name + ".keyword";
                // 使用精确匹配聚合查询结果
                queryBuilder.addAggregation(AggregationBuilders.terms(name).field(aggName));
            }
            AggregatedPage<Goods> aggGoods = template.queryForPage(queryBuilder.build(), Goods.class);
            // 获取一组聚合查询的结果集
            Aggregations aggs = aggGoods.getAggregations();
            // 获取所有的 聚合查询结果
            for (SpecParam specParam : specParams) {
                Map<String, Object> specMapping = new HashMap<>();
                // 根据聚合查询的名称, 获取聚合插叙弄得结果
                StringTerms specTerms = aggs.get(specParam.getName());
                // 获取搜索字段的规格属性的区间 如: CPU频率 : 1.5GHZ, 2.5GHZ 等等
                List<String> specList = specTerms.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());

                specMapping.put("key", specParam.getName());
                specMapping.put("options", specList);
                result.add(specMapping);
            }
        } else {
            return null;
        }

        return result;
    }

    /**
     * 把品牌的数据封装到规格集合中
     */
    private Map<String, Object> brandSpecHandler(String brandAggName, AggregatedPage<Goods> aggResult) {
        LongTerms brandAgg = (LongTerms) aggResult.getAggregation(brandAggName);

        List<Long> bids = brandAgg.getBuckets().stream().map(bucket -> bucket.getKeyAsNumber().longValue()).collect(Collectors.toList());
        HashMap<String, Object> map = new HashMap<>();
        map.put("key", "品牌");
        //  查询所有的品牌数据,
        List<Brand> brands = brandClient.queryBrandsByBids(bids);

        map.put("options", brands);
        return map;
    }

    /**
     * 封装分类信息, 根据聚合的结果
     */
    private Map<String, Object> categorySpecHandler(String categoryAggName, AggregatedPage<Goods> aggResult) {
        LongTerms aggregation = (LongTerms) aggResult.getAggregation(categoryAggName);
        List<Long> ids = aggregation.getBuckets().stream().map(bucket -> bucket.getKeyAsNumber().longValue()).collect(Collectors.toList());
        if (ids.size() > 1) {
            // 如果查询出来的分类过多, 则默认只要第一个分类的 id
            Long defaultId = ids.get(0);
            ids.removeAll(ids);
            ids.add(defaultId);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("key", "分类");
        List<Category> categories = categoryClient.queryCategoryByCids(ids);
        map.put("options", categories);
        return map;
    }


}
