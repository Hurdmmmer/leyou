package com.leyou.config;

import com.leyou.auth.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 *  鉴权相关的配置类
 * @author shen youjian
 * @date 2018/8/4 16:37
 */
@ConfigurationProperties(prefix = "ly.jwt")
@Slf4j
@Data
public class JwtProperties {
    private String pubKeyPath;
    private String cookieName;

    private PublicKey publicKey;


    @PostConstruct
    public void init() {
        if (StringUtils.isBlank(pubKeyPath) || StringUtils.isBlank(cookieName)) {
            log.info("请配置公钥文件地址或cookieName");
        }
        try {
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            log.error("加载公钥失败. {}", pubKeyPath);
            e.printStackTrace();
        }
    }
}
