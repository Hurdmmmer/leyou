package com.leyou.filter;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.auth.utils.RsaUtils;
import com.leyou.common.utils.CookieUtils2;
import com.leyou.config.FilterProperties;
import com.leyou.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *  zuul 的鉴权过滤器, 如果没有权限则不允许访问微服务
 * @author shen youjian
 * @date 2018/8/4 16:25
 */
@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
@Slf4j
public class AuthFilter extends ZuulFilter {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private FilterProperties filterProperties;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
    }

    @Override
    public boolean shouldFilter() {
        // 一些不需要权限的路径可以不需要过滤直接访问
        return !isAllowPath(filterProperties.getAllowPaths());
    }
    /** 根据配置文件设置没有访问权限可以访问的路径 */
    private boolean isAllowPath(List<String> allowPaths) {
        RequestContext ctx = RequestContext.getCurrentContext();
        String requestURI = ctx.getRequest().getRequestURI();
        log.info("访问的微服务路径是: {}", requestURI);
        for (String allowPath : allowPaths) {
            log.info("允许访问的路径是: {}", allowPath);

            if (requestURI.startsWith(allowPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        // 1. 获取当前会话请求的context
        RequestContext ctx = RequestContext.getCurrentContext();
        // 2. 获取 request 请求
        HttpServletRequest request = ctx.getRequest();
        // 获取权限 cookie
        log.info("zuul 网关开始鉴权");
        String token = CookieUtils2.getCookieValue(request, jwtProperties.getCookieName());
        try {
            if (StringUtils.isBlank(token)) {
                log.info("该访问没有权限");
                ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
                ctx.setSendZuulResponse(false);
            }
            JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            log.info("鉴权成功");
        } catch (Exception e) {
            e.printStackTrace();
            // 返回错误码, 并禁止继续访问
            log.info("该访问非法篡改");
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            ctx.setSendZuulResponse(false);
        }

        return null;
    }
}
