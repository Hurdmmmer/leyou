package com.leyou.client;

import com.leyou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author shen youjian
 * @date 2018/8/2 17:22
 */
@FeignClient("user-service")
public interface UserClient extends UserApi {
}
