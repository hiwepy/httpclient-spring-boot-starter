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
 * Configuration properties for HttpClient requests, bound to the {@code httpclient.request.*} prefix.
 * <p>Maps directly to {@link org.apache.http.client.config.RequestConfig} plus retry-related settings
 * consumed by the configured {@link org.apache.http.client.HttpRequestRetryHandler}.</p>
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
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
	/** Connection timeout in milliseconds. */
	private int connectTimeout;
	private int socketTimeout;
	private boolean contentCompressionEnabled;
	/** Number of retries on HTTP request failure. */
	protected int retryCount = Integer.parseInt(HttpClientParams.HTTP_CONNECTION_RETRY_TIME.getDefault());
	protected boolean requestSentRetryEnabled = true;
	/**
	 * <p>Is expect continue enabled.</p>
	 * @return the boolean
	 */
	
	public boolean isExpectContinueEnabled() {
		return expectContinueEnabled;
	}
	/** Sets the expect continue enabled. */

	public void setExpectContinueEnabled(boolean expectContinueEnabled) {
		this.expectContinueEnabled = expectContinueEnabled;
	}
	/** Gets the proxy. */

	public HttpHost getProxy() {
		return proxy;
	}
	/** Sets the proxy. */

	public void setProxy(HttpHost proxy) {
		this.proxy = proxy;
	}
	/** Gets the local address. */

	public InetAddress getLocalAddress() {
		return localAddress;
	}
	/** Sets the local address. */

	public void setLocalAddress(InetAddress localAddress) {
		this.localAddress = localAddress;
	}
	/**
	 * <p>Is stale connection check enabled.</p>
	 * @return the boolean
	 */

	public boolean isStaleConnectionCheckEnabled() {
		return staleConnectionCheckEnabled;
	}
	/** Sets the stale connection check enabled. */

	public void setStaleConnectionCheckEnabled(boolean staleConnectionCheckEnabled) {
		this.staleConnectionCheckEnabled = staleConnectionCheckEnabled;
	}
	/** Gets the cookie spec. */

	public String getCookieSpec() {
		return cookieSpec;
	}
	/** Sets the cookie spec. */

	public void setCookieSpec(String cookieSpec) {
		this.cookieSpec = cookieSpec;
	}
	/**
	 * <p>Is redirects enabled.</p>
	 * @return the boolean
	 */

	public boolean isRedirectsEnabled() {
		return redirectsEnabled;
	}
	/** Sets the redirects enabled. */

	public void setRedirectsEnabled(boolean redirectsEnabled) {
		this.redirectsEnabled = redirectsEnabled;
	}
	/**
	 * <p>Is relative redirects allowed.</p>
	 * @return the boolean
	 */

	public boolean isRelativeRedirectsAllowed() {
		return relativeRedirectsAllowed;
	}
	/** Sets the relative redirects allowed. */

	public void setRelativeRedirectsAllowed(boolean relativeRedirectsAllowed) {
		this.relativeRedirectsAllowed = relativeRedirectsAllowed;
	}
	/**
	 * <p>Is circular redirects allowed.</p>
	 * @return the boolean
	 */

	public boolean isCircularRedirectsAllowed() {
		return circularRedirectsAllowed;
	}
	/** Sets the circular redirects allowed. */

	public void setCircularRedirectsAllowed(boolean circularRedirectsAllowed) {
		this.circularRedirectsAllowed = circularRedirectsAllowed;
	}
	/** Gets the max redirects. */

	public int getMaxRedirects() {
		return maxRedirects;
	}
	/** Sets the max redirects. */

	public void setMaxRedirects(int maxRedirects) {
		this.maxRedirects = maxRedirects;
	}
	/**
	 * <p>Is authentication enabled.</p>
	 * @return the boolean
	 */

	public boolean isAuthenticationEnabled() {
		return authenticationEnabled;
	}
	/** Sets the authentication enabled. */

	public void setAuthenticationEnabled(boolean authenticationEnabled) {
		this.authenticationEnabled = authenticationEnabled;
	}
	/** Gets the target preferred auth schemes. */

	public Collection<String> getTargetPreferredAuthSchemes() {
		return targetPreferredAuthSchemes;
	}
	/** Sets the target preferred auth schemes. */

	public void setTargetPreferredAuthSchemes(Collection<String> targetPreferredAuthSchemes) {
		this.targetPreferredAuthSchemes = targetPreferredAuthSchemes;
	}
	/** Gets the proxy preferred auth schemes. */

	public Collection<String> getProxyPreferredAuthSchemes() {
		return proxyPreferredAuthSchemes;
	}
	/** Sets the proxy preferred auth schemes. */

	public void setProxyPreferredAuthSchemes(Collection<String> proxyPreferredAuthSchemes) {
		this.proxyPreferredAuthSchemes = proxyPreferredAuthSchemes;
	}
	/** Gets the connection request timeout. */

	public int getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}
	/** Sets the connection request timeout. */

	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}
	/** Gets the connect timeout. */

	public int getConnectTimeout() {
		return connectTimeout;
	}
	/** Sets the connect timeout. */

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	/** Gets the socket timeout. */

	public int getSocketTimeout() {
		return socketTimeout;
	}
	/** Sets the socket timeout. */

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}
	/**
	 * <p>Is content compression enabled.</p>
	 * @return the boolean
	 */

	public boolean isContentCompressionEnabled() {
		return contentCompressionEnabled;
	}
	/** Sets the content compression enabled. */

	public void setContentCompressionEnabled(boolean contentCompressionEnabled) {
		this.contentCompressionEnabled = contentCompressionEnabled;
	}
	/** Gets the retry count. */

	public int getRetryCount() {
		return retryCount;
	}
	/** Sets the retry count. */

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}
	/**
	 * <p>Is request sent retry enabled.</p>
	 * @return the boolean
	 */

	public boolean isRequestSentRetryEnabled() {
		return requestSentRetryEnabled;
	}
	/** Sets the request sent retry enabled. */

	public void setRequestSentRetryEnabled(boolean requestSentRetryEnabled) {
		this.requestSentRetryEnabled = requestSentRetryEnabled;
	}

}
