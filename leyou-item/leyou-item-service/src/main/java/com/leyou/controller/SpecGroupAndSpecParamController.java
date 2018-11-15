package com.leyou.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.service.SpecGroupService;
import com.leyou.service.SpecParamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shen youjian
 * @date 2018/7/22 15:13
 */
@Controller
@RequestMapping("spec")
public class SpecGroupAndSpecParamController {

    @Autowired
    private SpecGroupService specGroupService;
    @Autowired
    private SpecParamService specParamService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 根据 Category id查询规格组
     */
    @GetMapping("groups/{id}")
    public ResponseEntity<List<SpecGroup>> queryGroupsByCid(@PathVariable("id") Long id) {
        logger.info("@GetMapping(\"groups/{id}\") 方法执行");
        try {
            SpecGroup record = new SpecGroup();
            record.setCid(id);
            List<SpecGroup> specGroups = this.specGroupService.queryListByWhere(record);
            return ResponseEntity.ok(specGroups);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 修改
     */
    @RequestMapping(value = "group", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateGroup(@RequestBody SpecGroup specGroup) {

        if (specGroup.getCid() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        boolean update = this.specGroupService.update(specGroup);
        if (!update) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 查询 SpecParam 属性
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> querySpecParamByGid(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "searching", required = false) Boolean searching,
            @RequestParam(value = "generic", required = false) Boolean generic) {

        // 根据 specGroup id 查询 specParam
        try {
            SpecParam specParam = new SpecParam();
            specParam.setGroupId(gid);
            specParam.setCid(cid);
            specParam.setSearching(searching);
            specParam.setGeneric(generic);
            List<SpecParam> specParams = this.specParamService.queryListByWhere(specParam);

            return ResponseEntity.ok(specParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 增加规格属性中的分组
     */
    @PostMapping("group")
    public ResponseEntity<Void> saveSpecGroup(@RequestBody SpecGroup specGroup) {
        specGroup.setId(null);
        Boolean insert = this.specGroupService.insert(specGroup);
        if (insert) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 根据组ID 删除分组
     *
     * @param gid
     * @return
     */
    @DeleteMapping("group/{gid}")
    public ResponseEntity<Void> deleteSpecGroupByGid(@PathVariable("gid") Long gid) {
        logger.info("删除SpecGroup 分组");
        boolean flag = this.specGroupService.deleteSpecGroupAndSpecParamByGid(gid);
        if (flag) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 新增 specParam 属性
     */
    @PostMapping("param")
    public ResponseEntity<Void> saveSpecParam(@RequestBody SpecParam specParam) {

        logger.info("新增specParam开始执行");

        boolean flag = specParamService.insert(specParam);
        if (flag) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 修改 specParam 属性
     */
    @PutMapping("param")
    public ResponseEntity<Void> updateSpecParam(@RequestBody SpecParam specParam) {
        // 修改
        logger.info("updateSpecParam()  开始执行");
        boolean flag = specParamService.update(specParam);
        if (flag) {
            logger.info("updateSpecParam()  修改成功");
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        logger.info("updateSpecParam()  修改失败结束执行");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 根据ID 删除 SpecParam
     */
    @DeleteMapping("param/{id}")
    public ResponseEntity<Void> deleteSpecParamById(@PathVariable("id") Long id) {
        // 删除
        logger.info("deleteSpecParamById()  删除SpecParam开始执行");
        Boolean flag = this.specParamService.deleteByPK(id);
        if (flag) {
            logger.info("deleteSpecParamById()  删除SpecParam成功");
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        logger.info("deleteSpecParamById()  删除SpecParam结束执行");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
