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


	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public boolean getBooleanProperty(Properties properties, HttpClientParams key) { 
		String property = properties.getProperty(key.getName(), key.getDefault()); 
		return Boolean.getBoolean(property);
	}
	
	public int getIntProperty(Properties properties, HttpClientParams key) { 
		String property = properties.getProperty(key.getName(), key.getDefault()); 
		return Integer.parseInt(property);
	}
	
	public long getLongProperty(Properties properties, HttpClientParams key) { 
		String property = properties.getProperty(key.getName(), key.getDefault()); 
		return Long.parseLong(property);
	}
 
	public String getStringProperty(Properties properties, HttpClientParams key) { 
		return properties.getProperty(key.getName(), key.getDefault());
	}
	
	protected String setParams(String message, Object... params) {
		if (null != params && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				message = message.replaceFirst("\\{" + i + "\\}", String.valueOf(params[i]));
			}
		}
		return message;
	}
	
	public boolean isEvictExpiredConnections() {
		return evictExpiredConnections;
	}

	public void setEvictExpiredConnections(boolean evictExpiredConnections) {
		this.evictExpiredConnections = evictExpiredConnections;
	}

	public boolean isEvictIdleConnections() {
		return evictIdleConnections;
	}

	public void setEvictIdleConnections(boolean evictIdleConnections) {
		this.evictIdleConnections = evictIdleConnections;
	}

	public long getMaxIdleTime() {
		return maxIdleTime;
	}

	public void setMaxIdleTime(long maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
	}

	public TimeUnit getMaxIdleTimeUnit() {
		return maxIdleTimeUnit;
	}

	public void setMaxIdleTimeUnit(TimeUnit maxIdleTimeUnit) {
		this.maxIdleTimeUnit = maxIdleTimeUnit;
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

	public int getMaxConnTotal() {
		return maxConnTotal;
	}

	public void setMaxConnTotal(int maxConnTotal) {
		this.maxConnTotal = maxConnTotal;
	}

	public int getMaxConnPerRoute() {
		return maxConnPerRoute;
	}

	public void setMaxConnPerRoute(int maxConnPerRoute) {
		this.maxConnPerRoute = maxConnPerRoute;
	}

	public long getConnTimeToLive() {
		return connTimeToLive;
	}

	public void setConnTimeToLive(long connTimeToLive) {
		this.connTimeToLive = connTimeToLive;
	}

	public TimeUnit getConnTimeToLiveTimeUnit() {
		return connTimeToLiveTimeUnit;
	}

	public void setConnTimeToLiveTimeUnit(TimeUnit connTimeToLiveTimeUnit) {
		this.connTimeToLiveTimeUnit = connTimeToLiveTimeUnit;
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
	
	
	
}
