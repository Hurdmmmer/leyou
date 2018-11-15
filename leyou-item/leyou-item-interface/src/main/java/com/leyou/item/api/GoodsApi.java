package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 使用 Feign 风格自动生成 http 请求, 接口的方法必须跟 controller 中的方法一致
 *
 * @author shen youjian
 * @date 2018/7/26 17:11
 */

public interface GoodsApi {
    /**
     * 根据spuId 查询该 spu 下 所有的 sku 数据
     */
    @GetMapping("sku/list")
    List<Sku> querySkusById(@RequestParam("id") Long id);

    /**
     * 根据分页数据, 查询所有的 商品集 spu 的数据
     */
    @GetMapping("spu/page")
    PageResult<Spu> querySkuByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable);

    /**
     * 根据商品分类id 查询 该分类下的参与搜索的规格属性
     *
     * @param gid       规格分组的 id
     * @param cid       分类ID
     * @param searching 是否支持搜索
     * @param generic   是否是通用的规格属性
     */
    @GetMapping("spec/params")
    List<SpecParam> querySpecParamByWhere(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "searching", required = false) Boolean searching,
            @RequestParam(value = "generic", required = false) Boolean generic);

    /**
     * 根据 spu id 查询 SpuDetail 数据
     */
    @GetMapping("spu/detail/{id}")
    SpuDetail querySpuDetailById(@PathVariable("id") Long id);

    /**
     * 根据 spuId 查询 spu对象
     */
    @RequestMapping("spu")
    Spu querySpuBySpuId(@RequestParam("spuId") Long spuId);

    /**
     *  根据 skuId 查询库存
     *  */
    @GetMapping("sku/stock")
    Stock queryStockBySkuId(@RequestParam("skuId") Long skuId);
}
