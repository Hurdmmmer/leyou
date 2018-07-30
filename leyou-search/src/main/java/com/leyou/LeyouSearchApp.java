package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 *  搜索微服务
 * @author shen youjian
 * @date 2018/7/26 13:23
 */
@SpringBootApplication
// 开启 eureka 客服端自动注册到 eureka 服务器中
@EnableDiscoveryClient
// 开启 Feign 客服端, 能够方便提供服务间的调用
@EnableFeignClients
public class LeyouSearchApp {
    public static void main(String[] args) {
        SpringApplication.run(LeyouSearchApp.class, args);
    }
}
