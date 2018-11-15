package com.leyou.service;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.client.UserClient;
import com.leyou.config.JwtProperties;
import com.leyou.user.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 *  授权认证处理类
 * @author shen youjian
 * @date 2018/8/2 18:18
 */
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {
    /** 创建 token 要使用的私钥, 公钥, 的配置文件 */
    @Autowired
    private JwtProperties jwtProperties;
    /**调用 user 服务, 验证账号密码是否正确 */
    @Autowired
    private UserClient userClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    /**
     *  根据账号密码 获取该用户的 token
     * @param username 账号
     * @param password 密码
     * @return 返回 null 标识该账号密码错误, 正常返回一个 token 字符串
     */
    public String authentication(String username, String password) throws Exception {

        User user = userClient.queryUserByUsernameAndPassword(username, password);
        if (user == null) {
            LOGGER.info("用户信息不存在. user: {}", username);
            return null;
        }
        UserInfo userInfo = new UserInfo(user.getId(), user.getUsername());
        // 根据私钥, token 载荷中携带的用户信息 过期时间, 生成一个 token
        String token = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
        LOGGER.info("生成token: {}", token);
        return token;
    }
}
