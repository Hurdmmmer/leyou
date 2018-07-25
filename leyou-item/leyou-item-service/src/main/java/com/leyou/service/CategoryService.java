package com.leyou.service;

import com.leyou.dao.CategoryMapper;
import com.leyou.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

/**
 *   商品管理的 service 层
 * @author shen youjian
 * @date 2018/7/18 16:51
 */
@Service
public class CategoryService extends BaseService<Category> {

    private final CategoryMapper mapper;
    // 使用构造函数给字段进行注入
    @Autowired
    public CategoryService(CategoryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Mapper<Category> getMapper() {
        return this.mapper;
    }

}
