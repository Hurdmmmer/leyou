package com.leyou.item.api;

import com.leyou.item.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author shen youjian
 * @date 2018/7/26 16:57
 */
@RequestMapping("brand")
public interface BrandApi {
    @GetMapping("/bid")
    Brand queryBrandByBid(@RequestParam("bid") Long bid);

    /** 根据一组 品牌id 查询所有的品牌*/
    @GetMapping("list")
    List<Brand> queryBrandsByBids(@RequestParam("bids") List<Long> bids);
}
