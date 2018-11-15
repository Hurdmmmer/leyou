package com.leyou.dao;

import com.leyou.item.pojo.Sku;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author shen youjian
 * @date 2018/7/23 20:18
 */
public interface SkuMapper extends Mapper<Sku>, InsertListMapper<Sku> {
}
