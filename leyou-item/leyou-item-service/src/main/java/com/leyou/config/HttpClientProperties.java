package com.leyou.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author shen youjian
 * @date 2018/7/22 20:46
 */
@ConfigurationProperties(prefix = "http-client")
public class HttpClientProperties {

    private int connectionRequestTimeout;
    private int connectTimeout;
    private int socketTimeout;
    private int maxPerRoute;
    private int maxTotal;

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getMaxPerRoute() {
        return maxPerRoute;
    }

    public void setMaxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }
}
