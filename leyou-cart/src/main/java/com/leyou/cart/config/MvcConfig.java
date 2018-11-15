package com.leyou.cart.config;

import com.leyou.cart.interceptor.CartInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *  拦截器生效，需要继承 WebMvcConfigurer， 加入我们的自定义的拦截器
 * @author shen youjian
 * @date 2018/8/5 16:46
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtProperties properties;

    @Bean
    public CartInterceptor cartInterceptor() {
        return new CartInterceptor(properties);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加自定义拦截器， 并对所有请求都进行拦截
        registry.addInterceptor(cartInterceptor()).addPathPatterns("/**");
    }
}
