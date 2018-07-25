package com.leyou.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author shen youjian
 * @date 2018/7/22 21:24
 */
@ConfigurationProperties(prefix = "delete-image-url")
public class BaseUrlProperties {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
