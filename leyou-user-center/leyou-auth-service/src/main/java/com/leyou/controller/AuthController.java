package com.leyou.controller;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils2;
import com.leyou.config.JwtProperties;
import com.leyou.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  给登陆用户授权,
 * @author shen youjian
 * @date 2018/8/2 18:13
 */
@Controller
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtProperties jwtProperties;

    /** 给用户请求建立访问权限 */
    @PostMapping("accredit")
    public ResponseEntity<Void> authentication(
            @RequestParam("username")String username,
            @RequestParam("password")String password,
            HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            String token = authService.authentication(username, password);
            if (token == null) {
                // 返回未授权的状态码
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            // 需要把 token 存放到 cookie 中
            CookieUtils2.newBuilder(request, response)
                    .cookieMaxAge(jwtProperties.getCookieMaxAge())
                    .httpOnly().build(jwtProperties.getCookieName(), token);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(), jwtProperties.getCookieName())) {
                    token = cookie.getValue();
                }
            }
        }
        if (StringUtils.isBlank(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        // 鉴定权限
        try {
            UserInfo info = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            // 鉴权后需要刷新 token 的时间限制, 重新写入 cookie
            // 重新生成token
            token = JwtUtils.generateToken(info, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
            CookieUtils2.newBuilder(request, response)
                    .cookieMaxAge(jwtProperties.getCookieMaxAge())
                    .httpOnly()
                    .build(jwtProperties.getCookieName(), token);

            return ResponseEntity.status(HttpStatus.OK).body(info);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
