package com.xy.util;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("httpClientPool")
public class HttpClientPool implements InitializingBean{

	private String totalConn = "200";
	private String connPerRoute = "20";
	private String maxPerRoute = "50";
	private String hostAndPort = "10.33.119.31:80";
	

	private final int socketTimeout = 5000;
	private final int connectionRequestTimeout = 5000;
	private final int connectTimeout = 5000;
	
	
	private CloseableHttpClient httpClient = null;
	
	private void init(){
	    if(hostAndPort==null)
	    	throw new RuntimeException("host and port config is null.");
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
	    // 将最大连接数增加到200
	    cm.setMaxTotal(Integer.valueOf(totalConn));
	    // 将每个路由基础的连接增加到20
	    cm.setDefaultMaxPerRoute(Integer.valueOf(connPerRoute));
	    //将目标主机的最大连接数增加到50
	    String[] hostAndPorts = hostAndPort.split(";");
	    for(String hP : hostAndPorts){
	    	String[] h_P = hP.split(":");
	    	String host = h_P[0];
	    	String port = null;
	    	if(h_P.length==1){
	    		port="80";
	    	}else{
	    		port = h_P[1];
	    	}
		    HttpHost localhost = new HttpHost(host, Integer.valueOf(port));
		    cm.setMaxPerRoute(new HttpRoute(localhost), Integer.valueOf(maxPerRoute));
	    }

	    RequestConfig requestConfig = RequestConfig.custom()
	    							.setConnectionRequestTimeout(connectionRequestTimeout)  
	    							.setConnectTimeout(connectTimeout).
	    							setSocketTimeout(socketTimeout).build();  
	    // 请求重试处理
	    HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception,
                    int executionCount, HttpContext context) {
                if (executionCount >= 5) {// 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext
                        .adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };
	    httpClient = HttpClients.custom()
	            .setConnectionManager(cm)
	            .setDefaultRequestConfig(requestConfig)
	            .setRetryHandler(httpRequestRetryHandler)
	            .build();
	}
	
	public HttpClient getHttpClient(){
		return httpClient;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}
}
