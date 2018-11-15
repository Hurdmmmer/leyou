package com.leyou.order.interseptor;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils2;
import com.leyou.order.config.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 订单系统的鉴权拦截器
 *
 * @author shen youjian
 * @date 2018/8/6 13:45
 */
@Slf4j
public class OrderInterceptor implements HandlerInterceptor {

    private JwtProperties prop;
    private static final ThreadLocal<UserInfo> tl = new ThreadLocal<>();

    /**
     * 构造函数注入属性
     */
    public OrderInterceptor(JwtProperties prop) {
        this.prop = prop;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = CookieUtils2.getCookieValue(request, prop.getCookieName());

        if (StringUtils.isBlank(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        try {
            UserInfo info = JwtUtils.getInfoFromToken(token, prop.getPublicKey());
            tl.set(info);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("token 数据非法篡改. {}", getIp2(request), e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        return false;
    }

    public static UserInfo getUserInfo() {
        return tl.get();
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 处理请求完成后， 需要销毁当前线程中的存放的数据
        tl.remove();
    }

    /** 获取客服端的IP */
    private static String getIp2(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }
}
