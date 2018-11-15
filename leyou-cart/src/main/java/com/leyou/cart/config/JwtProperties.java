package com.leyou.cart.config;

import com.leyou.auth.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 *  获取公钥的配置属性类， 用于给拦截器鉴权用的配置
 * @author shen youjian
 * @date 2018/8/5 16:40
 */
@ConfigurationProperties(prefix = "ly.jwt")
@Data
@Slf4j
public class JwtProperties {
    private String pubKeyPath;
    private String cookieName;
    private PublicKey publicKey;


    @PostConstruct
    public void init(){
        try {
            publicKey = RsaUtils.getPublicKey(this.pubKeyPath);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("购物车鉴权公钥加载失败.", e);
        }
    }




}
