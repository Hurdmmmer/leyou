package com.leyou.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 *  单表查询的通用 service 层, 一些常用的增删改查
 * @author shen youjian
 * @date 2018/7/18 16:52
 */
public abstract class BaseService<T> {
    // 子类实现, 获取子类具体 service 中使用的 mapper
    abstract public Mapper<T> getMapper();

    /**
     * 根据主键查询
     */
    public T queryById(Long id) {
        return this.getMapper().selectByPrimaryKey(id);
    }
    /**
     * 查询所有
     */
    public List<T> queryAll() {
        return this.getMapper().selectAll();
    }

    /**
     * 根据条件查询一个
     */
    public T queryOne(T record) {
        return this.getMapper().selectOne(record);
    }

    /**
     * 根据条件查询多个
     */
    public List<T> queryListByWhere(T record) {
        return this.getMapper().select(record);
    }

    /**
     *  根据范围查询多个
     * @param clazz  查询的表对应的实体类
     * @param field 查询的字段
     */
    public List<T> queryListByIn(List<Object> ids, Class clazz, String field) {
        Example example = new Example(clazz);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn(field, ids);

        return getMapper().selectByExample(example);
    }

    /**
     * 通用的分页查询
     * @param pageNum 当前页
     * @param pageSize 每页显示条数
     * @param record 查询条件
     * @return
     */
    public PageInfo<T> queryPageListByWhere(Integer pageNum, Integer pageSize, T record) {
        // 启动分页助手
        PageHelper.startPage(pageNum, pageSize);
        List<T> list = this.getMapper().select(record);
        return new PageInfo<>(list);
    }

    /**
     * 插入数据, 并设置好创建时间 和 修改时间
     * @param record
     * @return
     */
    public Boolean insert(T record) {
//        record.setCreated(new Date());
//        record.setUpdated(new Date());
        return this.getMapper().insertSelective(record) == 1;
    }

    /**\
     * 修改数据
     * @param record
     * @return
     */
    public boolean update(T record) {
//        record.setUpdated(new Date());
        return this.getMapper().updateByPrimaryKeySelective(record) == 1;
    }

    /**
     *  根据主键删除
     * @param key
     * @return
     */
    public Boolean deleteByPK(Object key) {
        return this.getMapper().deleteByPrimaryKey(key) == 1;
    }

    /**
     * 根据传入的参数集合批量删除
     * @param property 数据库中哪个一个字段
     * @param params 条件
     * @param clazz 对应表的pojo类的class对象
     * @return 是否删除成功
     */
    public Boolean deleteByProperty(String property, List<Object> params, Class clazz) {
        Example example = new Example(clazz);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn(property, params);

        return this.getMapper().deleteByExample(example) > 0;
    }

    /**
     *  根据条件删除
     * @param record
     * @return
     */
    public boolean deleteByWhere(T record) {
        return this.getMapper().delete(record) == 1;
    }

}
