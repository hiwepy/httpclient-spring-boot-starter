package org.apache.http.spring.boot;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.spring.boot.client.HttpClientFactory;
import org.apache.http.spring.boot.client.connection.HttpClientConnectionManagerBuilder;
import org.apache.http.spring.boot.client.handler.ConnectionKeepAliveStrategyHandler;
import org.apache.http.spring.boot.client.handler.HttpRequestExceptionRetryHandler;
import org.apache.http.spring.boot.client.interceptor.HttpRequestGzipInterceptor;
import org.apache.http.spring.boot.client.interceptor.HttpRequestHeaderInterceptor;
import org.apache.http.spring.boot.client.interceptor.HttpRequestSummaryInterceptor;
import org.apache.http.spring.boot.client.interceptor.HttpResponseGzipInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({ CloseableHttpClient.class })
@EnableConfigurationProperties(HttpclientProperties.class)
public class HttpclientAutoConfiguration {

	@Bean
	public HttpClientConnectionManagerBuilder connectionManagerBuilder(HttpclientProperties properties) {
		return HttpClientConnectionManagerBuilder.getInstance(properties);
	}

	/**
	 * 普通http请求的HttpClient连接池
	 */
	@Bean
	public HttpClientConnectionManager httpConnectionManager(HttpclientProperties properties,
			HttpClientConnectionManagerBuilder builder) {

		return builder.getHttpConnectionManager();

	}
	
	/*keepAliveStrat = new ConnectionKeepAliveStrategyHandler(config.isUserManager(), config.getKeepAlive(), config.getHostProperties());
	requestRetryHandler = new HttpRequestExceptionRetryHandler(config.getRetryTime());
	
	if(config.isUserManager()){
		httpConnectionManager =  HttpClientConnectionManagerBuilder.getInstance(config).getHttpConnectionManager();
	}*/
	

	@Bean
	public SocketConfig defaultSocketConfig(HttpclientProperties properties) {

		// 创建默认Socket参数
		SocketConfig socketConfig = null;

		try {
			// 创建默认Socket参数
			socketConfig = SocketConfig.custom()
					// nagle算法默认是打开的，会引起delay的问题；所以要手工关掉
					.setTcpNoDelay(properties.isTcpNoDelay())
					// 设置读数据超时时间(单位毫秒)
					.setSoTimeout(properties.getSoTimeout()).build();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return socketConfig;
	}

	@Bean
	public RequestConfig defaultRequestConfig(HttpclientProperties properties) {
		// 设置请求和传输超时时间
		RequestConfig requestConfig = null;
		try {

			// 设置请求和传输超时时间
			requestConfig = RequestConfig.custom()
					// Socket读数据的超时时间
					.setSocketTimeout(properties.getSocketTimeout())
					// 设置连接超时时间(单位毫秒)
					.setConnectTimeout(properties.getConnectTimeout())
					.setConnectionRequestTimeout(properties.getConnectTimeout()).setExpectContinueEnabled(false)
					.build();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestConfig;
	}

	@Bean
	public ConnectionKeepAliveStrategy keepAliveStrat(HttpclientProperties properties) {

		ConnectionKeepAliveStrategy keepAliveStrat = new ConnectionKeepAliveStrategyHandler(properties.isUserManager(),
				properties.getKeepAlive(), properties.getHostProperties());

		return keepAliveStrat;
	}

	@Bean
	public HttpRequestRetryHandler requestRetryHandler(HttpclientProperties properties) {

		HttpRequestRetryHandler requestRetryHandler = new HttpRequestExceptionRetryHandler(properties.getRetryTime());

		return requestRetryHandler;
	}

	@Bean
	public HttpRequestInterceptor summaryInterceptor(HttpclientProperties properties) {

		HttpRequestInterceptor summaryInterceptor = new HttpRequestSummaryInterceptor();

		return summaryInterceptor;
	}

	@Bean
	public HttpRequestInterceptor headerInterceptor(HttpclientProperties properties) {

		HttpRequestInterceptor headerInterceptor = new HttpRequestHeaderInterceptor(properties.getHeaderProperties());

		return headerInterceptor;
	}

	@Bean
	public HttpRequestInterceptor gzipInterceptor(HttpclientProperties properties) {

		HttpRequestInterceptor gzipInterceptor = new HttpRequestGzipInterceptor();

		return gzipInterceptor;
	}

	@Bean
	public HttpResponseInterceptor ungzipInterceptor(HttpclientProperties properties) {

		HttpResponseInterceptor ungzipInterceptor = new HttpResponseGzipInterceptor();

		return ungzipInterceptor;
	}
	
	@Bean
	public HttpClientFactory httpClientFactory(HttpclientProperties properties,HttpClientConnectionManager httpConnectionManager,
			ConnectionKeepAliveStrategy keepAliveStrat, HttpRequestRetryHandler requestRetryHandler) {

		HttpClientFactory httpClientFactory = new HttpClientFactory(httpConnectionManager, keepAliveStrat, requestRetryHandler, properties.isUserManager());

		return httpClientFactory;
	}
	
	
	/*
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
	
		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
				.loadTrustMaterial(null, acceptingTrustStrategy)
				.build();
	
		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
	
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
	*/
	

}
