package com.leyou.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.dao.BrandMapper;
import com.leyou.pojo.Brand;
import com.leyou.pojo.Category;
import com.sun.org.apache.xpath.internal.SourceTree;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jcp.xml.dsig.internal.SignerOutputStream;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author shen youjian
 * @date 2018/7/19 16:25
 */
@Service
public class BrandService extends BaseService<Brand> {

    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private CloseableHttpClient httpClient;
    @Autowired
    private HttpDelete httpDelete;
    @Autowired
    private Logger logger;

    @Override
    public Mapper<Brand> getMapper() {
        return brandMapper;
    }


    /**
     * 根据条件进行分页查询
     */

    public PageInfo<Brand> queryqueryPageListByWhere(String key, Integer pageNum, Integer pageSize, String sort, Boolean desc) {
        // 启动分页助手
        PageHelper.startPage(pageNum, pageSize);
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("name", "%" + key + "%")
                    .orEqualTo("letter", key);
        }

        if (StringUtils.isNotBlank(sort)) {
            if (desc) {
                example.orderBy(sort).desc();
            } else {
                example.orderBy(sort).asc();
            }
        }

        List<Brand> brands = this.brandMapper.selectByExample(example);

        return new PageInfo<>(brands);
    }

    /**
     * 根据分类id 添加关系表和 brand表
     * 使用事务管理
     */
    @Transactional
    public void saveBrandAndCids(Brand brand, List<Long> cids) {
        // 插入数据库
        super.insert(brand);

//        批量插入关系表
        for (Long cid : cids) {
            this.brandMapper.insertCategoryBrand(cid, brand.getId());
        }

    }

    public List<Category> queryCategorysByBid(Long bid) {
        return this.brandMapper.queryCategoryByBid(bid);
    }

    /**
     * 根据 id 删除品牌和图片
     */
    @Transactional
    public boolean deleteBrandAndImage(Brand brand) throws URISyntaxException {

        Boolean flag = false;
        flag = super.deleteByPK(brand.getId());
        if (flag) {
            flag = this.brandMapper.deleteCategoryAndBrandByBid(brand.getId()) > 0;
        }
        // 获取 image 地址
        String image = brand.getImage();
        URI uri = this.httpDelete.getURI();
        int index = image.indexOf("com");
        String param = image.substring(index + 3, image.length());
        //System.out.println("访问的 url 地址是: " + uri +"?id="+ param);

        URI uri1 = new URI(uri + "?id=" + param);
        logger.info("删除图片的请求 url : {}", uri1);
        this.httpDelete.setURI(uri1);
        CloseableHttpResponse response = null;
        try {
            // 执行删除图片
            response = this.httpClient.execute(httpDelete);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode != 200) {
                logger.info("删除图片失败, 状态码: " + statusCode);
                throw new RuntimeException("删除品牌图片异常");
            }
            logger.info("删除图片成功, 状态码: " + statusCode);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 根据 cid 查询分类下的所有的品牌
     */
    public List<Brand> queryBrandsByCid(Long cid) {
        List<Brand> brands = brandMapper.queryBrandByCid(cid);
        return brands;
    }

    /**
     * 根据一组 品牌id 查询品牌
     * @param bids
     * @return
     */
    public List<Brand> queryBrandsByBids(List<Long> bids) {
        return brandMapper.selectByIdList(bids);
    }
}
