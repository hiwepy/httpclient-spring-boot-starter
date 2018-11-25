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


@Configuration
@ConditionalOnClass({ CloseableHttpClient.class, HttpClientConnectionManager.class })
@AutoConfigureAfter({HttpClientDependsOnAutoConfiguration.class, HttpClientMetricAutoConfiguration.class})
@EnableConfigurationProperties(HttpClientProperties.class)
public class HttpClientBuilderAutoConfiguration {
	
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
	
	@Bean
	@ConditionalOnMissingBean(HttpClientConnectionManager.class)
	public HttpClientConnectionManager connectionManager(HttpClientConnectionManagerBuilder connectionManagerBuilder) {
    	return connectionManagerBuilder.build();
	}
	
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
	
	@Bean
	@ConditionalOnMissingBean(CloseableHttpClient.class)
	public CloseableHttpClient closeableHttpClient(HttpClientBuilder httpClientBuilder) {
    	return httpClientBuilder.build();
	}
	
	@Bean
	public HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory(
			HttpClientBuilder httpClientBuilder,
			HttpClientProperties properties,
			HttpClientRequestProperties requestProperties) {
    	
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setBufferRequestBody(properties.isBufferRequestBody());
		clientHttpRequestFactory.setConnectionRequestTimeout(requestProperties.getConnectionRequestTimeout());
		clientHttpRequestFactory.setConnectTimeout(requestProperties.getConnectTimeout());
		clientHttpRequestFactory.setHttpClient(httpClientBuilder.build());
		clientHttpRequestFactory.setReadTimeout(requestProperties.getSocketTimeout());
		
		return clientHttpRequestFactory;
	}
	
	

}
