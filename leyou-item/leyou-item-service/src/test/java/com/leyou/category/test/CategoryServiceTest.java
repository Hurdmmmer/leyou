package com.leyou.category.test;

import com.leyou.pojo.Category;
import com.leyou.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author shen youjian
 * @date 2018/7/18 17:19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceTest {
    @Autowired
    private CategoryService service;


    @Test
    public void queryList() {
        Category record = new Category();
        record.setParentId(0L);
        List<Category> categories = this.service.queryListByWhere(record);

        System.out.println("categories = " + categories);
    }
}
