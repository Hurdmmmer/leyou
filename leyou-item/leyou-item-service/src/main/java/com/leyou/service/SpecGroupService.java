package com.leyou.service;

import com.leyou.dao.SpecGroupMapper;
import com.leyou.pojo.SpecGroup;
import com.leyou.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;

/**
 *  规格参数 service 业务层
 * @author shen youjian
 * @date 2018/7/22 15:10
 */
@Service
public class SpecGroupService extends BaseService<SpecGroup> {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamService specParamService;

    @Override
    public Mapper<SpecGroup> getMapper() {
        return this.specGroupMapper;
    }


    /**
     *  根据 组ID 删除 组和改组下面的数据
     */
    @Transactional
    public boolean deleteSpecGroupAndSpecParamByGid(Long gid) {
        boolean flag = false;
        try {
            flag = super.deleteByPK(gid);
            if (flag) {
                SpecParam record = new SpecParam();
                record.setGroupId(gid);
                flag = specParamService.deleteByWhere(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}
