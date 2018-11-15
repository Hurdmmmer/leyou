package com.leyou.cart.service;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.cart.interceptor.CartInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.utils.JsonUtils;
import net.minidev.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车业务层
 *
 * @author shen youjian
 * @date 2018/8/5 21:16
 */
@Service
public class CartService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String key_prefix = "ly:cart:key:";

    /**
     * 获取原有的数据并合并成一个购物车
     */
    public List<Cart> queryAndSaveCart(List<Cart> carts) {

        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key_prefix + CartInterceptor.getUserInfo().getId());

        for (Cart cart : carts) {
            String hashKey = cart.getSkuId().toString();
            Integer num = cart.getNum();
            if (hashOps.hasKey(hashKey)) {
                // 如果redis 中存在对象， 则先取出对象， 增加商品的数量
                cart = JsonUtils.parse(hashOps.get(hashKey).toString(), Cart.class);
                cart.setNum(num);
            }else {
                //如果不存在该商品， 则设置该商品属于哪一个用户
                cart.setUserId(CartInterceptor.getUserInfo().getId());
            }
            hashOps.put(hashKey, JsonUtils.serialize(cart));
        }
        // 取出所有的数据并强转成 cart
        List<Object> values = hashOps.values();

        carts = values.stream().map(obj -> JsonUtils.parse((String) obj, Cart.class)).collect(Collectors.toList());

        return carts;
    }

    /** 添加购物车 */
    public boolean addCart(Cart cart) {

        String key = key_prefix + CartInterceptor.getUserInfo().getId();

        try {
            BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(key);
            if (ops.hasKey(cart.getSkuId().toString())) {
                Integer num = cart.getNum();
                cart = JsonUtils.parse((String) ops.get(cart.getSkuId().toString()), Cart.class);
                cart.setNum(num + cart.getNum());
            } else {
                cart.setUserId(CartInterceptor.getUserInfo().getId());
            }

            ops.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
