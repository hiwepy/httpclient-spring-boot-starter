package org.apache.http.spring.boot;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.client.ConnectionBackoffStrategy;
import org.apache.http.client.CookieStore;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultBackoffStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.DefaultServiceUnavailableRetryStrategy;
import org.apache.http.impl.client.DefaultUserTokenHandler;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.apache.http.impl.client.TargetAuthenticationStrategy;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.spring.boot.client.HttpClientConnectionProperties;
import org.apache.http.spring.boot.client.HttpClientRequestProperties;
import org.apache.http.spring.boot.client.HttpClientSocketProperties;
import org.apache.http.spring.boot.client.ProxyAuthStrategy;
import org.apache.http.spring.boot.client.TargetAuthStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Auto-configuration that provides the default dependency beans required to build an HttpClient,
 * such as request/socket config, strategies and the standard Apache HttpClient defaults.
 *
 * <p>Each bean is only created when no user-defined bean of the same type exists.</p>
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass({ org.apache.http.impl.client.CloseableHttpClient.class, HttpClientConnectionManager.class })
@EnableConfigurationProperties(value = { HttpClientProperties.class, HttpClientSocketProperties.class })
public class HttpClientDependsOnAutoConfiguration {

	/** Build the default {@link SocketConfig} from socket properties unless one already exists. @param properties socket properties @return a configured SocketConfig */
	@Bean
	@ConditionalOnMissingBean(SocketConfig.class)
	public SocketConfig socketConfig(HttpClientSocketProperties properties) {
		return SocketConfig.custom().setBacklogSize(properties.getBacklogSize())
				.setRcvBufSize(properties.getRcvBufSize())
				.setSndBufSize(properties.getSndBufSize())
				.setSoKeepAlive(properties.isSoKeepAlive())
				.setSoLinger(properties.getSoLinger())
				.setSoReuseAddress(properties.isSoReuseAddress())
				.setSoTimeout(properties.getSoTimeout())
				.setTcpNoDelay(properties.isTcpNoDelay()).build();
	}

	/** Build the default {@link RequestConfig} from request properties unless one already exists. @param properties request properties @return a configured RequestConfig */
	@Bean
	@ConditionalOnMissingBean(RequestConfig.class)
	public RequestConfig requestConfig(HttpClientRequestProperties properties) {
		return RequestConfig.custom().setAuthenticationEnabled(properties.isAuthenticationEnabled())
				.setCircularRedirectsAllowed(properties.isCircularRedirectsAllowed())
				.setConnectionRequestTimeout(properties.getConnectionRequestTimeout())
				.setConnectTimeout(properties.getConnectTimeout())
				.setContentCompressionEnabled(properties.isContentCompressionEnabled())
				.setCookieSpec(properties.getCookieSpec())
				.setExpectContinueEnabled(properties.isExpectContinueEnabled())
				.setMaxRedirects(properties.getMaxRedirects())
				.setProxy(properties.getProxy())
				.setProxyPreferredAuthSchemes(properties.getProxyPreferredAuthSchemes())
				.setRedirectsEnabled(properties.isRedirectsEnabled())
				.setRelativeRedirectsAllowed(properties.isRelativeRedirectsAllowed())
				.setSocketTimeout(properties.getSocketTimeout())
				.setTargetPreferredAuthSchemes(properties.getTargetPreferredAuthSchemes()).build();
	}

	/** Provide the default {@link ConnectionBackoffStrategy} unless one already exists. @return a DefaultBackoffStrategy */
	@Bean
	@ConditionalOnMissingBean(ConnectionBackoffStrategy.class)
	public ConnectionBackoffStrategy connectionBackoffStrategy() {
		return new DefaultBackoffStrategy();
	}

	/** Provide the default {@link ConnectionReuseStrategy} unless one already exists. @return the shared DefaultConnectionReuseStrategy */
	@Bean
	@ConditionalOnMissingBean(ConnectionReuseStrategy.class)
	public ConnectionReuseStrategy reuseStrategy() {
		return DefaultConnectionReuseStrategy.INSTANCE;
	}

	/** Provide a default in-memory {@link CookieStore} unless one already exists. @return a BasicCookieStore */
	@Bean
	@ConditionalOnMissingBean(CookieStore.class)
	public CookieStore cookieStore() {
		return new BasicCookieStore();
	}

	/** Provide a default {@link DnsResolver} backed by the JVM defaults unless one already exists. @return a SystemDefaultDnsResolver */
	@Bean
	@ConditionalOnMissingBean(DnsResolver.class)
	public DnsResolver dnsResolver() {
		return new SystemDefaultDnsResolver();
	}

	/** Provide the default proxy {@link org.apache.http.client.AuthenticationStrategy} unless one already exists. @return the shared ProxyAuthenticationStrategy */
	@Bean
	@ConditionalOnMissingBean
	@ProxyAuthStrategy
	public org.apache.http.client.AuthenticationStrategy proxyAuthStrategy() {
		return ProxyAuthenticationStrategy.INSTANCE;
	}

	/** Provide a default {@link HttpRequestExecutor} unless one already exists. @return a new HttpRequestExecutor */
	@Bean
	@ConditionalOnMissingBean(HttpRequestExecutor.class)
	public HttpRequestExecutor httpRequestExecutor() {
		return new HttpRequestExecutor();
	}

	/** Provide the default {@link RedirectStrategy} unless one already exists. @return the shared DefaultRedirectStrategy */
	@Bean
	@ConditionalOnMissingBean(RedirectStrategy.class)
	public RedirectStrategy redirectStrategy() {
		return DefaultRedirectStrategy.INSTANCE;
	}

	/** Provide the default {@link SchemePortResolver} unless one already exists. @return the shared DefaultSchemePortResolver */
	@Bean
	@ConditionalOnMissingBean(SchemePortResolver.class)
	public SchemePortResolver schemePortResolver() {
		return DefaultSchemePortResolver.INSTANCE;
	}

	/** Provide a default {@link ServiceUnavailableRetryStrategy} driven by max-retries and retry-interval properties unless one already exists. @param properties HttpClient properties @return a DefaultServiceUnavailableRetryStrategy */
	@Bean
	@ConditionalOnMissingBean(ServiceUnavailableRetryStrategy.class)
	public ServiceUnavailableRetryStrategy serviceUnavailStrategy(HttpClientProperties properties) {
		return new DefaultServiceUnavailableRetryStrategy(properties.getMaxRetries(), properties.getRetryInterval());
	}

	/** Provide the default target {@link org.apache.http.client.AuthenticationStrategy} unless one already exists. @return the shared TargetAuthenticationStrategy */
	@Bean
	@ConditionalOnMissingBean
	@TargetAuthStrategy
	public org.apache.http.client.AuthenticationStrategy targetAuthStrategy() {
		return TargetAuthenticationStrategy.INSTANCE;
	}

	/** Provide the default {@link UserTokenHandler} unless one already exists. @return the shared DefaultUserTokenHandler */
	@Bean
	@ConditionalOnMissingBean(UserTokenHandler.class)
	public UserTokenHandler userTokenHandler() {
		return DefaultUserTokenHandler.INSTANCE;
	}

	/** Return the bound HttpClient connection properties. @param properties connection properties @return the same properties instance (convenience accessor) */
	@Bean
	@ConditionalOnMissingBean
	public HttpClientConnectionProperties httpClientConnectionProperties(HttpClientConnectionProperties properties) {
		return properties;
	}

}
