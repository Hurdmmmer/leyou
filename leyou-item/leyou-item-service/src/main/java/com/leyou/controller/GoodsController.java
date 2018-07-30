package com.leyou.controller;

import com.leyou.pojo.Spu;
import com.leyou.service.GoodsService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author shen youjian
 * @date 2018/7/23 19:47
 */
@Controller
@RequestMapping("goods")
public class GoodsController {
    @Autowired
    private Logger logger;
    @Autowired
    private GoodsService goodsService;

    /**
     * 添加商品
     */
    @PostMapping
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu) {

        logger.info("开始添加商品");
        try {
            boolean flag = goodsService.saveGoods(spu);
            if (!flag) {
                logger.info("添加商品失败: {}", spu);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            logger.info("添加商品成功: {}", spu);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("添加商品失败: 异常错误");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 修改商品
     */
    @PutMapping
    public ResponseEntity<Void> updateGoods(@RequestBody Spu spu) {

        try {
            boolean flag = goodsService.updateGoods(spu);
            if (!flag) {
                return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }




}
