package com.leyou.common.pojo;

import java.util.List;

/**
 * Vue Table 中分页使用的数据封装集合
 * @author shen youjian
 * @date 2018/7/19 17:18
 */

public class PageResult<T> {
    private long total;
    private List<T> data;
    private long totalElements;

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> date) {
        this.data = date;
    }
}
