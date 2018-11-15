package com.leyou.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 *  支付微服务
 * @author shen youjian
 * @date 2018/8/6 13:32
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class LeyouOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeyouOrderApplication.class, args);
    }
}
