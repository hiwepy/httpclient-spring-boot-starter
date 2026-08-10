package org.apache.http.spring.boot.client;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {{ @link HttpClientManagerProperties }}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("HttpClientManagerProperties Tests")
class HttpClientManagerPropertiesTest {

    @Test
    @DisplayName("Default constructor creates non-null instance")
    void testDefaultInstance() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        assertThat(props).isNotNull();
    }

    @Test
    @DisplayName("connectTimeout getter/setter")
    void testConnectTimeout() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        props.setConnectTimeout(5000);
        assertThat(props.getConnectTimeout()).isEqualTo(5000);
    }

    @Test
    @DisplayName("socketTimeout getter/setter")
    void testSocketTimeout() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        props.setSocketTimeout(3000);
        assertThat(props.getSocketTimeout()).isEqualTo(3000);
    }

    @Test
    @DisplayName("protocol getter/setter")
    void testProtocol() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        props.setProtocol("https");
        assertThat(props.getProtocol()).isEqualTo("https");
    }

    @Test
    @DisplayName("evictExpiredConnections getter/setter")
    void testEvictExpiredConnections() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        props.setEvictExpiredConnections(true);
        assertThat(props.isEvictExpiredConnections()).isTrue();
    }

    @Test
    @DisplayName("evictIdleConnections getter/setter")
    void testEvictIdleConnections() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        props.setEvictIdleConnections(true);
        assertThat(props.isEvictIdleConnections()).isTrue();
    }

    @Test
    @DisplayName("maxIdleTime getter/setter")
    void testMaxIdleTime() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        props.setMaxIdleTime(60000L);
        assertThat(props.getMaxIdleTime()).isEqualTo(60000L);
    }

    @Test
    @DisplayName("maxIdleTimeUnit getter/setter")
    void testMaxIdleTimeUnit() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        props.setMaxIdleTimeUnit(TimeUnit.SECONDS);
        assertThat(props.getMaxIdleTimeUnit()).isEqualTo(TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("systemProperties getter/setter")
    void testSystemProperties() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        props.setSystemProperties(true);
        assertThat(props.isSystemProperties()).isTrue();
    }

    @Test
    @DisplayName("redirectHandlingDisabled getter/setter")
    void testRedirectHandlingDisabled() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        props.setRedirectHandlingDisabled(true);
        assertThat(props.isRedirectHandlingDisabled()).isTrue();
    }

    @Test
    @DisplayName("automaticRetriesDisabled getter/setter")
    void testAutomaticRetriesDisabled() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        props.setAutomaticRetriesDisabled(true);
        assertThat(props.isAutomaticRetriesDisabled()).isTrue();
    }

    @Test
    @DisplayName("contentCompressionDisabled getter/setter")
    void testContentCompressionDisabled() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        props.setContentCompressionDisabled(true);
        assertThat(props.isContentCompressionDisabled()).isTrue();
    }

    @Test
    @DisplayName("cookieManagementDisabled getter/setter")
    void testCookieManagementDisabled() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        props.setCookieManagementDisabled(true);
        assertThat(props.isCookieManagementDisabled()).isTrue();
    }

    @Test
    @DisplayName("authCachingDisabled getter/setter")
    void testAuthCachingDisabled() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        props.setAuthCachingDisabled(true);
        assertThat(props.isAuthCachingDisabled()).isTrue();
    }

    @Test
    @DisplayName("connectionStateDisabled getter/setter")
    void testConnectionStateDisabled() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        props.setConnectionStateDisabled(true);
        assertThat(props.isConnectionStateDisabled()).isTrue();
    }

    @Test
    @DisplayName("maxConnTotal getter/setter")
    void testMaxConnTotal() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        props.setMaxConnTotal(200);
        assertThat(props.getMaxConnTotal()).isEqualTo(200);
    }

    @Test
    @DisplayName("maxConnPerRoute getter/setter")
    void testMaxConnPerRoute() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        props.setMaxConnPerRoute(50);
        assertThat(props.getMaxConnPerRoute()).isEqualTo(50);
    }

    @Test
    @DisplayName("connTimeToLive getter/setter")
    void testConnTimeToLive() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        props.setConnTimeToLive(30000L);
        assertThat(props.getConnTimeToLive()).isEqualTo(30000L);
    }

    @Test
    @DisplayName("connTimeToLiveTimeUnit getter/setter")
    void testConnTimeToLiveTimeUnit() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        props.setConnTimeToLiveTimeUnit(TimeUnit.MINUTES);
        assertThat(props.getConnTimeToLiveTimeUnit()).isEqualTo(TimeUnit.MINUTES);
    }

    @Test
    @DisplayName("proxy getter/setter")
    void testProxy() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        HttpHost proxy = new HttpHost("localhost", 8080);
        props.setProxy(proxy);
        assertThat(props.getProxy()).isEqualTo(proxy);
    }

    @Test
    @DisplayName("userAgent getter/setter")
    void testUserAgent() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        props.setUserAgent("TestAgent/1.0");
        assertThat(props.getUserAgent()).isEqualTo("TestAgent/1.0");
    }

    @Test
    @DisplayName("getBooleanProperty reads from Properties")
    void testGetBooleanProperty() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        Properties p = new Properties();
        p.setProperty("http.connection.dns.lookups-disabled", "true");
        // Note: getBooleanProperty uses Boolean.getBoolean() which checks system properties
        boolean result = props.getBooleanProperty(p, HttpClientParams.HTTP_CONNECTION_DNS_LOOKUPS_DISABLED);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("getIntProperty reads from Properties")
    void testGetIntProperty() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        Properties p = new Properties();
        p.setProperty("http.connection.maxPoolSize", "100");
        int result = props.getIntProperty(p, HttpClientParams.HTTP_CONNECTION_MAX_POOLSIZE);
        assertThat(result).isEqualTo(100);
    }

    @Test
    @DisplayName("getLongProperty reads from Properties")
    void testGetLongProperty() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        Properties p = new Properties();
        p.setProperty("http.connection.keepAlive", "60000");
        long result = props.getLongProperty(p, HttpClientParams.HTTP_CONNECTION_KEEPALIVE);
        assertThat(result).isEqualTo(60000L);
    }

    @Test
    @DisplayName("getStringProperty reads from Properties")
    void testGetStringProperty() {
        HttpClientManagerProperties props = new HttpClientManagerProperties();
        Properties p = new Properties();
        p.setProperty("http.connection.config-charset", "UTF-8");
        String result = props.getStringProperty(p, HttpClientParams.HTTP_CONNECTION_CONFIG_CHARSET);
        assertThat(result).isEqualTo("UTF-8");
    }
}
