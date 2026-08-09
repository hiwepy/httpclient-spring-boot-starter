package org.apache.http.spring.boot;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.ConnectionBackoffStrategy;
import org.apache.http.client.CookieStore;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.spring.boot.client.HttpClientConnectionProperties;
import org.apache.http.spring.boot.client.HttpClientRequestProperties;
import org.apache.http.spring.boot.client.HttpClientSocketProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HttpClientDependsOnAutoConfiguration Tests")
class HttpClientDependsOnAutoConfigurationTest {

    private final HttpClientDependsOnAutoConfiguration config = new HttpClientDependsOnAutoConfiguration();

    @Test
    @DisplayName("Auto-configuration class can be instantiated")
    void testInstantiation() {
        assertThat(config).isNotNull();
    }

    @Test
    @DisplayName("Auto-configuration class exists")
    void testClassExists() {
        assertThat(HttpClientDependsOnAutoConfiguration.class).isNotNull();
    }

    @Test
    @DisplayName("Auto-configuration is a configuration class")
    void testIsConfiguration() {
        assertThat(HttpClientDependsOnAutoConfiguration.class.isAnnotationPresent(
                org.springframework.context.annotation.Configuration.class)).isTrue();
    }

    @Test
    @DisplayName("socketConfig bean is created from properties")
    void testSocketConfig() {
        HttpClientSocketProperties props = new HttpClientSocketProperties();
        props.setSoTimeout(5000);
        props.setTcpNoDelay(true);
        props.setSoKeepAlive(true);
        props.setSoLinger(30);
        props.setSoReuseAddress(true);
        props.setSndBufSize(8192);
        props.setRcvBufSize(16384);
        props.setBacklogSize(128);
        SocketConfig sc = config.socketConfig(props);
        assertThat(sc).isNotNull();
    }

    @Test
    @DisplayName("requestConfig bean is created from properties")
    void testRequestConfig() {
        HttpClientRequestProperties props = new HttpClientRequestProperties();
        props.setAuthenticationEnabled(true);
        props.setCircularRedirectsAllowed(false);
        props.setConnectionRequestTimeout(3000);
        props.setConnectTimeout(5000);
        props.setContentCompressionEnabled(true);
        props.setCookieSpec("standard");
        props.setExpectContinueEnabled(true);
        props.setMaxRedirects(5);
        props.setRedirectsEnabled(true);
        props.setRelativeRedirectsAllowed(true);
        props.setSocketTimeout(10000);
        RequestConfig rc = config.requestConfig(props);
        assertThat(rc).isNotNull();
    }

    @Test
    @DisplayName("connectionBackoffStrategy bean")
    void testConnectionBackoffStrategy() {
        ConnectionBackoffStrategy strategy = config.connectionBackoffStrategy();
        assertThat(strategy).isNotNull();
    }

    @Test
    @DisplayName("reuseStrategy bean")
    void testReuseStrategy() {
        ConnectionReuseStrategy strategy = config.reuseStrategy();
        assertThat(strategy).isNotNull();
    }

    @Test
    @DisplayName("cookieStore bean")
    void testCookieStore() {
        CookieStore store = config.cookieStore();
        assertThat(store).isNotNull();
    }

    @Test
    @DisplayName("dnsResolver bean")
    void testDnsResolver() {
        DnsResolver resolver = config.dnsResolver();
        assertThat(resolver).isNotNull();
    }

    @Test
    @DisplayName("proxyAuthStrategy bean")
    void testProxyAuthStrategy() {
        AuthenticationStrategy strategy = config.proxyAuthStrategy();
        assertThat(strategy).isNotNull();
    }

    @Test
    @DisplayName("httpRequestExecutor bean")
    void testHttpRequestExecutor() {
        HttpRequestExecutor executor = config.httpRequestExecutor();
        assertThat(executor).isNotNull();
    }

    @Test
    @DisplayName("redirectStrategy bean")
    void testRedirectStrategy() {
        RedirectStrategy strategy = config.redirectStrategy();
        assertThat(strategy).isNotNull();
    }

    @Test
    @DisplayName("schemePortResolver bean")
    void testSchemePortResolver() {
        SchemePortResolver resolver = config.schemePortResolver();
        assertThat(resolver).isNotNull();
    }

    @Test
    @DisplayName("serviceUnavailStrategy bean")
    void testServiceUnavailStrategy() {
        HttpClientProperties props = new HttpClientProperties();
        props.setMaxRetries(3);
        props.setRetryInterval(1000);
        ServiceUnavailableRetryStrategy strategy = config.serviceUnavailStrategy(props);
        assertThat(strategy).isNotNull();
    }

    @Test
    @DisplayName("targetAuthStrategy bean")
    void testTargetAuthStrategy() {
        AuthenticationStrategy strategy = config.targetAuthStrategy();
        assertThat(strategy).isNotNull();
    }

    @Test
    @DisplayName("userTokenHandler bean")
    void testUserTokenHandler() {
        UserTokenHandler handler = config.userTokenHandler();
        assertThat(handler).isNotNull();
    }

    @Test
    @DisplayName("httpClientConnectionProperties bean")
    void testHttpClientConnectionProperties() {
        HttpClientConnectionProperties props = new HttpClientConnectionProperties();
        HttpClientConnectionProperties result = config.httpClientConnectionProperties(props);
        assertThat(result).isSameAs(props);
    }
}
