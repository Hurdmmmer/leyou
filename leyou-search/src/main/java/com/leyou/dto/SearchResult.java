package com.leyou.dto;

import com.leyou.common.pojo.PageResult;
import com.leyou.user.pojo.Goods;

import java.util.List;

/**
 *  由于原本的 PageResult 中属性不够, 需要添加几个属性用于
 *  保存商品搜索过滤的条件
 * @author shen youjian
 * @date 2018/7/28 16:34
 */
public class SearchResult extends PageResult<Goods> {

    private List<Object> filterOptions;

    public List<Object> getFilterOptions() {
        return filterOptions;
    }

    public void setFilterOptions(List<Object> filterOptions) {
        this.filterOptions = filterOptions;
    }
}
