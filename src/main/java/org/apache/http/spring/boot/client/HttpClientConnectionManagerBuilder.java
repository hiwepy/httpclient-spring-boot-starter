package org.apache.http.spring.boot.client;

import java.util.concurrent.TimeUnit;

import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;

/**
 * Builder that assembles a {@link PoolingHttpClientConnectionManager} with the most commonly
 * tuned settings (connection/socket config, DNS resolver, pool limits, public-suffix matcher).
 *
 * <p>Subclasses (e.g. the Dropwizard-metrics instrumented variant) can override
 * {@link #instance} to supply a custom connection-manager implementation.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
public class HttpClientConnectionManagerBuilder {

	private ConnectionConfig defaultConnectionConfig;
	private SocketConfig defaultSocketConfig;
	private DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;
	private int maxConnPerRoute = 20;
	private int maxConnTotal = 200;
	private PublicSuffixMatcher publicSuffixMatcher;

	/** Create a new builder instance. @return a new builder */
	public static HttpClientConnectionManagerBuilder create() {
		return new HttpClientConnectionManagerBuilder();
	}

	/** Set the default {@link ConnectionConfig}. @param config the connection config @return this builder */
	public HttpClientConnectionManagerBuilder setDefaultConnectionConfig(ConnectionConfig config) {
		this.defaultConnectionConfig = config;
		return this;
	}

	/** Set the default {@link SocketConfig}. @param config the socket config @return this builder */
	public HttpClientConnectionManagerBuilder setDefaultSocketConfig(SocketConfig config) {
		this.defaultSocketConfig = config;
		return this;
	}

	/** Set the {@link DnsResolver}. @param resolver the DNS resolver @return this builder */
	public HttpClientConnectionManagerBuilder setDnsResolver(DnsResolver resolver) {
		this.dnsResolver = resolver;
		return this;
	}

	/** Set the maximum number of connections per route. @param maxConnPerRoute the limit @return this builder */
	public HttpClientConnectionManagerBuilder setMaxConnPerRoute(int maxConnPerRoute) {
		this.maxConnPerRoute = maxConnPerRoute;
		return this;
	}

	/** Set the maximum total number of connections. @param maxConnTotal the limit @return this builder */
	public HttpClientConnectionManagerBuilder setMaxConnTotal(int maxConnTotal) {
		this.maxConnTotal = maxConnTotal;
		return this;
	}

	/** Set the {@link PublicSuffixMatcher}. @param matcher the public suffix matcher @return this builder */
	public HttpClientConnectionManagerBuilder setPublicSuffixMatcher(PublicSuffixMatcher matcher) {
		this.publicSuffixMatcher = matcher;
		return this;
	}

	/**
	 * Build the {@link HttpClientConnectionManager} from the configured settings.
	 * @return a configured connection manager
	 */
	public HttpClientConnectionManager build() {
		PoolingHttpClientConnectionManager connManager = instance(null, null, null, dnsResolver, -1, TimeUnit.SECONDS);
		if (defaultConnectionConfig != null) {
			connManager.setDefaultConnectionConfig(defaultConnectionConfig);
		}
		if (defaultSocketConfig != null) {
			connManager.setDefaultSocketConfig(defaultSocketConfig);
		}
		connManager.setDefaultMaxPerRoute(maxConnPerRoute);
		connManager.setMaxTotal(maxConnTotal);
		return connManager;
	}

	/**
	 * Create the underlying {@link PoolingHttpClientConnectionManager}. Subclasses can override
	 * this to supply an instrumented or otherwise customised connection manager.
	 * @param socketFactoryRegistry socket factory registry (may be {@code null})
	 * @param connFactory connection factory (may be {@code null})
	 * @param schemePortResolver scheme port resolver (may be {@code null})
	 * @param dnsResolver DNS resolver
	 * @param connTimeToLive connection time-to-live value
	 * @param connTimeToLiveTimeUnit time-to-live unit
	 * @return a new pooling connection manager
	 */
	protected PoolingHttpClientConnectionManager instance(
			Registry<ConnectionSocketFactory> socketFactoryRegistry,
			HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory,
			SchemePortResolver schemePortResolver,
			DnsResolver dnsResolver,
			long connTimeToLive,
			TimeUnit connTimeToLiveTimeUnit) {
		return new PoolingHttpClientConnectionManager();
	}

}
