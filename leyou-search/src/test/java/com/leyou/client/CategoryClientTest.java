package com.leyou.client;

import com.leyou.LeyouSearchApp;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeyouSearchApp.class)
public class CategoryClientTest {

    @Autowired
    private CategoryClient categoryClient;

    @Test
    public void test1() {
        String all = StringUtils.join(categoryClient.queryCategoryNamesByCids(Arrays.asList(74L, 75L, 76L)), "/");
        System.out.println("all = " + all);
    }
}