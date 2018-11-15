package com.leyou.user.api;

import com.leyou.user.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *  向外提供的查询user 的接口, 给 feign 客服端使用
 * @author shen youjian
 * @date 2018/8/2 17:20
 */
public interface UserApi {
    /**
     *  根据用户名, 密码查询用户
     * */
    @GetMapping("query")
   User queryUserByUsernameAndPassword(@RequestParam("username")String username,
                                                               @RequestParam("password")String password);
}
