package org.apache.http.spring.boot;

import java.util.List;
import java.util.stream.Collectors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.ConnectionBackoffStrategy;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
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
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.spring.boot.client.HttpClientConnectionManagerBuilder;
import org.apache.http.spring.boot.client.HttpClientManagerProperties;
import org.apache.http.spring.boot.client.HttpClientRequestProperties;
import org.apache.http.spring.boot.client.ProxyAuthStrategy;
import org.apache.http.spring.boot.client.RequestFirst;
import org.apache.http.spring.boot.client.RequestLast;
import org.apache.http.spring.boot.client.ResponseFirst;
import org.apache.http.spring.boot.client.ResponseLast;
import org.apache.http.spring.boot.client.TargetAuthStrategy;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.CollectionUtils;


/**
 * Auto-configuration that assembles the {@link HttpClientBuilder}, the underlying
 * {@link HttpClientConnectionManager} and the resulting {@link CloseableHttpClient} bean, wiring together
 * the dependency beans produced by {@link HttpClientDependsOnAutoConfiguration} and (optionally)
 * {@link HttpClientMetricAutoConfiguration}.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass({ CloseableHttpClient.class, HttpClientConnectionManager.class })
@AutoConfigureAfter({HttpClientDependsOnAutoConfiguration.class, HttpClientMetricAutoConfiguration.class})
@EnableConfigurationProperties(HttpClientProperties.class)
public class HttpClientBuilderAutoConfiguration {

	/** Create the {@link HttpClientConnectionManagerBuilder} bean unless one is already defined. @param connectionConfig default connection configuration @param requestConfig default request configuration @param socketConfig default socket configuration @param dnsResolver DNS resolver @param keepAliveStrategy keep-alive strategy @param publicSuffixMatcher public suffix matcher @param schemePortResolver scheme port resolver @param serviceUnavailStrategy service-unavailable retry strategy @param hostnameVerifier SSL hostname verifier @param trustManager X509 trust manager @param properties connection manager properties @return a new connection manager builder */
	@Bean
	@ConditionalOnMissingBean(HttpClientConnectionManagerBuilder.class)
	public HttpClientConnectionManagerBuilder connectionManagerBuilder(
			ConnectionConfig connectionConfig,
			RequestConfig requestConfig,
			SocketConfig socketConfig,
			DnsResolver dnsResolver,
			ConnectionKeepAliveStrategy keepAliveStrategy,
			PublicSuffixMatcher publicSuffixMatcher,
			SchemePortResolver schemePortResolver,
			ServiceUnavailableRetryStrategy serviceUnavailStrategy,
			HostnameVerifier hostnameVerifier,
			X509TrustManager trustManager,
			HttpClientManagerProperties properties) {
		return HttpClientConnectionManagerBuilder.create()
				.setDefaultConnectionConfig(connectionConfig)
				.setDefaultSocketConfig(socketConfig)
				.setDnsResolver(dnsResolver)
				.setMaxConnPerRoute(properties.getMaxConnPerRoute())
				.setMaxConnTotal(properties.getMaxConnTotal())
				.setPublicSuffixMatcher(publicSuffixMatcher);
	}
	
	/** Build the {@link HttpClientConnectionManager} from the provided builder unless one already exists. @param connectionManagerBuilder the connection manager builder @return a built connection manager */
	@Bean
	@ConditionalOnMissingBean(HttpClientConnectionManager.class)
	public HttpClientConnectionManager connectionManager(HttpClientConnectionManagerBuilder connectionManagerBuilder) {
    	return connectionManagerBuilder.build();
	}

	/**
	 * Build the fully configured {@link HttpClientBuilder} from the wired dependency beans and the
	 * {@link HttpClientProperties} feature toggles.
	 * @param requestFirstInterceptors interceptors to add first to requests
	 * @param requestLastInterceptors interceptors to add last to requests
	 * @param responseFirstInterceptors interceptors to add first to responses
	 * @param responseLastInterceptors interceptors to add last to responses
	 * @param connectionBackoffStrategy connection backoff strategy
	 * @param connectionManager connection manager
	 * @param reuseStrategy connection reuse strategy
	 * @param connectionConfig default connection config
	 * @param cookieStore cookie store
	 * @param credentialsProvider optional credentials provider
	 * @param requestConfig default request config
	 * @param socketConfig default socket config
	 * @param dnsResolver DNS resolver
	 * @param keepAliveStrategy keep-alive strategy
	 * @param proxyAuthStrategy proxy authentication strategy provider
	 * @param publicSuffixMatcher public suffix matcher
	 * @param redirectStrategy redirect strategy
	 * @param httpRequestExecutor request executor
	 * @param retryHandler request retry handler
	 * @param schemePortResolver scheme port resolver
	 * @param serviceUnavailStrategy service-unavailable retry strategy
	 * @param targetAuthStrategy target authentication strategy provider
	 * @param userTokenHandler user token handler
	 * @param sslContext SSL context
	 * @param hostnameVerifier SSL hostname verifier
	 * @param sslSocketFactory layered SSL socket factory
	 * @param properties HttpClient properties
	 * @param managerProperties connection manager properties
	 * @return a configured HttpClientBuilder
	 */
	@Bean
	public HttpClientBuilder httpClientBuilder(
			@RequestFirst ObjectProvider<HttpRequestInterceptor> requestFirstInterceptors,
			@RequestLast ObjectProvider<HttpRequestInterceptor> requestLastInterceptors,
			@ResponseFirst ObjectProvider<HttpResponseInterceptor> responseFirstInterceptors,
			@ResponseLast ObjectProvider<HttpResponseInterceptor> responseLastInterceptors, 
			ConnectionBackoffStrategy connectionBackoffStrategy,
			HttpClientConnectionManager connectionManager,
			ConnectionReuseStrategy reuseStrategy,
			ConnectionConfig connectionConfig,
			CookieStore cookieStore,
			@Autowired(required = false) CredentialsProvider credentialsProvider,
			RequestConfig requestConfig,
			SocketConfig socketConfig,
			DnsResolver dnsResolver,
			ConnectionKeepAliveStrategy keepAliveStrategy,
			@ProxyAuthStrategy ObjectProvider<AuthenticationStrategy> proxyAuthStrategy,
			PublicSuffixMatcher publicSuffixMatcher,
			RedirectStrategy redirectStrategy,
			HttpRequestExecutor httpRequestExecutor,
			HttpRequestRetryHandler retryHandler,
			SchemePortResolver schemePortResolver,
			ServiceUnavailableRetryStrategy serviceUnavailStrategy,
			@TargetAuthStrategy ObjectProvider<AuthenticationStrategy> targetAuthStrategy,
			UserTokenHandler userTokenHandler,
			SSLContext sslContext,
			HostnameVerifier hostnameVerifier,
			LayeredConnectionSocketFactory sslSocketFactory,
			HttpClientProperties properties,
			HttpClientManagerProperties managerProperties) {
		
		//创建忽略任何安全校验的httpClient实例.
		HttpClientBuilder clientBuilder =  HttpClients.custom()
			.evictIdleConnections(managerProperties.getMaxIdleTime(), managerProperties.getMaxIdleTimeUnit())
			//.setBackoffManager(backoffManager)
			.setConnectionBackoffStrategy(connectionBackoffStrategy)
			.setConnectionManager(connectionManager)
			.setConnectionManagerShared(properties.isConnManagerShared())
			.setConnectionReuseStrategy(reuseStrategy)
			.setDefaultConnectionConfig(connectionConfig)
			.setDefaultCookieStore(cookieStore)
			.setDefaultRequestConfig(requestConfig)
			.setDefaultSocketConfig(socketConfig)
			.setDnsResolver(dnsResolver)
			.setKeepAliveStrategy(keepAliveStrategy)
			.setMaxConnPerRoute(managerProperties.getMaxConnPerRoute())
			.setMaxConnTotal(managerProperties.getMaxConnTotal())
			.setPublicSuffixMatcher(publicSuffixMatcher)
			.setRedirectStrategy(redirectStrategy)
			.setRetryHandler(retryHandler)
			.setSchemePortResolver(schemePortResolver)
			.setServiceUnavailableRetryStrategy(serviceUnavailStrategy)
			.setTargetAuthenticationStrategy(targetAuthStrategy.getIfAvailable())
			.setUserTokenHandler(userTokenHandler);
							
		if(properties.getProxy() != null) {
			clientBuilder.setProxy(properties.getProxy())
						 .setProxyAuthenticationStrategy(proxyAuthStrategy.getIfAvailable());
		}						
		if(credentialsProvider != null) {
			clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
		}							
		if(httpRequestExecutor != null) {
			clientBuilder.setRequestExecutor(httpRequestExecutor);
		}				
										
		if(StringUtils.isNotBlank(properties.getUserAgent())) {
			clientBuilder.setUserAgent(properties.getUserAgent());
		}			
		
		clientBuilder.setSSLContext(sslContext);
		clientBuilder.setSSLHostnameVerifier(hostnameVerifier);
		clientBuilder.setSSLSocketFactory(sslSocketFactory);	
		
						
		List<HttpRequestInterceptor> requestFirsts = requestFirstInterceptors.orderedStream().collect(Collectors.toList());	
		if(!CollectionUtils.isEmpty(requestFirsts)) {
			for (HttpRequestInterceptor itcp : requestFirsts) {
				clientBuilder.addInterceptorFirst(itcp);
			}
		}
		List<HttpRequestInterceptor> requestLasts = requestLastInterceptors.orderedStream().collect(Collectors.toList());	
		if(!CollectionUtils.isEmpty(requestLasts)) {
			for (HttpRequestInterceptor itcp : requestLasts) {
				clientBuilder.addInterceptorLast(itcp);
			}
		}	
		List<HttpResponseInterceptor> responseFirsts = responseFirstInterceptors.orderedStream().collect(Collectors.toList());	
		if(!CollectionUtils.isEmpty(responseFirsts)) {
			for (HttpResponseInterceptor itcp : responseFirsts) {
				clientBuilder.addInterceptorFirst(itcp);
			}
		}
		List<HttpResponseInterceptor> responseLasts = responseFirstInterceptors.orderedStream().collect(Collectors.toList());	
		if(!CollectionUtils.isEmpty(responseLasts)) {
			for (HttpResponseInterceptor itcp : responseLasts) {
				clientBuilder.addInterceptorLast(itcp);
			}
		}
		
		if(properties.isAuthCachingDisabled()) {
			clientBuilder.disableAuthCaching();
		}
		if(properties.isAutomaticRetriesDisabled()) {
			clientBuilder.disableAutomaticRetries();
		}
		if(properties.isConnectionStateDisabled()) {
			clientBuilder.disableConnectionState();
		}
		if(properties.isContentCompressionDisabled()) {
			clientBuilder.disableContentCompression();
		}
		if(properties.isCookieManagementDisabled()) {
			clientBuilder.disableCookieManagement();
		}
		if(properties.isRedirectHandlingDisabled()) {
			clientBuilder.disableRedirectHandling();
		}
		 
		return clientBuilder;
	}
	
	/** Build the {@link CloseableHttpClient} from the configured builder unless one already exists. @param httpClientBuilder the HttpClient builder @return a built closeable HttpClient */
	@Bean
	@ConditionalOnMissingBean(CloseableHttpClient.class)
	public CloseableHttpClient closeableHttpClient(HttpClientBuilder httpClientBuilder) {
    	return httpClientBuilder.build();
    }

	/**
	 * Create a Spring {@link HttpComponentsClientHttpRequestFactory} backed by a default HC5 HttpClient,
	 * applying request timeouts.
	 * <p>Spring Framework 7.x requires Apache HttpClient 5 (HC5); the HC4-based {@link HttpClientBuilder}
	 * bean is kept separately for backward compatibility with Dropwizard-metrics instrumentation.</p>
	 * @param requestProperties request properties
	 * @return a HttpComponents-based client request factory
	 */
	@Bean
	public HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory(
			HttpClientRequestProperties requestProperties) {

		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(org.apache.hc.client5.http.impl.classic.HttpClients.createDefault());
		clientHttpRequestFactory.setConnectionRequestTimeout(requestProperties.getConnectionRequestTimeout());
		clientHttpRequestFactory.setReadTimeout(requestProperties.getSocketTimeout());

		return clientHttpRequestFactory;
	}
	
	
}
