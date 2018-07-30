package com.leyou.client;

import com.leyou.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author shen youjian
 * @date 2018/7/29 19:59
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
