package org.apache.http.spring.boot;

import org.apache.http.HttpHost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HttpClientProperties Tests")
class HttpClientPropertiesTest {

    @Test
    @DisplayName("Default constructor creates non-null instance")
    void testDefaultInstance() {
        HttpClientProperties props = new HttpClientProperties();
        assertThat(props).isNotNull();
    }

    @Test
    @DisplayName("PREFIX constant")
    void testPrefix() {
        assertThat(HttpClientProperties.PREFIX).isEqualTo("httpclient");
    }

    @Test
    @DisplayName("gzip getter/setter")
    void testGzip() {
        HttpClientProperties props = new HttpClientProperties();
        assertThat(props.isGzip()).isFalse();
        props.setGzip(true);
        assertThat(props.isGzip()).isTrue();
    }

    @Test
    @DisplayName("keepAlive getter/setter")
    void testKeepAlive() {
        HttpClientProperties props = new HttpClientProperties();
        props.setKeepAlive(60000L);
        assertThat(props.getKeepAlive()).isEqualTo(60000L);
    }

    @Test
    @DisplayName("maxPoolSize getter/setter")
    void testMaxPoolSize() {
        HttpClientProperties props = new HttpClientProperties();
        props.setMaxPoolSize(100);
        assertThat(props.getMaxPoolSize()).isEqualTo(100);
    }

    @Test
    @DisplayName("maxRetries getter/setter")
    void testMaxRetries() {
        HttpClientProperties props = new HttpClientProperties();
        props.setMaxRetries(5);
        assertThat(props.getMaxRetries()).isEqualTo(5);
    }

    @Test
    @DisplayName("retryInterval getter/setter")
    void testRetryInterval() {
        HttpClientProperties props = new HttpClientProperties();
        props.setRetryInterval(2000);
        assertThat(props.getRetryInterval()).isEqualTo(2000);
    }

    @Test
    @DisplayName("maxLineLength getter/setter")
    void testMaxLineLength() {
        HttpClientProperties props = new HttpClientProperties();
        props.setMaxLineLength(4000);
        assertThat(props.getMaxLineLength()).isEqualTo(4000);
    }

    @Test
    @DisplayName("maxHeaderCount getter/setter")
    void testMaxHeaderCount() {
        HttpClientProperties props = new HttpClientProperties();
        props.setMaxHeaderCount(500);
        assertThat(props.getMaxHeaderCount()).isEqualTo(500);
    }

    @Test
    @DisplayName("charset getter/setter")
    void testCharset() {
        HttpClientProperties props = new HttpClientProperties();
        props.setCharset("ISO-8859-1");
        assertThat(props.getCharset()).isEqualTo("ISO-8859-1");
    }

    @Test
    @DisplayName("connManagerShared getter/setter")
    void testConnManagerShared() {
        HttpClientProperties props = new HttpClientProperties();
        props.setConnManagerShared(true);
        assertThat(props.isConnManagerShared()).isTrue();
    }

    @Test
    @DisplayName("systemProperties getter/setter")
    void testSystemProperties() {
        HttpClientProperties props = new HttpClientProperties();
        props.setSystemProperties(true);
        assertThat(props.isSystemProperties()).isTrue();
    }

    @Test
    @DisplayName("redirectHandlingDisabled getter/setter")
    void testRedirectHandlingDisabled() {
        HttpClientProperties props = new HttpClientProperties();
        props.setRedirectHandlingDisabled(true);
        assertThat(props.isRedirectHandlingDisabled()).isTrue();
    }

    @Test
    @DisplayName("automaticRetriesDisabled getter/setter")
    void testAutomaticRetriesDisabled() {
        HttpClientProperties props = new HttpClientProperties();
        props.setAutomaticRetriesDisabled(true);
        assertThat(props.isAutomaticRetriesDisabled()).isTrue();
    }

    @Test
    @DisplayName("contentCompressionDisabled getter/setter")
    void testContentCompressionDisabled() {
        HttpClientProperties props = new HttpClientProperties();
        props.setContentCompressionDisabled(true);
        assertThat(props.isContentCompressionDisabled()).isTrue();
    }

    @Test
    @DisplayName("cookieManagementDisabled getter/setter")
    void testCookieManagementDisabled() {
        HttpClientProperties props = new HttpClientProperties();
        props.setCookieManagementDisabled(true);
        assertThat(props.isCookieManagementDisabled()).isTrue();
    }

    @Test
    @DisplayName("authCachingDisabled getter/setter")
    void testAuthCachingDisabled() {
        HttpClientProperties props = new HttpClientProperties();
        props.setAuthCachingDisabled(true);
        assertThat(props.isAuthCachingDisabled()).isTrue();
    }

    @Test
    @DisplayName("connectionStateDisabled getter/setter")
    void testConnectionStateDisabled() {
        HttpClientProperties props = new HttpClientProperties();
        props.setConnectionStateDisabled(true);
        assertThat(props.isConnectionStateDisabled()).isTrue();
    }

    @Test
    @DisplayName("proxy getter/setter")
    void testProxy() {
        HttpClientProperties props = new HttpClientProperties();
        HttpHost proxy = new HttpHost("proxy.example.com", 8080);
        props.setProxy(proxy);
        assertThat(props.getProxy()).isEqualTo(proxy);
    }

    @Test
    @DisplayName("userAgent getter/setter")
    void testUserAgent() {
        HttpClientProperties props = new HttpClientProperties();
        props.setUserAgent("TestAgent/1.0");
        assertThat(props.getUserAgent()).isEqualTo("TestAgent/1.0");
    }

    @Test
    @DisplayName("bufferRequestBody getter/setter")
    void testBufferRequestBody() {
        HttpClientProperties props = new HttpClientProperties();
        assertThat(props.isBufferRequestBody()).isTrue();
        props.setBufferRequestBody(false);
        assertThat(props.isBufferRequestBody()).isFalse();
    }
}
