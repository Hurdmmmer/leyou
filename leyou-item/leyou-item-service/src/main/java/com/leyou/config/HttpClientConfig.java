package com.leyou.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @author shen youjian
 * @date 2018/7/22 19:22
 */
@Configuration
@EnableConfigurationProperties(HttpClientProperties.class)
public class HttpClientConfig {


    @Bean
    public CloseableHttpClient closeableHttpClient(@Qualifier("httpClientConnectionManager")PoolingHttpClientConnectionManager manager,
                                                   @Qualifier("requestConfig") RequestConfig requestConfig) {
        return HttpClients.custom().setConnectionManager(manager).setDefaultRequestConfig(requestConfig).
                setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)).build();
    }

    @Bean(name = "socketFactoryRegistry")
    public Registry socketFactoryRegistry() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());

        return RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();
    }


    @Bean(name = "httpClientConnectionManager")
    public PoolingHttpClientConnectionManager clientConnectionManager(
                        @Qualifier("socketFactoryRegistry") Registry<ConnectionSocketFactory> socketFactoryRegistry,
                        HttpClientProperties httpClientProperties) {

        PoolingHttpClientConnectionManager poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // 将最大连接数增加到200，实际项目最好从配置文件中读取这个值
        poolConnManager.setMaxTotal(httpClientProperties.getMaxTotal());
        // 设置最大路由(主机地址的并发数量)
        poolConnManager.setDefaultMaxPerRoute(httpClientProperties.getMaxPerRoute());

        return poolConnManager;
    }

    @Bean (name = "requestConfig")
    public RequestConfig requestConfig(HttpClientProperties httpClientProperties) {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(httpClientProperties.getConnectionRequestTimeout())
                .setSocketTimeout(httpClientProperties.getSocketTimeout())
                .setConnectTimeout(httpClientProperties.getConnectTimeout()).build();
    }
}
