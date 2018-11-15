package com.leyou.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author shen youjian
 * @date 2018/7/29 19:38
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
