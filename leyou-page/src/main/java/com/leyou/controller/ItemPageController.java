package com.leyou.controller;

import com.leyou.service.ItemPageService;
import com.leyou.service.StaticPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 *  页面静态化的控制类, 使用 thymeleaf 模板视图解析器, 我们使用了
 *  spring boot 提供的自动注入, thymeleaf 默认将使用 resources/templates下 以 .html 结尾的文件
 *  详细请参看 thymeleaf 的自动注入类
 * @author shen youjian
 * @date 2018/7/29 19:08
 */
@Controller
public class ItemPageController {

    @Autowired
    private ItemPageService itemPageService;
    @Autowired
    private StaticPageService staticPageService;
    // 使用 thymeleaf 模板解析 和 jsp 差不多 也可以使用 model存放数据, 然后再页面上渲染
    @RequestMapping("item/{id}.html")
    public String toItemPage(@PathVariable("id")Long id, Model model) {
        // 查询出的数据, 存放到 model 中, spring boot 会将自动将数据存放到 thymeleaf 的 context 容器中
        Map<String, Object> result = itemPageService.builderGoods(id);
        model.addAllAttributes(result);
        // 生成静态页面
        // 判断静态页面是否存在
        if(!staticPageService.exists(id)) {
            staticPageService.syncCreateHtml(id);
        }

        return "item";
    }

}
