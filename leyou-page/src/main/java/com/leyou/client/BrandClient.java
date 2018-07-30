package com.leyou.client;

import com.leyou.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author shen youjian
 * @date 2018/7/29 20:09
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
