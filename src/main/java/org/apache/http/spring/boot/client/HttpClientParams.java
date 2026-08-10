package org.apache.http.spring.boot.client;

import java.nio.charset.StandardCharsets;
import java.util.Locale;


/**
 * Enumeration of well-known HttpClient configuration parameter keys together with their default values.
 * <p>Each constant exposes the property name and a default string value used across the
 * {@code httpclient.*} configuration properties.</p>
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public enum HttpClientParams {

	/** Whether to use a connection pool. */
	HTTP_CONNECTION_MANAGER("http.connection.manager", "false"),

	/** Duration to keep pooled connections alive, in milliseconds; only effective when using a connection pool. Default is 30 seconds. */
	HTTP_CONNECTION_KEEPALIVE("http.connection.keepAlive", "30000"),

	/** Maximum number of connections held by the connection pool; default is 20. */
	HTTP_CONNECTION_MAX_POOLSIZE("http.connection.maxPoolSize", "20"),

	/** Number of retries on HTTP request failure. */
	HTTP_CONNECTION_RETRY_TIME("http.connection.retryTime", "5"),

	/**
	 * Maximum length limit of the HTTP request line. If set to a positive number, any HTTP request line
	 * exceeding this limit raises a {@code java.io.IOException}. A negative number or zero disables this
	 * check. Expected value type is {@code java.lang.Integer}; unset means no limit is enforced.
	 */
	HTTP_CONNECTION_MAX_LINE_LENGTH("http.connection.max-line-length", "2000"),

	/**
	 * Maximum allowed number of HTTP headers. If set to a positive number, exceeding this limit when
	 * reading headers from the stream raises a {@code java.io.IOException}. A negative number or zero
	 * disables this check. Expected value type is {@code java.lang.Integer}; unset means no limit.
	 */
	HTTP_CONNECTION_MAX_HEADER_COUNT("http.connection.max-header-count", "200"),

	/** Connection character encoding; allowed values are UTF-8, US-ASCII, ISO-8859-1. */
	HTTP_CONNECTION_CONFIG_CHARSET("http.connection.config-charset", StandardCharsets.UTF_8.toString()),

	/** Whether DNS lookups are disabled; {@code true} returns localhost/127.0.0.1 for host name/address, {@code false} attempts a real DNS lookup (default: false). */
	HTTP_CONNECTION_DNS_LOOKUPS_DISABLED("http.connection.dns.lookups-disabled", "true"),

	/** Whether request connection metric monitoring is disabled. */
	HTTP_CONNECTION_METRICS_DISABLED("http.connection.metrics.disabled", "true"),

	/** Metric registry object name. */
	HTTP_CONNECTION_METRICS_REGISTRYNAME("http.connection.metrics.registryName", "httpclient"),

	/**
	 * Whether HttpClient uses the NoDelay (TCP_NODELAY) strategy. When enabled, data is flushed from the
	 * send buffer as early as possible at the expense of bandwidth utilisation, which suits
	 * latency-sensitive scenarios. When disabled, data is sent using Nagle's algorithm, which favours
	 * bandwidth utilisation over latency.
	 */
	HTTP_SOCKET_TCPNODELAY("http.socket.tcpNoDelay", "true"),

	/** Socket read timeout in milliseconds; default is 5000. */
	HTTP_SOCKET_SO_TIMEOUT("http.socket.so_timeout", "5000"),

	/**
	 * Timeout for establishing the network connection to the server. HttpClient creates the socket
	 * connection asynchronously, and this is the connection timeout in milliseconds; default is 5000.
	 * A value of 0 is interpreted as an infinite timeout. Expected value type is {@code java.lang.Integer}.
	 * If unset, the connection operation will not time out (infinite timeout).
	 */
	HTTP_REQUEST_CONNECT_TIMEOUT("http.request.connect_timeout", "5000"),

	/** Socket read timeout, i.e. how long to wait when receiving response data from the server, in milliseconds; default is 5000. */
	HTTP_REQUEST_SOCKET_TIMEOUT("http.request.socket_timeout", "5000"),
	/** SSL certificate protocol type; allowed values are TLS, SSL, SSLv2. */
	HTTP_SSL_PROTOCOL("http.ssl.protocol", "TLS");
	

	private String name;
	private String defaultValue;

	private HttpClientParams(String name,String defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}

	/** Return the property name of this parameter. @return the property name */
	public String getName() {
		return name;
	}

	/** Return the default value of this parameter. @return the default value */
	public String getDefault() {
		return defaultValue;
	}

	/** Resolve a parameter case-insensitively and override its default value. @param parameter the parameter name @param defaultValue the default value to apply @return the resolved parameter */
	static HttpClientParams valueOfIgnoreCase(String parameter,String defaultValue) {
		HttpClientParams parm = valueOf(parameter.toUpperCase(Locale.ENGLISH).trim());
		parm.defaultValue = defaultValue;
		return parm;
	}
	
}
