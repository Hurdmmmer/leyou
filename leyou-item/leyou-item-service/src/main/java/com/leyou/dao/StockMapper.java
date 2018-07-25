package com.leyou.dao;

import com.leyou.pojo.Stock;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 *  库存
 * @author shen youjian
 * @date 2018/7/23 20:28
 */
public interface StockMapper extends Mapper<Stock>, InsertListMapper<Stock>, DeleteByIdListMapper<Stock, Long> {
}
