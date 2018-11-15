package com.leyou.respository;

import com.leyou.user.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 *
 *  使用 elasticsearch 提供的 spring data 实现的方法实现简单的 crud
 * @author shen youjian
 * @date 2018/7/26 20:57
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {

}
