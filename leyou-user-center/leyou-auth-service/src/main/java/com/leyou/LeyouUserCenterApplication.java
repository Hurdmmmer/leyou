package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 *  用户中心, 用于给登陆用户鉴权, 赋予 token
 * @author shen youjian
 * @date 2018/8/2 16:35
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class LeyouUserCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeyouUserCenterApplication.class, args);
    }
}
