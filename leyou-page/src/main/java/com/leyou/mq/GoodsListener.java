package com.leyou.mq;

import com.leyou.service.ItemPageService;
import com.leyou.service.StaticPageService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *  监听商品修改后修改静态页面
 * @author shen youjian
 * @date 2018/8/1 16:45
 */
@Component
public class GoodsListener {

    @Autowired
    private StaticPageService staticPageService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "ly.create.page.queue", durable = "true"),
            exchange = @Exchange(
                    value = "ly.item.exchange", type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true"
            ),
            key = {"item.insert", "item.update"}
    ))
    public void createItemListener(Long id) {
        if (id != null) {
            staticPageService.syncCreateHtml(id);
        }
    }

    // 删除静态页面
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "ly.delete.page.queue", durable = "true"),
                    exchange = @Exchange(value = "ly.item.exchange", type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true"),
                    key = "item.delete"
            ))
    public void deleteItemListener(Long id) {
        if (id !=null) {
            staticPageService.deleteHtml(id);
        }
    }

}
