package com.leyou.controller;

import com.leyou.pojo.Sku;
import com.leyou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author shen youjian
 * @date 2018/7/23 21:40
 */
@Controller
@RequestMapping("sku")
public class SkuController {

    @Autowired
    private GoodsService goodsService;

    /**
     *  根据 id 查询 sku 列表
     */
    @GetMapping("list")
    public ResponseEntity<List<Sku>> querySkusById(@RequestParam("id")Long id) {
        try {
            List<Sku> skus = goodsService.querySkusBySpuId(id);
            if (skus == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            return ResponseEntity.ok(skus);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
