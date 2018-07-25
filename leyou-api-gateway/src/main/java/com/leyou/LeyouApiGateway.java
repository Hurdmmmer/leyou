package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 乐优商城的网关启动类,
 * @author shen youjian
 * @date 7/15/2018 2:52 PM
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class LeyouApiGateway {

    public static void main(String[] args) {
        SpringApplication.run(LeyouApiGateway.class, args);
    }
}
