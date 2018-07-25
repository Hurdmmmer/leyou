package com.leyou.config;

import org.apache.http.client.methods.HttpDelete;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shen youjian
 * @date 2018/7/22 21:53
 */
@Configuration
@EnableConfigurationProperties(BaseUrlProperties.class)
public class HttpDeleteConfig {

    @Bean
    public HttpDelete httpDelete(BaseUrlProperties baseUrlProperties) {
        return new HttpDelete(baseUrlProperties.getUrl());
    }


}
