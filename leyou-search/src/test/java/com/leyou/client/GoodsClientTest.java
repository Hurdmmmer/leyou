package com.leyou.client;

import com.leyou.LeyouSearchApp;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeyouSearchApp.class)
public class GoodsClientTest {
    @Autowired
    private GoodsClient goodsClient;

    @Test
    public void test1() {
        List<Sku> skus = this.goodsClient.querySkusById(2L);
        List<Map<String, Object>> list = new ArrayList<>();
        for (Sku sku : skus) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("price", sku.getPrice());
            map.put("image", StringUtils.isNotBlank(sku.getImages()) ? sku.getImages().split(",")[0] : "");
            list.add(map);
        }

        System.out.println("list = " + list);
    }
}