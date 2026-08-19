package org.apache.http.spring.boot.client;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the HttpClient connection manager, bound to the {@code httpclient.manager.*}
 * prefix. Covers connect and socket timeouts, SSL protocol, idle/expired connection eviction, pool sizing,
 * connection time-to-live and feature toggles that mirror {@link org.apache.http.impl.client.HttpClientBuilder}.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = HttpClientManagerProperties.PREFIX)
public class HttpClientManagerProperties {

	public final static String PREFIX = "httpclient.manager";
	/**
	 * Timeout for establishing the network connection to the server. HttpClient creates the socket
	 * connection asynchronously, and this is the connection timeout in milliseconds; default is 5000.
	 * A value of 0 is interpreted as an infinite timeout. Expected value type is {@code java.lang.Integer}.
	 * If unset, the connection operation will not time out (infinite timeout).
	 */
	protected int connectTimeout = Integer.parseInt(HttpClientParams.HTTP_REQUEST_CONNECT_TIMEOUT.getDefault());
	/** Socket read timeout, i.e. how long to wait when receiving response data from the server, in milliseconds; default is 5000. */
	protected int socketTimeout = Integer.parseInt(HttpClientParams.HTTP_REQUEST_SOCKET_TIMEOUT.getDefault());
	/** SSL certificate protocol type; allowed values are TLS, SSL, SSLv2. */
	protected String protocol = HttpClientParams.HTTP_SSL_PROTOCOL.getDefault();
	private boolean evictExpiredConnections;
    private boolean evictIdleConnections;
    private long maxIdleTime;
    private TimeUnit maxIdleTimeUnit;

    private boolean systemProperties;
    private boolean redirectHandlingDisabled;
    private boolean automaticRetriesDisabled;
    private boolean contentCompressionDisabled;
    private boolean cookieManagementDisabled;
    private boolean authCachingDisabled;
    private boolean connectionStateDisabled;

    private int maxConnTotal = 0;
    private int maxConnPerRoute = 0;

    private long connTimeToLive = -1;
    private TimeUnit connTimeToLiveTimeUnit = TimeUnit.MILLISECONDS;
    
    private HttpHost proxy;
    private String userAgent;
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
	/** Gets the protocol. */


	public String getProtocol() {
		return protocol;
	}
	/** Sets the protocol. */

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	/** Gets the boolean property. */

	public boolean getBooleanProperty(Properties properties, HttpClientParams key) { 
		String property = properties.getProperty(key.getName(), key.getDefault()); 
		return Boolean.getBoolean(property);
	}
	/** Gets the int property. */
	
	public int getIntProperty(Properties properties, HttpClientParams key) { 
		String property = properties.getProperty(key.getName(), key.getDefault()); 
		return Integer.parseInt(property);
	}
	/** Gets the long property. */
	
	public long getLongProperty(Properties properties, HttpClientParams key) { 
		String property = properties.getProperty(key.getName(), key.getDefault()); 
		return Long.parseLong(property);
	}
	/** Gets the string property. */
 
	public String getStringProperty(Properties properties, HttpClientParams key) { 
		return properties.getProperty(key.getName(), key.getDefault());
	}
	/** Sets the params. */
	
	protected String setParams(String message, Object... params) {
		if (null != params && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				message = message.replaceFirst("\\{" + i + "\\}", String.valueOf(params[i]));
			}
		}
		return message;
	}
	/**
	 * <p>Is evict expired connections.</p>
	 * @return the boolean
	 */
	
	public boolean isEvictExpiredConnections() {
		return evictExpiredConnections;
	}
	/** Sets the evict expired connections. */

	public void setEvictExpiredConnections(boolean evictExpiredConnections) {
		this.evictExpiredConnections = evictExpiredConnections;
	}
	/**
	 * <p>Is evict idle connections.</p>
	 * @return the boolean
	 */

	public boolean isEvictIdleConnections() {
		return evictIdleConnections;
	}
	/** Sets the evict idle connections. */

	public void setEvictIdleConnections(boolean evictIdleConnections) {
		this.evictIdleConnections = evictIdleConnections;
	}
	/** Gets the max idle time. */

	public long getMaxIdleTime() {
		return maxIdleTime;
	}
	/** Sets the max idle time. */

	public void setMaxIdleTime(long maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
	}
	/** Gets the max idle time unit. */

	public TimeUnit getMaxIdleTimeUnit() {
		return maxIdleTimeUnit;
	}
	/** Sets the max idle time unit. */

	public void setMaxIdleTimeUnit(TimeUnit maxIdleTimeUnit) {
		this.maxIdleTimeUnit = maxIdleTimeUnit;
	}
	/**
	 * <p>Is system properties.</p>
	 * @return the boolean
	 */

	public boolean isSystemProperties() {
		return systemProperties;
	}
	/** Sets the system properties. */

	public void setSystemProperties(boolean systemProperties) {
		this.systemProperties = systemProperties;
	}
	/**
	 * <p>Is redirect handling disabled.</p>
	 * @return the boolean
	 */

	public boolean isRedirectHandlingDisabled() {
		return redirectHandlingDisabled;
	}
	/** Sets the redirect handling disabled. */

	public void setRedirectHandlingDisabled(boolean redirectHandlingDisabled) {
		this.redirectHandlingDisabled = redirectHandlingDisabled;
	}
	/**
	 * <p>Is automatic retries disabled.</p>
	 * @return the boolean
	 */

	public boolean isAutomaticRetriesDisabled() {
		return automaticRetriesDisabled;
	}
	/** Sets the automatic retries disabled. */

	public void setAutomaticRetriesDisabled(boolean automaticRetriesDisabled) {
		this.automaticRetriesDisabled = automaticRetriesDisabled;
	}
	/**
	 * <p>Is content compression disabled.</p>
	 * @return the boolean
	 */

	public boolean isContentCompressionDisabled() {
		return contentCompressionDisabled;
	}
	/** Sets the content compression disabled. */

	public void setContentCompressionDisabled(boolean contentCompressionDisabled) {
		this.contentCompressionDisabled = contentCompressionDisabled;
	}
	/**
	 * <p>Is cookie management disabled.</p>
	 * @return the boolean
	 */

	public boolean isCookieManagementDisabled() {
		return cookieManagementDisabled;
	}
	/** Sets the cookie management disabled. */

	public void setCookieManagementDisabled(boolean cookieManagementDisabled) {
		this.cookieManagementDisabled = cookieManagementDisabled;
	}
	/**
	 * <p>Is auth caching disabled.</p>
	 * @return the boolean
	 */

	public boolean isAuthCachingDisabled() {
		return authCachingDisabled;
	}
	/** Sets the auth caching disabled. */

	public void setAuthCachingDisabled(boolean authCachingDisabled) {
		this.authCachingDisabled = authCachingDisabled;
	}
	/**
	 * <p>Is connection state disabled.</p>
	 * @return the boolean
	 */

	public boolean isConnectionStateDisabled() {
		return connectionStateDisabled;
	}
	/** Sets the connection state disabled. */

	public void setConnectionStateDisabled(boolean connectionStateDisabled) {
		this.connectionStateDisabled = connectionStateDisabled;
	}
	/** Gets the max conn total. */

	public int getMaxConnTotal() {
		return maxConnTotal;
	}
	/** Sets the max conn total. */

	public void setMaxConnTotal(int maxConnTotal) {
		this.maxConnTotal = maxConnTotal;
	}
	/** Gets the max conn per route. */

	public int getMaxConnPerRoute() {
		return maxConnPerRoute;
	}
	/** Sets the max conn per route. */

	public void setMaxConnPerRoute(int maxConnPerRoute) {
		this.maxConnPerRoute = maxConnPerRoute;
	}
	/** Gets the conn time to live. */

	public long getConnTimeToLive() {
		return connTimeToLive;
	}
	/** Sets the conn time to live. */

	public void setConnTimeToLive(long connTimeToLive) {
		this.connTimeToLive = connTimeToLive;
	}
	/** Gets the conn time to live time unit. */

	public TimeUnit getConnTimeToLiveTimeUnit() {
		return connTimeToLiveTimeUnit;
	}
	/** Sets the conn time to live time unit. */

	public void setConnTimeToLiveTimeUnit(TimeUnit connTimeToLiveTimeUnit) {
		this.connTimeToLiveTimeUnit = connTimeToLiveTimeUnit;
	}
	/** Gets the proxy. */

	public HttpHost getProxy() {
		return proxy;
	}
	/** Sets the proxy. */

	public void setProxy(HttpHost proxy) {
		this.proxy = proxy;
	}
	/** Gets the user agent. */

	public String getUserAgent() {
		return userAgent;
	}
	/** Sets the user agent. */

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	
}
