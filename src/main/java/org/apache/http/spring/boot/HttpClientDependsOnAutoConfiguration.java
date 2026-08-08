package org.apache.http.spring.boot;

import java.io.IOException;
import java.nio.charset.CodingErrorAction;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.ConnectionBackoffStrategy;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultBackoffStrategy;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.DefaultServiceUnavailableRetryStrategy;
import org.apache.http.impl.client.DefaultUserTokenHandler;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.apache.http.impl.client.TargetAuthenticationStrategy;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.spring.boot.client.HttpClientConnectionProperties;
import org.apache.http.spring.boot.client.HttpClientManagerProperties;
import org.apache.http.spring.boot.client.HttpClientRequestProperties;
import org.apache.http.spring.boot.client.HttpClientSocketProperties;
import org.apache.http.spring.boot.client.ProxyAuthStrategy;
import org.apache.http.spring.boot.client.TargetAuthStrategy;
import org.apache.http.spring.boot.client.handler.HttpRequestExceptionRetryHandler;
import org.apache.http.spring.boot.client.interceptor.HttpRequestGzipInterceptor;
import org.apache.http.spring.boot.client.interceptor.HttpRequestHeaderInterceptor;
import org.apache.http.spring.boot.client.interceptor.HttpRequestHeaderProperties;
import org.apache.http.spring.boot.client.interceptor.HttpRequestSummaryInterceptor;
import org.apache.http.spring.boot.client.interceptor.HttpResponseGzipInterceptor;
import org.apache.http.spring.boot.client.utils.HttpMessageFactoryUtils;
import org.apache.http.spring.boot.client.utils.SSLContextUtils;
import org.apache.http.spring.boot.client.utils.TrustManagerUtils;
import org.apache.http.spring.boot.client.utils.TrustStrategyUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Auto-configuration that provides the default dependency beans required to build an HttpClient,
 * such as connection/socket/request config, strategies, SSL context, trust manager, interceptors and
 * message factories. Each bean is only created when no user-defined bean of the same type exists.
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass({ CloseableHttpClient.class, HttpClientConnectionManager.class })
@EnableConfigurationProperties(value = {HttpClientProperties.class, HttpClientSocketProperties.class})
public class HttpClientDependsOnAutoConfiguration {

	/** Build the default {@link ConnectionConfig} from connection properties unless one already exists. @param properties connection properties @return a configured ConnectionConfig */
	@Bean
	@ConditionalOnMissingBean(ConnectionConfig.class)
	public ConnectionConfig connectionConfig(HttpClientConnectionProperties properties) {
		ConnectionConfig.Builder builder = ConnectionConfig.custom()
				.setBufferSize(properties.getBufferSize())
				.setCharset(properties.getCharset())
				.setFragmentSizeHint(properties.getFragmentSizeHint())
				.setMessageConstraints(properties.getMessageConstraints());
		switch (properties.getMalformedInputAction().value()) {
			case "IGNORE": {
				builder.setMalformedInputAction(CodingErrorAction.IGNORE);
			};break;
			case "REPLACE": {
				builder.setMalformedInputAction(CodingErrorAction.REPLACE);
			};break;
			case "REPORT": {
				builder.setMalformedInputAction(CodingErrorAction.REPORT);
			};break;
		}
		switch (properties.getUnmappableInputAction().value()) {
			case "IGNORE": {
				builder.setUnmappableInputAction(CodingErrorAction.IGNORE);
			};break;
			case "REPLACE": {
				builder.setUnmappableInputAction(CodingErrorAction.REPLACE);
			};break;
			case "REPORT": {
				builder.setUnmappableInputAction(CodingErrorAction.REPORT);
			};break;
		}
		return builder.build();
	}
	
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
				.setLocalAddress(properties.getLocalAddress())
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

	/** Provide the default {@link ConnectionKeepAliveStrategy} unless one already exists. @return the shared DefaultConnectionKeepAliveStrategy */
	@Bean
	@ConditionalOnMissingBean(ConnectionKeepAliveStrategy.class)
	public ConnectionKeepAliveStrategy keepAliveStrategy() {
		return DefaultConnectionKeepAliveStrategy.INSTANCE;
	}

	/** Provide the default proxy {@link AuthenticationStrategy} unless one already exists. @return the shared ProxyAuthenticationStrategy */
	@Bean
	@ConditionalOnMissingBean
	@ProxyAuthStrategy
	public AuthenticationStrategy proxyAuthStrategy() {
		return ProxyAuthenticationStrategy.INSTANCE;
	}

	/** Provide the default {@link PublicSuffixMatcher} for cookie handling unless one already exists. @return the default PublicSuffixMatcher */
	@Bean
	@ConditionalOnMissingBean(PublicSuffixMatcher.class)
	public PublicSuffixMatcher publicSuffixMatcher() {
		return PublicSuffixMatcherLoader.getDefault();
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

	/** Provide a default {@link HttpRequestRetryHandler} honouring configured retry settings unless one already exists. @param properties request properties @return an exception-based retry handler */
	@Bean
	@ConditionalOnMissingBean(HttpRequestRetryHandler.class)
	public HttpRequestRetryHandler retryHandler(HttpClientRequestProperties properties) {
		return new HttpRequestExceptionRetryHandler(properties.getRetryCount(), properties.isRequestSentRetryEnabled());
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

	/** Provide the default target {@link AuthenticationStrategy} unless one already exists. @return the shared TargetAuthenticationStrategy */
	@Bean
	@ConditionalOnMissingBean
	@TargetAuthStrategy
	public AuthenticationStrategy targetAuthStrategy() {
		return TargetAuthenticationStrategy.INSTANCE;
	}

	/** Provide the default {@link UserTokenHandler} unless one already exists. @return the shared DefaultUserTokenHandler */
	@Bean
	@ConditionalOnMissingBean(UserTokenHandler.class)
	public UserTokenHandler userTokenHandler() {
		return DefaultUserTokenHandler.INSTANCE;
	}

	/** Provide the default SSL {@link HostnameVerifier} unless one already exists. @param publicSuffixMatcher public suffix matcher @return a DefaultHostnameVerifier */
	@Bean
	@ConditionalOnMissingBean(HostnameVerifier.class)
	public HostnameVerifier hostnameVerifier(PublicSuffixMatcher publicSuffixMatcher) {
		return new DefaultHostnameVerifier(publicSuffixMatcher);
	}

	/** Provide a trust-all {@link TrustStrategy} unless one already exists. @return an accept-all trust strategy */
	@Bean
	@ConditionalOnMissingBean(TrustStrategy.class)
	public TrustStrategy trustStrategy() {
		return TrustStrategyUtils.getAcceptAllTrustStrategy();
	}

	/** Provide an accept-all {@link TrustManager} unless one already exists. @return an accept-all trust manager */
	@Bean
	@ConditionalOnMissingBean(TrustManager.class)
	public TrustManager trustManager() {
		return TrustManagerUtils.getAcceptAllTrustManager();
	}

	/** Create the default {@link SSLContext} for the configured protocol unless one already exists. @param trustManager X509 trust manager @param properties connection manager properties @return an SSLContext @throws IOException if the SSL context cannot be created */
	@Bean
	@ConditionalOnMissingBean(SSLContext.class)
	public SSLContext sslContext(
			X509TrustManager trustManager,
			HttpClientManagerProperties properties) throws IOException {
		return SSLContextUtils.createSSLContext(properties.getProtocol(), null, trustManager);
	}

	/** Create the default SSL {@link LayeredConnectionSocketFactory} unless one already exists. @param sslContext SSL context @param hostnameVerifier SSL hostname verifier @return an SSLConnectionSocketFactory */
	@Bean
	@ConditionalOnMissingBean(LayeredConnectionSocketFactory.class)
	public LayeredConnectionSocketFactory sslSocketFactory(SSLContext sslContext, HostnameVerifier hostnameVerifier) {
		return new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
	}

    /** Provide the default {@link HttpMessageWriterFactory} for requests unless one already exists. @return a request writer factory */
    @Bean
	@ConditionalOnMissingBean(HttpMessageWriterFactory.class)
	public HttpMessageWriterFactory<HttpRequest> requestWriterFactory() {
    	return HttpMessageFactoryUtils.getRequestWriterFactory();
    }

    /** Provide the default {@link HttpMessageParserFactory} for responses unless one already exists. @return a response parser factory */
    @Bean
	@ConditionalOnMissingBean(HttpMessageParserFactory.class)
	public HttpMessageParserFactory<HttpResponse> responseParserFactory() {
    	return HttpMessageFactoryUtils.getResponseParserFactory();
    }

	/**
	 * Provide a default {@link HttpConnectionFactory} that wires the custom request writer and response
	 * parser factories into each managed connection, unless one already exists.
	 * @param requestWriterFactory request writer factory
	 * @param responseParserFactory response parser factory
	 * @return a ManagedHttpClientConnectionFactory
	 */
	@Bean
	@ConditionalOnMissingBean(HttpConnectionFactory.class)
	public HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory(
			HttpMessageWriterFactory<HttpRequest> requestWriterFactory,
			HttpMessageParserFactory<HttpResponse> responseParserFactory) {
		// Use a custom connection factory to customize the process of
		// initialization of outgoing HTTP connections. Beside standard connection
		// configuration parameters HTTP connection factory can define message
		// parser / writer routines to be employed by individual connections.
		return new ManagedHttpClientConnectionFactory(requestWriterFactory, responseParserFactory);
	}

	/** Register a request-summary logging {@link HttpRequestInterceptor}. @param properties HttpClient properties @return a HttpRequestSummaryInterceptor */
	@Bean
	public HttpRequestInterceptor summaryInterceptor(HttpClientProperties properties) {
		return new HttpRequestSummaryInterceptor();
	}

	/** Register a {@link HttpRequestInterceptor} that injects configured default headers. @param headerProperties header properties @return a HttpRequestHeaderInterceptor */
	@Bean
	public HttpRequestInterceptor headerInterceptor(HttpRequestHeaderProperties headerProperties) {
		return new HttpRequestHeaderInterceptor(headerProperties);
	}

	/** Register a GZIP request {@link HttpRequestInterceptor}. @return a HttpRequestGzipInterceptor */
	@Bean
	public HttpRequestInterceptor gzipInterceptor() {
		return new HttpRequestGzipInterceptor();
	}

	/** Register a GZIP response decompression {@link org.apache.http.HttpResponseInterceptor}. @return a HttpResponseGzipInterceptor */
	@Bean
	public HttpResponseInterceptor ungzipInterceptor() {
		return new HttpResponseGzipInterceptor();
	}
	
}
