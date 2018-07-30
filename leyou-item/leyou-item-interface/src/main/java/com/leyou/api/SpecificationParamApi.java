package com.leyou.api;

import com.leyou.pojo.SpecGroup;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author shen youjian
 * @date 2018/7/29 20:18
 */
public interface SpecificationParamApi {
    /**
     * 根据 Category id查询规格组
     */
    @GetMapping("spec/groups/{id}")
   List<SpecGroup> queryGroupsByCid(@PathVariable("id") Long id);
}
