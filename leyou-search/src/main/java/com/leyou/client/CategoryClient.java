package com.leyou.client;

import com.leyou.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 使用 feign 风格的客服端调用远程的服务获取数据,
 * feign 获取自动生成一个 restTemplate 实体类,发送http请求
 * @author shen youjian
 * @date 2018/7/26 16:32
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {

}
