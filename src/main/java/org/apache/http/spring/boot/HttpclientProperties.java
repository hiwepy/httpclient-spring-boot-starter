package org.apache.http.spring.boot;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.apache.http.spring.boot.client.HttpClientParams;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the HttpClient Spring Boot starter, bound to the {@code httpclient.*} prefix.
 * <p>Controls GZIP handling, keep-alive duration, connection pool sizing, retry behaviour, request line
 * and header limits, character encoding, proxy, user-agent, and a set of feature toggles that map directly
 * to {@link org.apache.http.impl.client.HttpClientBuilder} disable flags.</p>
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = HttpClientProperties.PREFIX)
public class HttpClientProperties {

	public final static String PREFIX = "httpclient";

	private boolean gzip = false;

	/** Duration to keep pooled connections alive, in milliseconds; only effective when using a connection pool. Default is 30 seconds. */
	private long keepAlive = Long.parseLong(HttpClientParams.HTTP_CONNECTION_KEEPALIVE.getDefault());
	/** Maximum number of connections held by the connection pool; default is 20. */
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

	/** Maximum length limit of the HTTP request line; default is 2000. */
	private int maxLineLength = Integer.parseInt(HttpClientParams.HTTP_CONNECTION_MAX_LINE_LENGTH.getDefault());
	/** Maximum allowed number of HTTP headers; default is 200. */
	private int maxHeaderCount = Integer.parseInt(HttpClientParams.HTTP_CONNECTION_MAX_HEADER_COUNT.getDefault());
	/** Connection character encoding; allowed values are UTF-8, US-ASCII, ISO-8859-1; default is UTF-8. */
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

	/** Return whether GZIP request compression is enabled. @return true if gzip is enabled */
	public boolean isGzip() {
		return gzip;
	}

	/** Return whether the connection manager is shared across HttpClient instances. @return true if shared */
	public boolean isConnManagerShared() {
		return connManagerShared;
	}

	/** Set whether the connection manager is shared across HttpClient instances. @param connManagerShared true to share */
	public void setConnManagerShared(boolean connManagerShared) {
		this.connManagerShared = connManagerShared;
	}

	/** Set whether GZIP request compression is enabled. @param gzip true to enable */
	public void setGzip(boolean gzip) {
		this.gzip = gzip;
	}

	/** Return the pooled connection keep-alive duration in milliseconds. @return the keep-alive duration */
	public long getKeepAlive() {
		return keepAlive;
	}

	/** Set the pooled connection keep-alive duration in milliseconds. @param keepAlive the keep-alive duration */
	public void setKeepAlive(long keepAlive) {
		this.keepAlive = keepAlive;
	}

	/** Return the maximum number of connections held by the pool. @return the max pool size */
	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	/** Set the maximum number of connections held by the pool. @param maxPoolSize the max pool size */
	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	/** Return the maximum number of service-unavailable retries. @return the max retries */
	public int getMaxRetries() {
		return maxRetries;
	}

	/** Set the maximum number of service-unavailable retries. @param maxRetries the max retries */
	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	/** Return the retry interval in milliseconds. @return the retry interval */
	public Integer getRetryInterval() {
		return retryInterval;
	}

	/** Set the retry interval in milliseconds. @param retryInterval the retry interval */
	public void setRetryInterval(Integer retryInterval) {
		this.retryInterval = retryInterval;
	}

	/** Return the maximum HTTP request line length. @return the max line length */
	public int getMaxLineLength() {
		return maxLineLength;
	}

	/** Set the maximum HTTP request line length. @param maxLineLength the max line length */
	public void setMaxLineLength(int maxLineLength) {
		this.maxLineLength = maxLineLength;
	}

	/** Return the maximum number of HTTP headers. @return the max header count */
	public int getMaxHeaderCount() {
		return maxHeaderCount;
	}

	/** Set the maximum number of HTTP headers. @param maxHeaderCount the max header count */
	public void setMaxHeaderCount(int maxHeaderCount) {
		this.maxHeaderCount = maxHeaderCount;
	}

	/** Return the connection character encoding. @return the charset */
	public String getCharset() {
		return charset;
	}

	/** Set the connection character encoding. @param charset the charset */
	public void setCharset(String charset) {
		this.charset = charset;
	}


	/** Return whether HttpClient should use JVM system properties. @return true if system properties are used */
	public boolean isSystemProperties() {
		return systemProperties;
	}

	/** Set whether HttpClient should use JVM system properties. @param systemProperties true to use system properties */
	public void setSystemProperties(boolean systemProperties) {
		this.systemProperties = systemProperties;
	}

	/** Return whether automatic redirect handling is disabled. @return true if disabled */
	public boolean isRedirectHandlingDisabled() {
		return redirectHandlingDisabled;
	}

	/** Set whether automatic redirect handling is disabled. @param redirectHandlingDisabled true to disable */
	public void setRedirectHandlingDisabled(boolean redirectHandlingDisabled) {
		this.redirectHandlingDisabled = redirectHandlingDisabled;
	}

	/** Return whether automatic retries are disabled. @return true if disabled */
	public boolean isAutomaticRetriesDisabled() {
		return automaticRetriesDisabled;
	}

	/** Set whether automatic retries are disabled. @param automaticRetriesDisabled true to disable */
	public void setAutomaticRetriesDisabled(boolean automaticRetriesDisabled) {
		this.automaticRetriesDisabled = automaticRetriesDisabled;
	}

	/** Return whether content compression handling is disabled. @return true if disabled */
	public boolean isContentCompressionDisabled() {
		return contentCompressionDisabled;
	}

	/** Set whether content compression handling is disabled. @param contentCompressionDisabled true to disable */
	public void setContentCompressionDisabled(boolean contentCompressionDisabled) {
		this.contentCompressionDisabled = contentCompressionDisabled;
	}

	/** Return whether cookie management is disabled. @return true if disabled */
	public boolean isCookieManagementDisabled() {
		return cookieManagementDisabled;
	}

	/** Set whether cookie management is disabled. @param cookieManagementDisabled true to disable */
	public void setCookieManagementDisabled(boolean cookieManagementDisabled) {
		this.cookieManagementDisabled = cookieManagementDisabled;
	}

	/** Return whether authentication caching is disabled. @return true if disabled */
	public boolean isAuthCachingDisabled() {
		return authCachingDisabled;
	}

	/** Set whether authentication caching is disabled. @param authCachingDisabled true to disable */
	public void setAuthCachingDisabled(boolean authCachingDisabled) {
		this.authCachingDisabled = authCachingDisabled;
	}

	/** Return whether connection state tracking is disabled. @return true if disabled */
	public boolean isConnectionStateDisabled() {
		return connectionStateDisabled;
	}

	/** Set whether connection state tracking is disabled. @param connectionStateDisabled true to disable */
	public void setConnectionStateDisabled(boolean connectionStateDisabled) {
		this.connectionStateDisabled = connectionStateDisabled;
	}

	/** Return the configured proxy host. @return the proxy host */
	public HttpHost getProxy() {
		return proxy;
	}

	/** Set the proxy host. @param proxy the proxy host */
	public void setProxy(HttpHost proxy) {
		this.proxy = proxy;
	}

	/** Return the configured User-Agent string. @return the user agent */
	public String getUserAgent() {
		return userAgent;
	}

	/** Set the User-Agent string. @param userAgent the user agent */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/** Return whether request bodies should be buffered in memory. @return true if buffered */
	public boolean isBufferRequestBody() {
		return bufferRequestBody;
	}

	/** Set whether request bodies should be buffered in memory. @param bufferRequestBody true to buffer */
	public void setBufferRequestBody(boolean bufferRequestBody) {
		this.bufferRequestBody = bufferRequestBody;
	}

}
