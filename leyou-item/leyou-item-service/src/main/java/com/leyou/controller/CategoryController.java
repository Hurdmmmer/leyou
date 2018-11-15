package com.leyou.controller;

import com.leyou.item.pojo.Category;
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

    /**
     *  根据 parentId 查询 分类列表
     */
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

    /**
     * 根据商品分类id集合 查询所有的分类数据
     */
    @GetMapping("names")
    public ResponseEntity<List<String>> queryCategoryNamesByCids(@RequestParam("cids") List<Long> cids ) {

        List<String> result = this.categoryService.queryCategoryNamesByCid(cids);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("categorys")
    public ResponseEntity<List<Category>> queryCategoryByCids(@RequestParam("cids") List<Long> cids ) {

        List<Category> result = this.categoryService.queryCategoryByCids(cids);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(result);
    }


    /***
     *  根据分类 id 查询该所有分类
     */
    @GetMapping("all/level")
    public ResponseEntity<List<Category>> queryCategorysByCid(@RequestParam("id")Long id) {

        List<Category> result = this.categoryService.queryCategoryByCid(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(result);
    }

}
