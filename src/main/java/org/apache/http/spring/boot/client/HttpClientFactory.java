/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.http.spring.boot.client;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.spring.boot.client.utils.HttpClientMetricUtils;

public class HttpClientFactory {

	/**
	 * 普通http请求的HttpClient连接池
	 */
	private HttpClientConnectionManager httpConnectionManager = null;

	private ConnectionKeepAliveStrategy keepAliveStrat = null;

	private HttpRequestRetryHandler requestRetryHandler = null;

	private HttpRequestInterceptor summaryInterceptor;

	private HttpRequestInterceptor headerInterceptor;

	private HttpRequestInterceptor gzipInterceptor;

	private HttpResponseInterceptor ungzipInterceptor;

	// 默认Socket参数
	private SocketConfig defaultSocketConfig = null;
	// 默认请求和传输超时时间
	private RequestConfig defaultRequestConfig = null;

	private boolean userManager = true;

	public HttpClientFactory(HttpClientConnectionManager httpConnectionManager,
			ConnectionKeepAliveStrategy keepAliveStrat, HttpRequestRetryHandler requestRetryHandler) {
		this(httpConnectionManager, keepAliveStrat, requestRetryHandler, true);
	}

	public HttpClientFactory(HttpClientConnectionManager httpConnectionManager,
			ConnectionKeepAliveStrategy keepAliveStrat, HttpRequestRetryHandler requestRetryHandler,
			boolean userManager) {
		this.httpConnectionManager = httpConnectionManager;
		this.keepAliveStrat = keepAliveStrat;
		this.requestRetryHandler = requestRetryHandler;
		this.userManager = userManager;
	}

	/**
	 * @description ： 创建默认的httpClient实例.优先使用连接池对象，如果连接池对象使用过程出现异常，则使用非安全连接
	 * @author ： <a href="https://github.com/vindell">vindell</a>
	 * @date ：2017年12月3日 下午10:41:46
	 * @param connectionManager
	 * @return
	 */
	public CloseableHttpClient getCloseableHttpClient(HttpClientConnectionManager connectionManager) {

		try {

			// 创建忽略任何安全校验的httpClient实例.
			HttpClientBuilder clientBuilder = HttpClients.custom()
					// 添加度量监控
					.setRequestExecutor(HttpClientMetricUtils.getHttpRequestExecutor(null))
					.setKeepAliveStrategy(keepAliveStrat).setRetryHandler(requestRetryHandler)
					// 设置相关的压缩文件标识，在请求头的信息中
					.addInterceptorFirst(headerInterceptor).addInterceptorFirst(gzipInterceptor)
					.addInterceptorLast(summaryInterceptor)
					// 设置相应相应的拦截器，用于处理接收到的拦截的压缩信息
					.addInterceptorLast(ungzipInterceptor);

			/**
			 * --------------以下设置为客户端级别，作为所有请求的默认值：-------------------------------------------------------------------------
			 */

			clientBuilder.setDefaultRequestConfig(defaultRequestConfig).setDefaultSocketConfig(defaultSocketConfig);

			// 设置链接管理池
			if (connectionManager != null && isUserManager()) {
				clientBuilder.setConnectionManager(connectionManager);
			}
			return clientBuilder.build();
		} catch (Exception e) {
			return HttpClients.createDefault();
		}
	}

	public HttpClientConnectionManager getHttpConnectionManager() {
		return httpConnectionManager;
	}

	public void setHttpConnectionManager(HttpClientConnectionManager httpConnectionManager) {
		this.httpConnectionManager = httpConnectionManager;
	}

	public ConnectionKeepAliveStrategy getKeepAliveStrat() {
		return keepAliveStrat;
	}

	public void setKeepAliveStrat(ConnectionKeepAliveStrategy keepAliveStrat) {
		this.keepAliveStrat = keepAliveStrat;
	}

	public HttpRequestRetryHandler getRequestRetryHandler() {
		return requestRetryHandler;
	}

	public void setRequestRetryHandler(HttpRequestRetryHandler requestRetryHandler) {
		this.requestRetryHandler = requestRetryHandler;
	}

	public HttpRequestInterceptor getSummaryInterceptor() {
		return summaryInterceptor;
	}

	public void setSummaryInterceptor(HttpRequestInterceptor summaryInterceptor) {
		this.summaryInterceptor = summaryInterceptor;
	}

	public HttpRequestInterceptor getHeaderInterceptor() {
		return headerInterceptor;
	}

	public void setHeaderInterceptor(HttpRequestInterceptor headerInterceptor) {
		this.headerInterceptor = headerInterceptor;
	}

	public HttpRequestInterceptor getGzipInterceptor() {
		return gzipInterceptor;
	}

	public void setGzipInterceptor(HttpRequestInterceptor gzipInterceptor) {
		this.gzipInterceptor = gzipInterceptor;
	}

	public HttpResponseInterceptor getUngzipInterceptor() {
		return ungzipInterceptor;
	}

	public void setUngzipInterceptor(HttpResponseInterceptor ungzipInterceptor) {
		this.ungzipInterceptor = ungzipInterceptor;
	}

	public SocketConfig getDefaultSocketConfig() {
		return defaultSocketConfig;
	}

	public void setDefaultSocketConfig(SocketConfig defaultSocketConfig) {
		this.defaultSocketConfig = defaultSocketConfig;
	}

	public RequestConfig getDefaultRequestConfig() {
		return defaultRequestConfig;
	}

	public void setDefaultRequestConfig(RequestConfig defaultRequestConfig) {
		this.defaultRequestConfig = defaultRequestConfig;
	}

	public boolean isUserManager() {
		return userManager;
	}

	public void setUserManager(boolean userManager) {
		this.userManager = userManager;
	}

}
