package com.leyou.cart.controller;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.ResourceBundle;

/**
 *  购物车控制器， 用户登录的情况下， 把购物车的信息存储到 redis 缓存中
 * @author shen youjian
 * @date 2018/8/5 16:37
 */
@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("list")
    public ResponseEntity<List<Cart>> queryCarts(@RequestBody(required = false) List<Cart> carts) {

        try {
            carts = cartService.queryAndSaveCart(carts);
            if (carts == null ){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            return ResponseEntity.ok(carts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart) {
        if (cart == null ||cart.getNum() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        boolean result = cartService.addCart(cart);
        if (!result) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
