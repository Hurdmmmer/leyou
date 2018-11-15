package com.leyou.client;

import com.leyou.item.api.SpecificationParamApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 *  查询 规格参数的 http Feign请求对象
 * @author shen youjian
 * @date 2018/7/29 20:21
 */
@FeignClient("item-service")
public interface SpecificatonClient extends SpecificationParamApi {
}
