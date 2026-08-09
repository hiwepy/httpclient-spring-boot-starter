package org.apache.http.spring.boot;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.ConnectionBackoffStrategy;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
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
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultBackoffStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.DefaultServiceUnavailableRetryStrategy;
import org.apache.http.impl.client.DefaultUserTokenHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.apache.http.impl.client.TargetAuthenticationStrategy;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.spring.boot.client.HttpClientConnectionManagerBuilder;
import org.apache.http.spring.boot.client.HttpClientManagerProperties;
import org.apache.http.spring.boot.client.HttpClientRequestProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HttpClientBuilderAutoConfiguration Tests")
class HttpClientBuilderAutoConfigurationTest {

    private final HttpClientBuilderAutoConfiguration config = new HttpClientBuilderAutoConfiguration();

    @Test
    @DisplayName("Auto-configuration class can be instantiated")
    void testInstantiation() {
        assertThat(config).isNotNull();
    }

    @Test
    @DisplayName("Auto-configuration class exists")
    void testClassExists() {
        assertThat(HttpClientBuilderAutoConfiguration.class).isNotNull();
    }

    @Test
    @DisplayName("Auto-configuration is a configuration class")
    void testIsConfiguration() {
        assertThat(HttpClientBuilderAutoConfiguration.class.isAnnotationPresent(
                org.springframework.context.annotation.Configuration.class)).isTrue();
    }

    @Test
    @DisplayName("connectionManagerBuilder bean is created")
    void testConnectionManagerBuilder() {
        ConnectionConfig connConfig = ConnectionConfig.custom().build();
        RequestConfig reqConfig = RequestConfig.custom().build();
        SocketConfig sockConfig = SocketConfig.custom().build();
        DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;
        ConnectionKeepAliveStrategy keepAliveStrategy = (response, context) -> 30;
        PublicSuffixMatcher suffixMatcher = new PublicSuffixMatcher(Collections.singleton("com"), null);
        SchemePortResolver schemePortResolver = (host) -> 443;
        ServiceUnavailableRetryStrategy retryStrategy = new org.apache.http.impl.client.DefaultServiceUnavailableRetryStrategy();
        HostnameVerifier hostnameVerifier = (hostname, session) -> true;
        X509TrustManager trustManager = new javax.net.ssl.X509TrustManager() {
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[0]; }
        };
        HttpClientManagerProperties properties = new HttpClientManagerProperties();
        properties.setMaxConnPerRoute(20);
        properties.setMaxConnTotal(200);

        HttpClientConnectionManagerBuilder builder = config.connectionManagerBuilder(
                connConfig, reqConfig, sockConfig, dnsResolver, keepAliveStrategy,
                suffixMatcher, schemePortResolver, retryStrategy,
                hostnameVerifier, trustManager, properties);
        assertThat(builder).isNotNull();
    }

    @Test
    @DisplayName("connectionManager bean is created from builder")
    void testConnectionManager() {
        HttpClientConnectionManagerBuilder builder = HttpClientConnectionManagerBuilder.create();
        HttpClientConnectionManager manager = config.connectionManager(builder);
        assertThat(manager).isNotNull();
    }

    @Test
    @DisplayName("httpComponentsClientHttpRequestFactory bean is created")
    void testHttpComponentsClientHttpRequestFactory() {
        HttpClientRequestProperties requestProperties = new HttpClientRequestProperties();
        requestProperties.setConnectionRequestTimeout(3000);
        requestProperties.setSocketTimeout(5000);
        HttpComponentsClientHttpRequestFactory factory = config.httpComponentsClientHttpRequestFactory(requestProperties);
        assertThat(factory).isNotNull();
    }

    @Test
    @DisplayName("httpClientBuilder bean is created with all dependencies")
    void testHttpClientBuilder() throws Exception {
        ObjectProvider<HttpRequestInterceptor> requestFirst = emptyProvider();
        ObjectProvider<HttpRequestInterceptor> requestLast = emptyProvider();
        ObjectProvider<HttpResponseInterceptor> responseFirst = emptyProvider();
        ObjectProvider<HttpResponseInterceptor> responseLast = emptyProvider();
        ConnectionBackoffStrategy backoffStrategy = new DefaultBackoffStrategy();
        HttpClientConnectionManager connManager = HttpClientConnectionManagerBuilder.create().build();
        ConnectionReuseStrategy reuseStrategy = DefaultConnectionReuseStrategy.INSTANCE;
        ConnectionConfig connConfig = ConnectionConfig.custom().build();
        CookieStore cookieStore = new BasicCookieStore();
        CredentialsProvider credsProvider = null;
        RequestConfig reqConfig = RequestConfig.custom().build();
        SocketConfig sockConfig = SocketConfig.custom().build();
        DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;
        ConnectionKeepAliveStrategy keepAliveStrategy = (response, context) -> 30;
        ObjectProvider<AuthenticationStrategy> proxyAuthStrategy = singleProvider(ProxyAuthenticationStrategy.INSTANCE);
        PublicSuffixMatcher suffixMatcher = new PublicSuffixMatcher(Collections.singleton("com"), null);
        RedirectStrategy redirectStrategy = DefaultRedirectStrategy.INSTANCE;
        HttpRequestExecutor requestExecutor = new HttpRequestExecutor();
        org.apache.http.client.HttpRequestRetryHandler retryHandler = null;
        SchemePortResolver schemePortResolver = DefaultSchemePortResolver.INSTANCE;
        ServiceUnavailableRetryStrategy retryStrategy = new DefaultServiceUnavailableRetryStrategy();
        ObjectProvider<AuthenticationStrategy> targetAuthStrategy = singleProvider(TargetAuthenticationStrategy.INSTANCE);
        UserTokenHandler userTokenHandler = DefaultUserTokenHandler.INSTANCE;
        SSLContext sslContext = SSLContext.getDefault();
        HostnameVerifier hostnameVerifier = (hostname, session) -> true;
        LayeredConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactory.getSocketFactory();
        HttpClientProperties properties = new HttpClientProperties();
        properties.setConnManagerShared(false);
        properties.setAuthCachingDisabled(false);
        properties.setAutomaticRetriesDisabled(false);
        properties.setConnectionStateDisabled(false);
        properties.setContentCompressionDisabled(false);
        properties.setCookieManagementDisabled(false);
        properties.setRedirectHandlingDisabled(false);
        HttpClientManagerProperties managerProperties = new HttpClientManagerProperties();
        managerProperties.setMaxIdleTime(60000L);
        managerProperties.setMaxIdleTimeUnit(java.util.concurrent.TimeUnit.MILLISECONDS);

        HttpClientBuilder builder = config.httpClientBuilder(
                requestFirst, requestLast, responseFirst, responseLast,
                backoffStrategy, connManager, reuseStrategy, connConfig,
                cookieStore, null, reqConfig, sockConfig, dnsResolver,
                keepAliveStrategy, proxyAuthStrategy, suffixMatcher,
                redirectStrategy, requestExecutor, retryHandler,
                schemePortResolver, retryStrategy, targetAuthStrategy,
                userTokenHandler, sslContext, hostnameVerifier, sslSocketFactory,
                properties, managerProperties);
        assertThat(builder).isNotNull();
    }

    @Test
    @DisplayName("httpClientBuilder with proxy, credentials, userAgent, and disabled features")
    void testHttpClientBuilderWithAllOptions() throws Exception {
        ObjectProvider<HttpRequestInterceptor> requestFirst = emptyProvider();
        ObjectProvider<HttpRequestInterceptor> requestLast = emptyProvider();
        ObjectProvider<HttpResponseInterceptor> responseFirst = emptyProvider();
        ObjectProvider<HttpResponseInterceptor> responseLast = emptyProvider();
        ConnectionBackoffStrategy backoffStrategy = new DefaultBackoffStrategy();
        HttpClientConnectionManager connManager = HttpClientConnectionManagerBuilder.create().build();
        ConnectionReuseStrategy reuseStrategy = DefaultConnectionReuseStrategy.INSTANCE;
        ConnectionConfig connConfig = ConnectionConfig.custom().build();
        CookieStore cookieStore = new BasicCookieStore();
        RequestConfig reqConfig = RequestConfig.custom().build();
        SocketConfig sockConfig = SocketConfig.custom().build();
        DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;
        ConnectionKeepAliveStrategy keepAliveStrategy = (response, context) -> 30;
        ObjectProvider<AuthenticationStrategy> proxyAuthStrategy = singleProvider(ProxyAuthenticationStrategy.INSTANCE);
        PublicSuffixMatcher suffixMatcher = new PublicSuffixMatcher(Collections.singleton("com"), null);
        RedirectStrategy redirectStrategy = DefaultRedirectStrategy.INSTANCE;
        HttpRequestExecutor requestExecutor = new HttpRequestExecutor();
        SchemePortResolver schemePortResolver = DefaultSchemePortResolver.INSTANCE;
        ServiceUnavailableRetryStrategy retryStrategy = new DefaultServiceUnavailableRetryStrategy();
        ObjectProvider<AuthenticationStrategy> targetAuthStrategy = singleProvider(TargetAuthenticationStrategy.INSTANCE);
        UserTokenHandler userTokenHandler = DefaultUserTokenHandler.INSTANCE;
        SSLContext sslContext = SSLContext.getDefault();
        HostnameVerifier hostnameVerifier = (hostname, session) -> true;
        LayeredConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactory.getSocketFactory();
        HttpClientProperties properties = new HttpClientProperties();
        properties.setProxy(new org.apache.http.HttpHost("proxy.example.com", 8080));
        properties.setUserAgent("TestAgent/1.0");
        properties.setAuthCachingDisabled(true);
        properties.setAutomaticRetriesDisabled(true);
        properties.setConnectionStateDisabled(true);
        properties.setContentCompressionDisabled(true);
        properties.setCookieManagementDisabled(true);
        properties.setRedirectHandlingDisabled(true);
        HttpClientManagerProperties managerProperties = new HttpClientManagerProperties();
        managerProperties.setMaxIdleTime(60000L);
        managerProperties.setMaxIdleTimeUnit(java.util.concurrent.TimeUnit.MILLISECONDS);

        HttpClientBuilder builder = config.httpClientBuilder(
                requestFirst, requestLast, responseFirst, responseLast,
                backoffStrategy, connManager, reuseStrategy, connConfig,
                cookieStore, new org.apache.http.impl.client.BasicCredentialsProvider(),
                reqConfig, sockConfig, dnsResolver,
                keepAliveStrategy, proxyAuthStrategy, suffixMatcher,
                redirectStrategy, requestExecutor, new org.apache.http.impl.client.DefaultHttpRequestRetryHandler(),
                schemePortResolver, retryStrategy, targetAuthStrategy,
                userTokenHandler, sslContext, hostnameVerifier, sslSocketFactory,
                properties, managerProperties);
        assertThat(builder).isNotNull();
    }

    @SuppressWarnings("unchecked")
    private <T> ObjectProvider<T> emptyProvider() {
        return new ObjectProvider<T>() {
            @Override public T getObject(Object... args) throws org.springframework.beans.BeansException { return null; }
            @Override public T getObject() throws org.springframework.beans.BeansException { return null; }
            @Override public T getIfAvailable() throws org.springframework.beans.BeansException { return null; }
            @Override public T getIfUnique() throws org.springframework.beans.BeansException { return null; }
            @Override public Stream<T> orderedStream() { return Stream.empty(); }
            @Override public Stream<T> stream() { return Stream.empty(); }
        };
    }

    @SuppressWarnings("unchecked")
    private <T> ObjectProvider<T> singleProvider(T value) {
        return new ObjectProvider<T>() {
            @Override public T getObject(Object... args) throws org.springframework.beans.BeansException { return value; }
            @Override public T getObject() throws org.springframework.beans.BeansException { return value; }
            @Override public T getIfAvailable() throws org.springframework.beans.BeansException { return value; }
            @Override public T getIfUnique() throws org.springframework.beans.BeansException { return value; }
            @Override public Stream<T> orderedStream() { return Stream.of(value); }
            @Override public Stream<T> stream() { return Stream.of(value); }
        };
    }
}
