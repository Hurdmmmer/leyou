package com.leyou.dao;

import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author shen youjian
 * @date 2018/7/19 16:25
 */
public interface BrandMapper extends Mapper<Brand>, IdListMapper<Brand, Long> {
    /**
     * 新增商品分类和品牌中间表数据
     * @param cid 商品分类id
     * @param bid 品牌id
     * @return
     */
    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    @Select("SELECT b.* from tb_category_brand as a LEFT JOIN tb_category b on  b.id = a.category_id WHERE a.brand_id =  #{bid} ")
    List<Category> queryCategoryByBid(@Param("bid") Long bid);

    @Delete("delete from tb_category_brand where brand_id=#{bid}")
    int deleteCategoryAndBrandByBid(@Param("bid") Long bid);

    @Select("SELECT * FROM tb_brand as b LEFT JOIN tb_category_brand as cb on b.id = cb.brand_id where cb.category_id = #{cid} ")
    List<Brand> queryBrandByCid(@Param("cid") Long cid);
}
