package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 乐优商城的 eureka 启动类, 用于注册服务的注册中心
 * @author shen youjian
 * @date 7/15/2018 2:31 PM
 */
@SpringBootApplication
@EnableEurekaServer
public class LeyouRegistry {
    public static void main(String[] args) {
        SpringApplication.run(LeyouRegistry.class, args);
    }
}
