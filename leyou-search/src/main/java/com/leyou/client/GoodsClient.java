package com.leyou.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 *  用于查询 sku 数据的 Feign 接口
 * @author shen youjian
 * @date 2018/7/26 17:08
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
