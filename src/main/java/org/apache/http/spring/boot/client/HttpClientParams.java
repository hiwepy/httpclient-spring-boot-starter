package org.apache.http.spring.boot.client;

import java.nio.charset.StandardCharsets;
import java.util.Locale;


public enum HttpClientParams {
	
	/**
	 * 是否使用连接池
	 */
	HTTP_CONNECTION_MANAGER("http.connection.manager", "false"),
	 
	/**
	 * 保持连接池内的长连接时长,仅在使用连接池时有效，单位毫秒，默认30秒
	 */
	HTTP_CONNECTION_KEEPALIVE("http.connection.keepAlive", "30000"),
	
	/**
	 * 连接池最大持有连接数;默认 20
	 */
	HTTP_CONNECTION_MAX_POOLSIZE("http.connection.maxPoolSize", "20"),
	
	/**
	 * http请求失败重试次数
	 */
	HTTP_CONNECTION_RETRY_TIME("http.connection.retryTime", "5"),
	
	/**
	 * 最大请求行长度的限制。如果设置为一个正数，任何HTTP请求行超过这个限制将会引发java.io.IOException异常。
	 * 负数或零将会关闭这个检查。这个参数期望得到一个java.lang.Integer类型的值。如果这个参数没有被设置，那么就不强制进行限制了。
	 */
	HTTP_CONNECTION_MAX_LINE_LENGTH("http.connection.max-line-length", "2000"),
	
	/**
	 * 允许的最大HTTP头部信息数量。如果设置为一个正数，从数据流中获得的HTTP头部信息数量超过这个限制就会引发java.io.IOException异常。
	 * 负数或零将会关闭这个检查。这个参数期望得到一个java.lang.Integer类型的值。如果这个参数没有被设置，那么就不强制进行限制了。
	 */
	HTTP_CONNECTION_MAX_HEADER_COUNT("http.connection.max-header-count", "200"),
	
	/**
	 * 连接字符编码格式 可用值 UTF-8，US-ASCII，ISO-8859-1
	 */
	HTTP_CONNECTION_CONFIG_CHARSET("http.connection.config-charset", StandardCharsets.UTF_8.toString()),
	
	/**
	 * 是否禁用DNS查找；true | false, true will return localhost/127.0.0.1 for hostname/hostaddress, false will attempt dns lookup for hostname (default: false).
	 */
	HTTP_CONNECTION_DNS_LOOKUPS_DISABLED("http.connection.dns.lookups-disabled", "true"),
	
	/**
	 * 是否关闭请求连接度量监控
	 */
	HTTP_CONNECTION_METRICS_DISABLED("http.connection.metrics.disabled", "true"),
	
	/**
	 * 度量监控对象名称
	 */
	HTTP_CONNECTION_METRICS_REGISTRYNAME("http.connection.metrics.registryName", "httpclient"),
	
	/**
	 * 设置httpclient是否使用NoDelay策略。如果启用了NoDelay策略，httpclient和站点之间传输数据时将会尽可能及时地将发送缓冲区中的数据发送出去、
	 * 而不考虑网络带宽的利用率，这个策略适合对实时性要求高的场景。而禁用了这个策略之后，数据传输会采用Nagle's algorithm发送数据，该算法会充分顾及带宽的利用率，而不是数据传输的实时性
	 */
	HTTP_SOCKET_TCPNODELAY("http.socket.tcpNoDelay", "true"),
	
	/**
	 * 连接读取数据超时时间；单位毫秒，默认5000
	 */
	HTTP_SOCKET_SO_TIMEOUT("http.socket.so_timeout", "5000"),
	
	/**
	 * 通过网络与服务器建立连接的超时时间。Httpclient包中通过一个异步线程去创建与服务器的socket连接，这就是该socket连接的超时时间，此处默认为5秒
	 * 决定了直到连接建立时的毫秒级超时时间。超时时间的值为0解释为一个无限大的时间。这个参数期望得到一个java.lang.Integer类型的值。
	 * 如果这个参数没有被设置，连接操作将不会超时（无限大的超时时间）；单位毫秒，默认5000
	 */
	HTTP_REQUEST_CONNECT_TIMEOUT("http.request.connect_timeout", "5000"),
	
	/**
	 * Socket读数据的超时时间，即从服务器获取响应数据需要等待的时间；单位毫秒，默认5000
	 */
	HTTP_REQUEST_SOCKET_TIMEOUT("http.request.socket_timeout", "5000"),
	/**
	 * SSL证书类型；TLS,SSL,SSLv2
	 */
	HTTP_SSL_PROTOCOL("http.ssl.protocol", "TLS");
	

	private String name;
	private String defaultValue;

	private HttpClientParams(String name,String defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}

	public String getDefault() {
		return defaultValue;
	}

	static HttpClientParams valueOfIgnoreCase(String parameter,String defaultValue) {
		HttpClientParams parm = valueOf(parameter.toUpperCase(Locale.ENGLISH).trim());
		parm.defaultValue = defaultValue;
		return parm;
	}
	
}
