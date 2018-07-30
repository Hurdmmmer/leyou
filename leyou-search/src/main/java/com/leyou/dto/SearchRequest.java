package com.leyou.dto;

import java.util.Map;

/**
 *  用于接受 前端页面发送过来的请求中携带的数据的封装
 * @author shen youjian
 * @date 2018/7/26 13:40
 */
public class SearchRequest {
    /** 关键字 */
    private String key;
    /** 分页查询的页码 */
    private Integer page;
    /** 分页每页显示数 */
    private Integer size;
    /** 设置默认的页码 和 大小, 防止前端乱传数据 */
    private final int DEFAULT_PAGE = 1, DEFAULT_SIZE = 20;
    /** 根据价格排序标识 */
    private Boolean price;
    /** 根据商品的上架日期进行排序 */
    private Boolean newGoods;
    /** 过滤的条件 */
    private Map<String, String> filter;

    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }

    public Boolean getPrice() {
        return price;
    }

    public void setPrice(Boolean price) {
        this.price = price;
    }

    public Boolean getNewGoods() {
        return newGoods;
    }

    public void setNewGoods(Boolean newGoods) {
        this.newGoods = newGoods;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if (this.page == null) {
            return DEFAULT_PAGE;
        }
        return page;
    }

    public void setPage(Integer page) {
        this.page = Math.max(page, DEFAULT_PAGE);

    }

    public Integer getSize() {
        if (this.size == null) {
            return DEFAULT_SIZE;
        }
        return size;
    }

    public void setSize(Integer size) {
        this.size = Math.max(size, DEFAULT_SIZE);
    }
}
