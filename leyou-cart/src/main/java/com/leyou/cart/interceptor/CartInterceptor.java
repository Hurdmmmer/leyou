package com.leyou.cart.interceptor;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.cart.config.JwtProperties;
import com.leyou.common.utils.CookieUtils2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  购物车鉴权， 拦截器, 由于该类如果使用 @Component 注解， 该生成 Bean 对象的优先级没有
 *  注册拦截器的 Bean 高， 所以这里不能直接交由 spring 创建实例， 需要使用 注册拦截器的
 *  对象实例中创建该 拦截器的实例, 所以使用构造函数方式注入 配置属性实例
 * @author shen youjian
 * @date 2018/8/5 16:38
 */
@Slf4j
public class CartInterceptor implements HandlerInterceptor {
    /** 通过spring 构造函数注入配置属性对象实例 */
    private JwtProperties prop;
    /** 使用线程域，存放用户信息 */
    private final static ThreadLocal<UserInfo> tl = new ThreadLocal<>();
    public CartInterceptor(JwtProperties prop) {
        this.prop = prop;
    }


    /** 前置拦截器 */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String token = CookieUtils2.getCookieValue(request, prop.getCookieName());
        if (StringUtils.isBlank(token)) {
            // 没有登录则返回未授权的状态码
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, prop.getPublicKey());
            // 存放到线程域中
            tl.set(userInfo);
        } catch (Exception e) {
            log.info("用户权限异常。", e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        return true;
    }
    /** 获取存放在线程域中的数据 */
    public static UserInfo getUserInfo() {
        return tl.get();
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清空线程域中的数据， 防止内存溢出
        tl.remove();
    }
}
