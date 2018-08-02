package com.leyou.com.leyou.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *  用于读取yml文件的与短信发送相关的配置属性
 * @author shen youjian
 * @date 2018/8/1 23:26
 */
@ConfigurationProperties(prefix = "ly.sms")
public class AliDayuProperties {
    private String accessKeyId;

    private String accessKeySecret;

    private String signName;

    private String verifyCodeTemplate;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getVerifyCodeTemplate() {
        return verifyCodeTemplate;
    }

    public void setVerifyCodeTemplate(String verifyCodeTemplate) {
        this.verifyCodeTemplate = verifyCodeTemplate;
    }
}
