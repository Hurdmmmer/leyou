package com.leyou.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 *  过滤配置类, 再未登录的情况下可以访问的路径
 * @author shen youjian
 * @date 2018/8/4 16:34
 */
@ConfigurationProperties(prefix = "ly.filter")
public class FilterProperties {
    List<String> allowPaths;

    public List<String> getAllowPaths() {
        return allowPaths;
    }

    public void setAllowPaths(List<String> allowPaths) {
        this.allowPaths = allowPaths;
    }
}
