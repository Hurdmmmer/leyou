package com.leyou.service;

import com.leyou.dao.CategoryMapper;
import com.leyou.dao.SpuMapper;
import com.leyou.pojo.Category;
import com.leyou.pojo.Spu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *   商品管理的 service 层
 * @author shen youjian
 * @date 2018/7/18 16:51
 */
@Service
public class CategoryService extends BaseService<Category> {

    private final CategoryMapper mapper;
    @Autowired
    private SpuMapper spuMapper;
    // 使用构造函数给字段进行注入
    @Autowired
    public CategoryService(CategoryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Mapper<Category> getMapper() {
        return this.mapper;
    }

    /**
     *  根据一组 cid 查询分类的名称
     */
    public List<String> queryCategoryNamesByCid(List<Long> cids) {
        Category category = new Category();
        List<String> name = mapper.selectByIdList(cids).stream().map(c -> c.getName()).collect(Collectors.toList());

        return name;
    }

    /**
     * 根据一组 cid 查询分类
     * @param cids
     * @return
     */
    public List<Category> queryCategoryByCids(List<Long> cids) {
        return mapper.selectByIdList(cids);
    }

    /**
     * 根据一个id 查询 所有的三级分类
     * @param id
     * @return
     */
    public List<Category> queryCategoryByCid(Long id) {
        Category category = mapper.selectByPrimaryKey(id);
        if (category.getParentId() == null) {
            return null;
        }
        Category category1 = mapper.selectByPrimaryKey(category.getParentId());
        Category category2 = mapper.selectByPrimaryKey(category1.getParentId());
        return Arrays.asList(category2, category1, category);
    }

}
