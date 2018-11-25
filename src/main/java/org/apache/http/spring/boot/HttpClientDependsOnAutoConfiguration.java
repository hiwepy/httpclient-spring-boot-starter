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


@Configuration
@ConditionalOnClass({ CloseableHttpClient.class, HttpClientConnectionManager.class })
@EnableConfigurationProperties(value = {HttpClientProperties.class, HttpClientSocketProperties.class})
public class HttpClientDependsOnAutoConfiguration {
	
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

	@Bean
	@ConditionalOnMissingBean(ConnectionBackoffStrategy.class)
	public ConnectionBackoffStrategy connectionBackoffStrategy() {
		return new DefaultBackoffStrategy();
	}
	
	@Bean
	@ConditionalOnMissingBean(ConnectionReuseStrategy.class)
	public ConnectionReuseStrategy reuseStrategy() {
		return DefaultConnectionReuseStrategy.INSTANCE;
	}
	
	@Bean
	@ConditionalOnMissingBean(CookieStore.class)
	public CookieStore cookieStore() {
		return new BasicCookieStore();
	}
	
	@Bean
	@ConditionalOnMissingBean(DnsResolver.class)
	public DnsResolver dnsResolver() {
		return new SystemDefaultDnsResolver();
	}
	
	@Bean
	@ConditionalOnMissingBean(ConnectionKeepAliveStrategy.class)
	public ConnectionKeepAliveStrategy keepAliveStrategy() {
		return DefaultConnectionKeepAliveStrategy.INSTANCE;
	}
	
	@Bean
	@ConditionalOnMissingBean
	@ProxyAuthStrategy
	public AuthenticationStrategy proxyAuthStrategy() {
		return ProxyAuthenticationStrategy.INSTANCE;
	}
	
	@Bean
	@ConditionalOnMissingBean(PublicSuffixMatcher.class)
	public PublicSuffixMatcher publicSuffixMatcher() {
		return PublicSuffixMatcherLoader.getDefault();
	}
	
	@Bean
	@ConditionalOnMissingBean(HttpRequestExecutor.class)
	public HttpRequestExecutor httpRequestExecutor() {
		return new HttpRequestExecutor();
	}
	
	@Bean
	@ConditionalOnMissingBean(RedirectStrategy.class)
	public RedirectStrategy redirectStrategy() {
		return DefaultRedirectStrategy.INSTANCE;
	}
	
	@Bean
	@ConditionalOnMissingBean(HttpRequestRetryHandler.class)
	public HttpRequestRetryHandler retryHandler(HttpClientRequestProperties properties) {
		return new HttpRequestExceptionRetryHandler(properties.getRetryCount(), properties.isRequestSentRetryEnabled());
	}
	
	@Bean
	@ConditionalOnMissingBean(SchemePortResolver.class)
	public SchemePortResolver schemePortResolver() {
		return DefaultSchemePortResolver.INSTANCE;
	}
	
	@Bean
	@ConditionalOnMissingBean(ServiceUnavailableRetryStrategy.class)
	public ServiceUnavailableRetryStrategy serviceUnavailStrategy(HttpClientProperties properties) {
		return new DefaultServiceUnavailableRetryStrategy(properties.getMaxRetries(), properties.getRetryInterval());
	}
	
	@Bean
	@ConditionalOnMissingBean
	@TargetAuthStrategy
	public AuthenticationStrategy targetAuthStrategy() {
		return TargetAuthenticationStrategy.INSTANCE;
	}
	
	@Bean
	@ConditionalOnMissingBean(UserTokenHandler.class)
	public UserTokenHandler userTokenHandler() {
		return DefaultUserTokenHandler.INSTANCE;
	}
	
	@Bean
	@ConditionalOnMissingBean(HostnameVerifier.class)
	public HostnameVerifier hostnameVerifier(PublicSuffixMatcher publicSuffixMatcher) {
		return new DefaultHostnameVerifier(publicSuffixMatcher);
	}
	
	@Bean
	@ConditionalOnMissingBean(TrustStrategy.class)
	public TrustStrategy trustStrategy() {
		return TrustStrategyUtils.getAcceptAllTrustStrategy();
	}
	
	@Bean
	@ConditionalOnMissingBean(TrustManager.class)
	public TrustManager trustManager() {
		return TrustManagerUtils.getAcceptAllTrustManager();
	}
	
	@Bean
	@ConditionalOnMissingBean(SSLContext.class)
	public SSLContext sslContext(
			X509TrustManager trustManager,
			HttpClientManagerProperties properties) throws IOException {
		return SSLContextUtils.createSSLContext(properties.getProtocol(), null, trustManager);
	}
	
	@Bean
	@ConditionalOnMissingBean(LayeredConnectionSocketFactory.class)
	public LayeredConnectionSocketFactory sslSocketFactory(SSLContext sslContext, HostnameVerifier hostnameVerifier) {
		return new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
	}

    @Bean
	@ConditionalOnMissingBean(HttpMessageWriterFactory.class)
	public HttpMessageWriterFactory<HttpRequest> requestWriterFactory() {
    	return HttpMessageFactoryUtils.getRequestWriterFactory();
	}
        
    @Bean
	@ConditionalOnMissingBean(HttpMessageParserFactory.class)
	public HttpMessageParserFactory<HttpResponse> responseParserFactory() {
    	return HttpMessageFactoryUtils.getResponseParserFactory();
	}
    
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
	    
	@Bean
	public HttpRequestInterceptor summaryInterceptor(HttpClientProperties properties) {
		return new HttpRequestSummaryInterceptor();
	}

	@Bean
	public HttpRequestInterceptor headerInterceptor(HttpRequestHeaderProperties headerProperties) {
		return new HttpRequestHeaderInterceptor(headerProperties);
	}

	@Bean
	public HttpRequestInterceptor gzipInterceptor() {
		return new HttpRequestGzipInterceptor();
	}

	@Bean
	public HttpResponseInterceptor ungzipInterceptor() {
		return new HttpResponseGzipInterceptor();
	}
	
}
