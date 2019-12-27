/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
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

import java.net.InetAddress;
import java.util.Collection;

import org.apache.http.HttpHost;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * TODO
 * @author ： <a href="https://github.com/hiwepy">hiwepy</a>
 */
@ConfigurationProperties(prefix = HttpClientRequestProperties.PREFIX)
public class HttpClientRequestProperties {

	public final static String PREFIX = "httpclient.request";

	private boolean expectContinueEnabled;
	private HttpHost proxy;
	private InetAddress localAddress;
	private boolean staleConnectionCheckEnabled;
	private String cookieSpec;
	private boolean redirectsEnabled;
	private boolean relativeRedirectsAllowed;
	private boolean circularRedirectsAllowed;
	private int maxRedirects;
	private boolean authenticationEnabled;
	private Collection<String> targetPreferredAuthSchemes;
	private Collection<String> proxyPreferredAuthSchemes;
	private int connectionRequestTimeout;
	/**
	 * 连接超时时间(单位毫秒)
	 */
	private int connectTimeout;
	private int socketTimeout;
	private boolean contentCompressionEnabled;
	/**
	 * http请求失败重试次数
	 */
	protected int retryCount = Integer.parseInt(HttpClientParams.HTTP_CONNECTION_RETRY_TIME.getDefault());
	protected boolean requestSentRetryEnabled = true;
	
	public boolean isExpectContinueEnabled() {
		return expectContinueEnabled;
	}

	public void setExpectContinueEnabled(boolean expectContinueEnabled) {
		this.expectContinueEnabled = expectContinueEnabled;
	}

	public HttpHost getProxy() {
		return proxy;
	}

	public void setProxy(HttpHost proxy) {
		this.proxy = proxy;
	}

	public InetAddress getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(InetAddress localAddress) {
		this.localAddress = localAddress;
	}

	public boolean isStaleConnectionCheckEnabled() {
		return staleConnectionCheckEnabled;
	}

	public void setStaleConnectionCheckEnabled(boolean staleConnectionCheckEnabled) {
		this.staleConnectionCheckEnabled = staleConnectionCheckEnabled;
	}

	public String getCookieSpec() {
		return cookieSpec;
	}

	public void setCookieSpec(String cookieSpec) {
		this.cookieSpec = cookieSpec;
	}

	public boolean isRedirectsEnabled() {
		return redirectsEnabled;
	}

	public void setRedirectsEnabled(boolean redirectsEnabled) {
		this.redirectsEnabled = redirectsEnabled;
	}

	public boolean isRelativeRedirectsAllowed() {
		return relativeRedirectsAllowed;
	}

	public void setRelativeRedirectsAllowed(boolean relativeRedirectsAllowed) {
		this.relativeRedirectsAllowed = relativeRedirectsAllowed;
	}

	public boolean isCircularRedirectsAllowed() {
		return circularRedirectsAllowed;
	}

	public void setCircularRedirectsAllowed(boolean circularRedirectsAllowed) {
		this.circularRedirectsAllowed = circularRedirectsAllowed;
	}

	public int getMaxRedirects() {
		return maxRedirects;
	}

	public void setMaxRedirects(int maxRedirects) {
		this.maxRedirects = maxRedirects;
	}

	public boolean isAuthenticationEnabled() {
		return authenticationEnabled;
	}

	public void setAuthenticationEnabled(boolean authenticationEnabled) {
		this.authenticationEnabled = authenticationEnabled;
	}

	public Collection<String> getTargetPreferredAuthSchemes() {
		return targetPreferredAuthSchemes;
	}

	public void setTargetPreferredAuthSchemes(Collection<String> targetPreferredAuthSchemes) {
		this.targetPreferredAuthSchemes = targetPreferredAuthSchemes;
	}

	public Collection<String> getProxyPreferredAuthSchemes() {
		return proxyPreferredAuthSchemes;
	}

	public void setProxyPreferredAuthSchemes(Collection<String> proxyPreferredAuthSchemes) {
		this.proxyPreferredAuthSchemes = proxyPreferredAuthSchemes;
	}

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

	public boolean isContentCompressionEnabled() {
		return contentCompressionEnabled;
	}

	public void setContentCompressionEnabled(boolean contentCompressionEnabled) {
		this.contentCompressionEnabled = contentCompressionEnabled;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public boolean isRequestSentRetryEnabled() {
		return requestSentRetryEnabled;
	}

	public void setRequestSentRetryEnabled(boolean requestSentRetryEnabled) {
		this.requestSentRetryEnabled = requestSentRetryEnabled;
	}

}
