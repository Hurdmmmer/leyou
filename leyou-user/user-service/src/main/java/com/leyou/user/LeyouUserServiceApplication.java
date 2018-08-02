package com.leyou.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author shen youjian
 * @date 2018/8/1 20:06
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.leyou.user.mapper")
public class LeyouUserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeyouUserServiceApplication.class, args);
    }
}
