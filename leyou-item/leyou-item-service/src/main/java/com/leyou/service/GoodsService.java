package com.leyou.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.dao.SkuMapper;
import com.leyou.dao.SpuDetailMapper;
import com.leyou.dao.SpuMapper;
import com.leyou.dao.StockMapper;
import com.leyou.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 添加商品的service
 *
 * @author shen youjian
 * @date 2018/7/23 15:27
 */
@Service
public class GoodsService extends BaseService<Spu> {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private Logger logger;

    @Override
    public Mapper<Spu> getMapper() {
        return spuMapper;
    }

    /**
     * 根据条件分页查询
     */
    public PageResult<Spu> queryPageListByWhere(String key, Integer page, Integer rows, Boolean saleable) {

        PageHelper.startPage(page, rows);
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        // 是否关键字搜索
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        // 是否上下架搜索
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }
        // 排除逻辑删除的商品
        criteria.andEqualTo("valid", true);
        List<Spu> spus = this.spuMapper.selectByExample(example);
        // 还需要查询 分类, 和品牌
        for (Spu spu : spus) {

            List<Object> ids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
            List<Category> categories = categoryService.queryListByIn(ids, Category.class, "id");
            List<String> names = categories.stream().map(c -> c.getName()).collect(Collectors.toList());
            spu.setCname(StringUtils.join(names, "/"));
            // 查询品牌名称
            Brand brand = this.brandService.queryById(spu.getBrandId());
            spu.setBname(brand.getName());
        }
        PageInfo<Spu> info = new PageInfo<>(spus);

        PageResult<Spu> pageResult = new PageResult<>();
        pageResult.setTotal(info.getTotal());
        pageResult.setData(info.getList());

        return pageResult;
    }

    /**
     * 添加商品
     *
     * @param spu
     * @return
     */
    @Transactional
    public boolean saveGoods(Spu spu) {
        boolean flag = false;
        // 填充一个必须字段
        try {
            spu.setCreateTime(new Date());
            spu.setLastUpdateTime(spu.getCreateTime());
            spu.setSaleable(true);
            spu.setValid(true);

            logger.info("添加商品到 spu 表中");
            spu.setId(null);
            flag = super.insert(spu);
            if (!flag) {
                logger.info("添加商品到 spu 表中失败");
                throw new RuntimeException("添加商品 spu 失败");
            }
            logger.info("添加商品到 spu 表中成功");
            SpuDetail spuDetail = spu.getSpuDetail();
            spuDetail.setSpuId(spu.getId());

            logger.info("添加 spuDetail 到 spuDetail 表中");
            flag = spuDetailMapper.insert(spuDetail) > 0;

            if (!flag) {
                logger.info("添加商品到 SpuDetail 表中失败");
                throw new RuntimeException("添加商品 SpuDetail 失败");
            }
            List<Sku> skus = spu.getSkus();
            List<Stock> stocks = new ArrayList<>();
            for (Sku sku : skus) {
                sku.setSpuId(spu.getId());
                sku.setCreateTime(spu.getCreateTime());
                sku.setLastUpdateTime(spu.getCreateTime());
                logger.info("插入 sku 数据");
                flag = skuMapper.insert(sku) > 0;
                if (!flag) {
                    logger.info("插入 sku 失败");
                    throw new RuntimeException("批量插入 sku 失败");
                }
                // 设置库存数量
                Stock stock = new Stock();
                stock.setSkuId(sku.getId());
                stock.setStock(sku.getStock());
                stocks.add(stock);
            }
            logger.info("批量插入 Stock 数据");
            flag = stockMapper.insertList(stocks) > 0;
            if (!flag) {
                logger.info("批量插入 Stock 失败");
                throw new RuntimeException("批量插入 Stock 失败");
            }
        } catch (RuntimeException e) {
            logger.info("插入商品失败");
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 根据 id 查询
     */
    public SpuDetail querySpuDetailById(Long id) {
        return spuDetailMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据 id 查询 sku 列表
     */
    public List<Sku> querySkusBySpuId(Long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> skus = skuMapper.select(sku);
        // 查询库存
        for (Sku s : skus) {
            Stock stock = new Stock();
            stock.setSkuId(s.getId());
            Stock stock1 = stockMapper.selectByPrimaryKey(stock.getSkuId());
            s.setStock(stock1.getStock());
        }

        return skus;
    }

    /**
     * 修改商品
     */
    @Transactional
    public boolean updateGoods(Spu spu) {
        logger.info("开始修改商品");
        boolean flag = false;
        try {
            spu.setLastUpdateTime(new Date());
            flag = spuMapper.updateByPrimaryKeySelective(spu) > 0;

            if (!flag) {
                logger.info("修改 spu 失败");
                throw new RuntimeException("修改 spu 失败");
            }
            // 查询原来的 sku id 并删除
            List<Sku> skus = this.querySkusBySpuId(spu.getId());
            if (!CollectionUtils.isEmpty(skus)) {
                // 获取原来的 sku 的 id
                List<Long> skuIds = skus.stream().map(s -> s.getId()).collect(Collectors.toList());
                // 根据原来的 sku id 查询所有的库存, 然后删除库存
                logger.info("删除原来的库存");
                flag = stockMapper.deleteByIdList(skuIds) > 0;
                logger.info("删除原有 SKU 数据");

                Sku record = new Sku();
                record.setSpuId(spu.getId());
                skuMapper.delete(record);
            }
            logger.info("插入新的 sku 和 stock 的信息");
            skus = spu.getSkus();
            for (Sku sku : skus) {
                sku.setSpuId(spu.getId());
                sku.setCreateTime(spu.getCreateTime());
                sku.setLastUpdateTime(new Date());
                flag = skuMapper.insert(sku) > 0;
                if (!flag) {
                    logger.info("修改商品失败, 数据回滚");
                    throw new RuntimeException("修改 SKU 失败");
                }
                Stock stock = new Stock();
                stock.setSkuId(sku.getId());
                stock.setStock(sku.getStock());

                stockMapper.insert(stock);
            }
            logger.info("修改商品 spuDetail ");
            flag = spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail()) > 0;
            if (!flag) {
                throw new RuntimeException("修改 spuDetail 失败");
            }

        } catch (RuntimeException e) {
            logger.info("修改商品失败");
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * 根据 skuId 查询库存
     *
     * @param skuId
     * @return
     */
    public Stock queryStockBySkuId(Long skuId) {
        return this.stockMapper.selectByPrimaryKey(skuId);
    }
}
