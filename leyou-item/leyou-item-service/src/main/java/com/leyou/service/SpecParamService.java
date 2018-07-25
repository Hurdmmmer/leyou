package com.leyou.service;

import com.leyou.dao.SpecParamMapper;
import com.leyou.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

/**
 *  规格参数业务层
 * @author shen youjian
 * @date 2018/7/23 13:45
 */
@Service
public class SpecParamService extends BaseService<SpecParam> {
    @Autowired
    private SpecParamMapper specParamMapper;
    @Override
    public Mapper<SpecParam> getMapper() {
        return specParamMapper;
    }
}
