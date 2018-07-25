package com.leyou.controller;

import com.leyou.pojo.Category;
import com.leyou.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  查询数据库获取 item category 的控制器 api
 * @author shen youjian
 * @date 2018/7/18 16:18
 */
@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getCategoryById(
            @RequestParam(value = "pid", defaultValue = "0") Long pid) {

        try {
            Category record = new Category();
            record.setParentId(pid);
            List<Category> categories = this.categoryService.queryListByWhere(record);

            return ResponseEntity.status(HttpStatus.OK).body(categories);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
