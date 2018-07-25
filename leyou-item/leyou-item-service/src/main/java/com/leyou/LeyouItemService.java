package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 乐优商城商品管理的启动类
 * @author shen youjian
 * @date 7/15/2018 3:14 PM
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@MapperScan(basePackages = "com.leyou.dao")
public class LeyouItemService {
    public static void main(String[] args) {
        SpringApplication.run(LeyouItemService.class, args);
    }
}
