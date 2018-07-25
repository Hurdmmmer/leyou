package com.leyou.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author shen youjian
 * @date 2018/7/21 16:15
 */
@Component
@ConfigurationProperties
public class BaseUrlProperties {
    @Value("${image.baseUrl}")
    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
