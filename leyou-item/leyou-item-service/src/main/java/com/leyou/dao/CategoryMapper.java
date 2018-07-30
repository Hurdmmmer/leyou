package com.leyou.dao;

import com.leyou.pojo.Category;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 *  商品分类的通用mapper接口
 * @author shen youjian
 * @date 2018/7/18 16:50
 */
public interface CategoryMapper extends Mapper<Category>, SelectByIdListMapper<Category, Long> {
}
