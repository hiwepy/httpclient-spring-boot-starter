package org.apache.http.spring.boot.client;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = HttpClientManagerProperties.PREFIX)
public class HttpClientManagerProperties {

	public final static String PREFIX = "httpclient.manager";
	/**
	 * 通过网络与服务器建立连接的超时时间。Httpclient包中通过一个异步线程去创建与服务器的socket连接，这就是该socket连接的超时时间，此处默认为5秒
	 * 决定了直到连接建立时的毫秒级超时时间。超时时间的值为0解释为一个无限大的时间。这个参数期望得到一个java.lang.Integer类型的值。
	 * 如果这个参数没有被设置，连接操作将不会超时（无限大的超时时间）；单位毫秒，默认5000
	 */
	protected int connectTimeout = Integer.parseInt(HttpClientParams.HTTP_REQUEST_CONNECT_TIMEOUT.getDefault());
	/**
	 * Socket读数据的超时时间，即从服务器获取响应数据需要等待的时间；单位毫秒，默认5000
	 */
	protected int socketTimeout = Integer.parseInt(HttpClientParams.HTTP_REQUEST_SOCKET_TIMEOUT.getDefault());
	/**
	 * SSL证书类型；TLS,SSL,SSLv2
	 */
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
