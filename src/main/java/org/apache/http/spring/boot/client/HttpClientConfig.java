package org.apache.http.spring.boot.client;

import java.util.Properties;

public class HttpClientConfig {

	/**
	 * 是否开启请求连接度量监控
	 */
	protected boolean userManager = Boolean.parseBoolean(HttpClientParams.HTTP_CONNECTION_MANAGER.getDefault());
	/**
	 * 保持连接池内的长连接时长,仅在使用连接池时有效，单位毫秒，默认30秒
	 */
	protected long keepAlive = Long.parseLong(HttpClientParams.HTTP_CONNECTION_KEEPALIVE.getDefault());
	/**
	 * 连接池最大持有连接数;默认 20
	 */
	protected int maxPoolSize = Integer.parseInt(HttpClientParams.HTTP_CONNECTION_MAX_POOLSIZE.getDefault());
	/**
	 * http请求失败重试次数
	 */
	protected int retryTime = Integer.parseInt(HttpClientParams.HTTP_CONNECTION_RETRY_TIME.getDefault());
	/**
	 * 最大请求行长度的限制;默认 2000
	 */
	protected int maxLineLength = Integer.parseInt(HttpClientParams.HTTP_CONNECTION_MAX_LINE_LENGTH.getDefault());
	/**
	 * 允许的最大HTTP头部信息数量;默认 200
	 */
	protected int maxHeaderCount = Integer.parseInt(HttpClientParams.HTTP_CONNECTION_MAX_HEADER_COUNT.getDefault());
	/**
	 * 连接字符编码格式 可用值 UTF-8，US-ASCII，ISO-8859-1;默认 UTF-8
	 */
	protected String charset = HttpClientParams.HTTP_CONNECTION_CONFIG_CHARSET.getDefault();
	/**
	 * 是否禁用DNS查找
	 */
	protected boolean dnsLookupsDisabled = Boolean.parseBoolean(HttpClientParams.HTTP_CONNECTION_DNS_LOOKUPS_DISABLED.getDefault());
	/**
	 * 是否关闭请求连接度量监控
	 */
	protected boolean metricsDisabled = Boolean.parseBoolean(HttpClientParams.HTTP_CONNECTION_METRICS_DISABLED.getDefault());
	/**
	 * 度量监控对象名称
	 */
	protected String metricsRegistryName = HttpClientParams.HTTP_CONNECTION_METRICS_REGISTRYNAME.getDefault();
	/**
	 * 设置httpclient是否使用NoDelay策略。如果启用了NoDelay策略，httpclient和站点之间传输数据时将会尽可能及时地将发送缓冲区中的数据发送出去、
	 * 而不考虑网络带宽的利用率，这个策略适合对实时性要求高的场景。而禁用了这个策略之后，数据传输会采用Nagle's
	 * algorithm发送数据，该算法会充分顾及带宽的利用率，而不是数据传输的实时性
	 */
	protected boolean tcpNoDelay = Boolean.parseBoolean(HttpClientParams.HTTP_SOCKET_TCPNODELAY.getDefault());
	/**
	 * 连接读取数据超时时间；单位毫秒，默认5000
	 */
	protected int soTimeout = Integer.parseInt(HttpClientParams.HTTP_SOCKET_SO_TIMEOUT.getDefault());
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
	 * 定时清除失效连接心跳线程延时启动时间；单位毫秒，默认5000
	 */
	protected int heartbeatInitialDelay = Integer.parseInt(HttpClientParams.HTTP_HEARTBEAT_INITIALDELAY.getDefault());
	/**
	 * 定时清除失效连接心跳线程执行周期；单位毫秒，默认5000
	 */
	protected int heartbeatPeriod = Integer.parseInt(HttpClientParams.HTTP_HEARTBEAT_PERIOD.getDefault());
	/**
	 * SSL证书类型；TLS,SSL,SSLv2
	 */
	protected String sslProtocol = HttpClientParams.HTTP_SSL_PROTOCOL.getDefault();
	/**
	 * Host对应的DNS、keepAlive信息
	 * [host].dns
	 * [host].keepAlive
	 */
	protected Properties hostProperties = new Properties();
	/**
	 * 默认的HttpHeader配置
	 */
	protected Properties headerProperties = new Properties();
	/**
	 * 相应的状态码信息
	 */
	protected Properties messages = new Properties();
	
	public boolean isUserManager() {
		return userManager;
	}

	public void setUserManager(boolean userManager) {
		this.userManager = userManager;
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

	public int getRetryTime() {
		return retryTime;
	}

	public void setRetryTime(int retryTime) {
		this.retryTime = retryTime;
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

	public boolean isDnsLookupsDisabled() {
		return dnsLookupsDisabled;
	}

	public void setDnsLookupsDisabled(boolean dnsLookupsDisabled) {
		this.dnsLookupsDisabled = dnsLookupsDisabled;
	}

	public boolean isMetricsDisabled() {
		return metricsDisabled;
	}

	public void setMetricsDisabled(boolean metricsDisabled) {
		this.metricsDisabled = metricsDisabled;
	}

	public String getMetricsRegistryName() {
		return metricsRegistryName;
	}

	public void setMetricsRegistryName(String metricsRegistryName) {
		this.metricsRegistryName = metricsRegistryName;
	}

	public boolean isTcpNoDelay() {
		return tcpNoDelay;
	}

	public void setTcpNoDelay(boolean tcpNoDelay) {
		this.tcpNoDelay = tcpNoDelay;
	}

	public int getSoTimeout() {
		return soTimeout;
	}

	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
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

	public int getHeartbeatInitialDelay() {
		return heartbeatInitialDelay;
	}

	public void setHeartbeatInitialDelay(int heartbeatInitialDelay) {
		this.heartbeatInitialDelay = heartbeatInitialDelay;
	}

	public int getHeartbeatPeriod() {
		return heartbeatPeriod;
	}

	public void setHeartbeatPeriod(int heartbeatPeriod) {
		this.heartbeatPeriod = heartbeatPeriod;
	}

	public String getSslProtocol() {
		return sslProtocol;
	}

	public void setSslProtocol(String sslProtocol) {
		this.sslProtocol = sslProtocol;
	}

	public Properties getHostProperties() {
		return hostProperties;
	}

	public void setHostProperties(Properties hostProperties) {
		this.hostProperties = hostProperties;
	}

	public Properties getHeaderProperties() {
		return headerProperties;
	}

	public void setHeaderProperties(Properties headerProperties) {
		this.headerProperties = headerProperties;
	}

	public Properties getMessages() {
		return messages;
	}

	public void setMessages(Properties messages) {
		this.messages = messages;
	}
	
}
