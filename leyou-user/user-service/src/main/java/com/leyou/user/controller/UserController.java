package com.leyou.user.controller;

import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * 用户注册相关的控制器
 *
 * @author shen youjian
 * @date 2018/8/1 20:19
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

//    参数  	说明                      	        是否必须	数据类型   	   默认值
//    data	要校验的数据                  	            是   	String  	无
//    type	要校验的数据类型：1，用户名；2，手机；3，邮箱	否   	Integer	    1

    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkData(
            @PathVariable("data") String data, @PathVariable(value = "type", required = false) Integer type) {
        if (type == null) {
            type = 1;
        }
        // todo 需要根据类型进行正则表达式匹配
        logger.info(data + " 数据开始校验唯一性");
        try {
            Boolean result = userService.checkDataByType(data, type);
            if (result == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            logger.info("数据校验完成 data: {}", data);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("校验数据唯一失败: data: {}", data, e);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }


    /**
     * 根据手机号码发送验证码
     */
    @PostMapping("phone")
    public ResponseEntity<Void> sendCheckCode(@RequestParam("phone") String phone) {
        if ("^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8])|(19[7]))\\\\d{8}$".matches(phone)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            Boolean result = userService.sendCheckCode(phone);
            if (result) {
                logger.info("短信发送成功. phone:{}", phone);
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("短信发送失败, 短信服务出现异常", e);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    /** 用户注册
     *  后台校验用户数据格式是否正确, 验证码是否输入正确
     * */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code") String code) {
        try {
            boolean result = userService.register(user, code);
            if (!result) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     *  根据用户名, 密码查询用户
     * */
    @GetMapping("query")
    public ResponseEntity<User> queryUserByUsernameAndPassword(@RequestParam("username")String username,
                                                               @RequestParam("password")String password) {
        try {
            User user = userService.queryUserByUsernameAndPassword(username, password);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
