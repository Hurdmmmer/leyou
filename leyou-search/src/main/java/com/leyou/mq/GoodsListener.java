package com.leyou.mq;

import com.leyou.client.GoodsClient;
import com.leyou.pojo.Goods;
import com.leyou.pojo.Spu;
import com.leyou.respository.GoodsRepository;
import com.leyou.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *  用于监听, RabbitMQ 中消息
 * @author shen youjian
 * @date 2018/8/1 15:20
 */
@Component
public class GoodsListener {
    @Autowired
    private SearchService searchService;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private GoodsRepository goodsRepository;

    /** 监听商品的修改, 新增, 因为 elasticsearch 中的修改和新增是一样的 */
    @RabbitListener(bindings = @QueueBinding(   // 绑定 queue 到交换机上
                                // 声明一个队列, 并需要持久化保存
                                value = @Queue(value = "ly.create.index.queue", durable = "true"),
                                // 绑定的 交换机
                                exchange = @Exchange(
                                        value = "ly.item.exchange",   // 交换机的名称
                                        ignoreDeclarationExceptions = "true", // 忽略声明的一些问题, 如果已经存在咋补继续声明, 防止重复声明
                                        type = ExchangeTypes.TOPIC  // 交换机类型, fanout, topic, direct
                                ),
                                // 绑定的 routing key 的路由规则, 可以是一个数组, 绑定多个 key
                                key = {"item.insert", "item.update"}
                        ))
    public void createItemListener(Long spuId) {
        if (spuId != null) {
            Spu spu = goodsClient.querySpuBySpuId(spuId);
            Goods goods = searchService.builderGoods(spu);
            goodsRepository.save(goods);
        }
    }


    /** 监听删除消息 */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ly.delete.item.queue", durable = "true"),
            exchange = @Exchange(value = "ly.item.exchange", type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true"),
            key = "item.delete"
    ))
    public void deleteItemListener(Long spuId) {
        if (spuId != null) {
            Spu spu = goodsClient.querySpuBySpuId(spuId);
            Goods goods = searchService.builderGoods(spu);

            goodsRepository.delete(goods);
        }
    }



}
