package com.leyou.controller;

import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.pojo.Brand;
import com.leyou.pojo.Category;
import com.leyou.service.BrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * 品牌类目的增删改查控制器
 *
 * @author shen youjian
 * @date 2018/7/19 16:24
 */
@Controller
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;



    /**
     * 参数 key=&page=1&rows=5&sortBy=id&desc=false
     * 返回的数据格式
     * 分页的数据封装对象
     *
     * @return
     */
    @RequestMapping(value = "page", method = RequestMethod.GET)
    public ResponseEntity<PageResult> queryBrandPage(
            @RequestParam(value = "key", defaultValue = "") String key,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", defaultValue = "") String sort,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc) {


        try {
            PageInfo<Brand> brandPageInfo = this.brandService.queryqueryPageListByWhere(key, page, rows, sort, desc);

            if (brandPageInfo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            PageResult<Brand> brandPageResult = new PageResult<>();

            brandPageResult.setData(brandPageInfo.getList());
            brandPageResult.setTotal(brandPageInfo.getTotal());

            return ResponseEntity.ok(brandPageResult);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }


    /**
     * 添加数据
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> addBrand(@RequestParam("name") String name,
                                         @RequestParam("cids") List<Long> cids,
                                         @RequestParam("letter") String letter,
                                         @RequestParam("image") String image) {

        if (StringUtils.isBlank(name) || StringUtils.isBlank(letter)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            Brand brand = new Brand();
            brand.setName(name);
            brand.setImage(StringUtils.isBlank(image) ? null : image);
            brand.setLetter(letter);

            this.brandService.saveBrandAndCids(brand, cids);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 修改数据
     */
    @RequestMapping(value = "/bid/{bid}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateBrand(@PathVariable(value = "bid") Long bid) {
        if (bid == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 根据品牌id 获取分类信息
     */
    @RequestMapping(value = "/bid/{bid}", method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getCategorysByBid(@PathVariable("bid") Long bid) {

        List<Category> categories = this.brandService.queryCategorysByBid(bid);

        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }
    /**
     * 根据id 删除数据
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteBrand(@RequestBody Brand brand) {

        try {
            boolean flag = this.brandService.deleteBrandAndImage(brand);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 根据分类 id 查询品牌集合
     */
    @GetMapping("/cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandsByCid(@PathVariable("cid") Long cid) {

        List<Brand> brands = brandService.queryBrandsByCid(cid);
        if (brands != null) {
            return ResponseEntity.ok(brands);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    /**
     * 根据品牌id 查询品牌数据
     */
    @GetMapping("/bid")
    public ResponseEntity<Brand> queryBrandByBid(@RequestParam("bid") Long bid) {
        Brand brand = brandService.queryById(bid);
        if (bid == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(brand);
    }

    @GetMapping("list")
    public ResponseEntity<List<Brand>> queryBrandsByBids(@RequestParam("bids") List<Long> bids) {

        List<Brand> brands = brandService.queryBrandsByBids(bids);
        if (brands == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(brands);
    }
}
