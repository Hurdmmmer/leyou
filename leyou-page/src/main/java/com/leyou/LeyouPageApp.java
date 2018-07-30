package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 *  页面静态化
 * @author shen youjian
 * @date 2018/7/29 18:58
 */
@SpringBootApplication
// eureka 客服端自动注册
@EnableDiscoveryClient
// feign 客服端启动
@EnableFeignClients
public class LeyouPageApp {

    public static void main(String[] args) {
        SpringApplication.run(LeyouPageApp.class, args);
    }
}
