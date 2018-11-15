package com.leyou.item.api;

import com.leyou.item.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *  提供外部服务调用的 api 接口, 使用 feign 风格获取数据
 * @author shen youjian
 * @date 2018/7/26 16:04
 */
@RequestMapping("category")
public interface CategoryApi {
    @GetMapping("names")
    List<String> queryCategoryNamesByCids(@RequestParam("cids") List<Long> cids ) ;

    @GetMapping("categorys")
    List<Category> queryCategoryByCids(@RequestParam("cids") List<Long> cids );
}
