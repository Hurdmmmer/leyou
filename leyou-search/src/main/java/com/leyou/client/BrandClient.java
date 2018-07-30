package com.leyou.client;

import com.leyou.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 品牌 Feign 客服端
 * @author shen youjian
 * @date 2018/7/26 17:01
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
