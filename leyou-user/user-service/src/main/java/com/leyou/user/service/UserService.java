package com.leyou.user.service;

import com.leyou.common.utils.NumberUtils;
import com.leyou.pojo.User;
import com.leyou.user.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * user 业务层
 *
 * @author shen youjian
 * @date 2018/8/1 20:26
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * 存放在 redis 中的key值, 用于保存 验证码的key
     */
    @Autowired
    private static final String KEY_PREFIX = "user:code:phone:";

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    /**
     * 检查数据是否存在 根据指定的类型
     *
     * @param data 被检查的数据
     * @param type 1: 用户, 2: 手机, 3: 邮箱
     * @return true 表示可用, false 表示不可用
     */
    public Boolean checkDataByType(String data, Integer type) {

        User record = new User();
        switch (type) {
            case 1:
                record.setUserName(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
                return null;
        }
        return userMapper.selectCount(record) == 0;
    }

    /**
     * 调用短信服务, 发送验证码到指定的手机号码
     *
     * @param phone 手机号码
     */
    public Boolean sendCheckCode(String phone) {
        String checkCode = NumberUtils.generateCode(6);
        // 调用短信服务发送短信, 忘消息队列中发送信息
        try {
            sendSMSMQ(phone, checkCode);
            // 把验证码存储到 redis, redis 并设置该数据存放有效期为 5 分钟
            redisTemplate.opsForValue().set(KEY_PREFIX + phone, checkCode, 5, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("发送验证消息失败. phone:{}, code:{}", phone, checkCode, e);
        }
        return false;
    }

    /**
     * 发送消息通知 短信服务发送验证码信息
     */
    private void sendSMSMQ(String phone, String code) {
        Map<String, String> msg = new HashMap<>();
        msg.put("phone", phone);
        msg.put("code", code);
        // exchange, routing key 都是在配置文件中配置
        amqpTemplate.convertAndSend(msg);
    }
}
