package com.leyou.com.leyou;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.leyou.com.leyou.utils.SmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *  监听短信验证码消息监听器
 * @author shen youjian
 * @date 2018/8/1 22:13
 */
@Component
public class SmsListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsListener.class);
    @Autowired
    private SmsUtils smsUtils;
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "ly.check.code.queue", durable = "true"),
                    exchange = @Exchange(
                            value = "ly.user.check.code.exchange",
                            type = ExchangeTypes.TOPIC,
                            ignoreDeclarationExceptions = "true"),
                    key = "user.code"
            ))
    public void checkCodeListener(Map<String, String> map) {
        if (map != null) {
            String phone = map.get("phone");
            String code = map.get("code");
            // 调用 阿里大鱼发送短信息
            if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(code)) {
                try {
                    SendSmsResponse sendSmsResponse = smsUtils.sendSms(phone, code);
                    LOGGER.info("短信发送成功. phone:{}, code:{}", phone, code);
                    LOGGER.info("code: {}",  sendSmsResponse.getCode());
                    LOGGER.info("message: {}",  sendSmsResponse.getMessage());
                    LOGGER.info("requestId: {}",  sendSmsResponse.getRequestId());
                    LOGGER.info("bizId: {}",  sendSmsResponse.getBizId());
                } catch (ClientException e) {
                    LOGGER.error("短信发送失败. phone:{}, code:{}", phone, code, e);
                }
            }
        }
    }
}
