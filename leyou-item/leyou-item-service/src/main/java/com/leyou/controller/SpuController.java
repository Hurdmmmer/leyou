package com.leyou.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import com.leyou.service.GoodsService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * 商品列表控制器
 *
 * @author shen youjian
 * @date 2018/7/23 15:24
 */
@Controller
@RequestMapping("spu")
public class SpuController {


    @Autowired
    private GoodsService goodsService;

    @Autowired
    private Logger logger;

    /**
     * key:
     * saleable: true
     * page: 1
     * rows: 5
     * 根据条件分页查询
     */
    @GetMapping("page")
    public ResponseEntity<PageResult> querySkuByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable) {
        logger.info("商品分页查询开始");
        try {
            PageResult<Spu> pageResult = this.goodsService.queryPageListByWhere(key, page, rows, saleable);
            if (pageResult == null) {
                logger.info("没有查询到商品");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            logger.info("商品分页查询正常结束");
            return ResponseEntity.ok(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("商品分页查询异常结束");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 根据 id 查询 SpuDetail 数据
     */

    @GetMapping("detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("id") Long id) {

        try {
            SpuDetail spuDetail = goodsService.querySpuDetailById(id);
            if (spuDetail == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            return ResponseEntity.ok(spuDetail);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
