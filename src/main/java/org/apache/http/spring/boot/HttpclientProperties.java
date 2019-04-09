package org.apache.http.spring.boot;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.apache.http.spring.boot.client.HttpClientParams;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = HttpClientProperties.PREFIX)
public class HttpClientProperties {

	public final static String PREFIX = "httpclient";

	private boolean gzip = false;

	/**
	 * 保持连接池内的长连接时长,仅在使用连接池时有效，单位毫秒，默认30秒
	 */
	private long keepAlive = Long.parseLong(HttpClientParams.HTTP_CONNECTION_KEEPALIVE.getDefault());
	/**
	 * 连接池最大持有连接数;默认 20
	 */
	private int maxPoolSize = Integer.parseInt(HttpClientParams.HTTP_CONNECTION_MAX_POOLSIZE.getDefault());

	/**
	 * Maximum number of allowed retries if the server responds with a HTTP code in
	 * our retry code list. Default value is 1.
	 */
	private int maxRetries = 1;

	/**
	 * Retry interval between subsequent requests, in milliseconds. Default value is
	 * 1 second.
	 */
	private Integer retryInterval = 1000;

	/**
	 * 最大请求行长度的限制;默认 2000
	 */
	private int maxLineLength = Integer.parseInt(HttpClientParams.HTTP_CONNECTION_MAX_LINE_LENGTH.getDefault());
	/**
	 * 允许的最大HTTP头部信息数量;默认 200
	 */
	private int maxHeaderCount = Integer.parseInt(HttpClientParams.HTTP_CONNECTION_MAX_HEADER_COUNT.getDefault());
	/**
	 * 连接字符编码格式 可用值 UTF-8，US-ASCII，ISO-8859-1;默认 UTF-8
	 */
	private String charset = HttpClientParams.HTTP_CONNECTION_CONFIG_CHARSET.getDefault();

	private boolean connManagerShared;
	private boolean systemProperties;
	private boolean redirectHandlingDisabled;
	private boolean automaticRetriesDisabled;
	private boolean contentCompressionDisabled;
	private boolean cookieManagementDisabled;
	private boolean authCachingDisabled;
	private boolean connectionStateDisabled;
	private HttpHost proxy;
	private String userAgent;
	
	private boolean bufferRequestBody = true;

	public boolean isGzip() {
		return gzip;
	}

	public boolean isConnManagerShared() {
		return connManagerShared;
	}

	public void setConnManagerShared(boolean connManagerShared) {
		this.connManagerShared = connManagerShared;
	}

	public void setGzip(boolean gzip) {
		this.gzip = gzip;
	}

	public long getKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(long keepAlive) {
		this.keepAlive = keepAlive;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	public Integer getRetryInterval() {
		return retryInterval;
	}

	public void setRetryInterval(Integer retryInterval) {
		this.retryInterval = retryInterval;
	}

	public int getMaxLineLength() {
		return maxLineLength;
	}

	public void setMaxLineLength(int maxLineLength) {
		this.maxLineLength = maxLineLength;
	}

	public int getMaxHeaderCount() {
		return maxHeaderCount;
	}

	public void setMaxHeaderCount(int maxHeaderCount) {
		this.maxHeaderCount = maxHeaderCount;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}


	public boolean isSystemProperties() {
		return systemProperties;
	}

	public void setSystemProperties(boolean systemProperties) {
		this.systemProperties = systemProperties;
	}

	public boolean isRedirectHandlingDisabled() {
		return redirectHandlingDisabled;
	}

	public void setRedirectHandlingDisabled(boolean redirectHandlingDisabled) {
		this.redirectHandlingDisabled = redirectHandlingDisabled;
	}

	public boolean isAutomaticRetriesDisabled() {
		return automaticRetriesDisabled;
	}

	public void setAutomaticRetriesDisabled(boolean automaticRetriesDisabled) {
		this.automaticRetriesDisabled = automaticRetriesDisabled;
	}

	public boolean isContentCompressionDisabled() {
		return contentCompressionDisabled;
	}

	public void setContentCompressionDisabled(boolean contentCompressionDisabled) {
		this.contentCompressionDisabled = contentCompressionDisabled;
	}

	public boolean isCookieManagementDisabled() {
		return cookieManagementDisabled;
	}

	public void setCookieManagementDisabled(boolean cookieManagementDisabled) {
		this.cookieManagementDisabled = cookieManagementDisabled;
	}

	public boolean isAuthCachingDisabled() {
		return authCachingDisabled;
	}

	public void setAuthCachingDisabled(boolean authCachingDisabled) {
		this.authCachingDisabled = authCachingDisabled;
	}

	public boolean isConnectionStateDisabled() {
		return connectionStateDisabled;
	}

	public void setConnectionStateDisabled(boolean connectionStateDisabled) {
		this.connectionStateDisabled = connectionStateDisabled;
	}

	public HttpHost getProxy() {
		return proxy;
	}

	public void setProxy(HttpHost proxy) {
		this.proxy = proxy;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public boolean isBufferRequestBody() {
		return bufferRequestBody;
	}

	public void setBufferRequestBody(boolean bufferRequestBody) {
		this.bufferRequestBody = bufferRequestBody;
	}

}
