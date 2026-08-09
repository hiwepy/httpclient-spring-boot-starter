package org.apache.http.spring.boot.client;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.http.HttpHost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HttpClientRequestProperties Tests")
class HttpClientRequestPropertiesTest {

    @Test
    @DisplayName("Default constructor creates non-null instance")
    void testDefaultInstance() {
        HttpClientRequestProperties props = new HttpClientRequestProperties();
        assertThat(props).isNotNull();
    }

    @Test
    @DisplayName("expectContinueEnabled getter/setter")
    void testExpectContinueEnabled() {
        HttpClientRequestProperties props = new HttpClientRequestProperties();
        props.setExpectContinueEnabled(true);
        assertThat(props.isExpectContinueEnabled()).isTrue();
    }

    @Test
    @DisplayName("proxy getter/setter")
    void testProxy() {
        HttpClientRequestProperties props = new HttpClientRequestProperties();
        HttpHost proxy = new HttpHost("proxy.example.com", 8080);
        props.setProxy(proxy);
        assertThat(props.getProxy()).isEqualTo(proxy);
    }

    @Test
    @DisplayName("staleConnectionCheckEnabled getter/setter")
    void testStaleConnectionCheckEnabled() {
        HttpClientRequestProperties props = new HttpClientRequestProperties();
        props.setStaleConnectionCheckEnabled(true);
        assertThat(props.isStaleConnectionCheckEnabled()).isTrue();
    }

    @Test
    @DisplayName("cookieSpec getter/setter")
    void testCookieSpec() {
        HttpClientRequestProperties props = new HttpClientRequestProperties();
        props.setCookieSpec("standard");
        assertThat(props.getCookieSpec()).isEqualTo("standard");
    }

    @Test
    @DisplayName("redirectsEnabled getter/setter")
    void testRedirectsEnabled() {
        HttpClientRequestProperties props = new HttpClientRequestProperties();
        props.setRedirectsEnabled(false);
        assertThat(props.isRedirectsEnabled()).isFalse();
    }

    @Test
    @DisplayName("relativeRedirectsAllowed getter/setter")
    void testRelativeRedirectsAllowed() {
        HttpClientRequestProperties props = new HttpClientRequestProperties();
        props.setRelativeRedirectsAllowed(false);
        assertThat(props.isRelativeRedirectsAllowed()).isFalse();
    }

    @Test
    @DisplayName("circularRedirectsAllowed getter/setter")
    void testCircularRedirectsAllowed() {
        HttpClientRequestProperties props = new HttpClientRequestProperties();
        props.setCircularRedirectsAllowed(true);
        assertThat(props.isCircularRedirectsAllowed()).isTrue();
    }

    @Test
    @DisplayName("maxRedirects getter/setter")
    void testMaxRedirects() {
        HttpClientRequestProperties props = new HttpClientRequestProperties();
        props.setMaxRedirects(5);
        assertThat(props.getMaxRedirects()).isEqualTo(5);
    }

    @Test
    @DisplayName("authenticationEnabled getter/setter")
    void testAuthenticationEnabled() {
        HttpClientRequestProperties props = new HttpClientRequestProperties();
        props.setAuthenticationEnabled(false);
        assertThat(props.isAuthenticationEnabled()).isFalse();
    }

    @Test
    @DisplayName("targetPreferredAuthSchemes getter/setter")
    void testTargetPreferredAuthSchemes() {
        HttpClientRequestProperties props = new HttpClientRequestProperties();
        Collection<String> schemes = Arrays.asList("NTLM", "Digest");
        props.setTargetPreferredAuthSchemes(schemes);
        assertThat(props.getTargetPreferredAuthSchemes()).containsExactly("NTLM", "Digest");
    }

    @Test
    @DisplayName("proxyPreferredAuthSchemes getter/setter")
    void testProxyPreferredAuthSchemes() {
        HttpClientRequestProperties props = new HttpClientRequestProperties();
        Collection<String> schemes = Collections.singletonList("Basic");
        props.setProxyPreferredAuthSchemes(schemes);
        assertThat(props.getProxyPreferredAuthSchemes()).containsExactly("Basic");
    }

    @Test
    @DisplayName("connectionRequestTimeout getter/setter")
    void testConnectionRequestTimeout() {
        HttpClientRequestProperties props = new HttpClientRequestProperties();
        props.setConnectionRequestTimeout(3000);
        assertThat(props.getConnectionRequestTimeout()).isEqualTo(3000);
    }

    @Test
    @DisplayName("connectTimeout getter/setter")
    void testConnectTimeout() {
        HttpClientRequestProperties props = new HttpClientRequestProperties();
        props.setConnectTimeout(5000);
        assertThat(props.getConnectTimeout()).isEqualTo(5000);
    }

    @Test
    @DisplayName("socketTimeout getter/setter")
    void testSocketTimeout() {
        HttpClientRequestProperties props = new HttpClientRequestProperties();
        props.setSocketTimeout(10000);
        assertThat(props.getSocketTimeout()).isEqualTo(10000);
    }

    @Test
    @DisplayName("contentCompressionEnabled getter/setter")
    void testContentCompressionEnabled() {
        HttpClientRequestProperties props = new HttpClientRequestProperties();
        props.setContentCompressionEnabled(false);
        assertThat(props.isContentCompressionEnabled()).isFalse();
    }

    @Test
    @DisplayName("retryCount getter/setter")
    void testRetryCount() {
        HttpClientRequestProperties props = new HttpClientRequestProperties();
        props.setRetryCount(3);
        assertThat(props.getRetryCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("requestSentRetryEnabled getter/setter")
    void testRequestSentRetryEnabled() {
        HttpClientRequestProperties props = new HttpClientRequestProperties();
        props.setRequestSentRetryEnabled(true);
        assertThat(props.isRequestSentRetryEnabled()).isTrue();
    }

    @Test
    @DisplayName("localAddress getter/setter")
    void testLocalAddress() throws Exception {
        HttpClientRequestProperties props = new HttpClientRequestProperties();
        InetAddress addr = InetAddress.getByName("127.0.0.1");
        props.setLocalAddress(addr);
        assertThat(props.getLocalAddress()).isEqualTo(addr);
    }
}
