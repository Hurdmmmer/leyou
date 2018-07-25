package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author shen youjian
 * @date 2018/7/21 15:34
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ImageApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImageApplication.class, args);
    }
}
