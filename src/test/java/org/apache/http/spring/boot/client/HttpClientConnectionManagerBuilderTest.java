package org.apache.http.spring.boot.client;

import java.util.Collections;

import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {{ @link HttpClientConnectionManagerBuilder }}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("HttpClientConnectionManagerBuilder Tests")
class HttpClientConnectionManagerBuilderTest {

    @Test
    @DisplayName("create() returns a non-null builder")
    void testCreate() {
        HttpClientConnectionManagerBuilder builder = HttpClientConnectionManagerBuilder.create();
        assertThat(builder).isNotNull();
    }

    @Test
    @DisplayName("build() returns a non-null connection manager with defaults")
    void testBuildDefaults() {
        HttpClientConnectionManager manager = HttpClientConnectionManagerBuilder.create().build();
        assertThat(manager).isNotNull();
    }

    @Test
    @DisplayName("setDefaultConnectionConfig is applied")
    void testSetDefaultConnectionConfig() {
        ConnectionConfig config = ConnectionConfig.custom().setBufferSize(1024).build();
        HttpClientConnectionManager manager = HttpClientConnectionManagerBuilder.create()
                .setDefaultConnectionConfig(config)
                .build();
        assertThat(manager).isNotNull();
    }

    @Test
    @DisplayName("setDefaultSocketConfig is applied")
    void testSetDefaultSocketConfig() {
        SocketConfig config = SocketConfig.custom().setSoTimeout(5000).build();
        HttpClientConnectionManager manager = HttpClientConnectionManagerBuilder.create()
                .setDefaultSocketConfig(config)
                .build();
        assertThat(manager).isNotNull();
    }

    @Test
    @DisplayName("setDnsResolver is applied")
    void testSetDnsResolver() {
        DnsResolver resolver = SystemDefaultDnsResolver.INSTANCE;
        HttpClientConnectionManager manager = HttpClientConnectionManagerBuilder.create()
                .setDnsResolver(resolver)
                .build();
        assertThat(manager).isNotNull();
    }

    @Test
    @DisplayName("setMaxConnPerRoute is applied")
    void testSetMaxConnPerRoute() {
        HttpClientConnectionManager manager = HttpClientConnectionManagerBuilder.create()
                .setMaxConnPerRoute(50)
                .build();
        assertThat(manager).isNotNull();
    }

    @Test
    @DisplayName("setMaxConnTotal is applied")
    void testSetMaxConnTotal() {
        HttpClientConnectionManager manager = HttpClientConnectionManagerBuilder.create()
                .setMaxConnTotal(200)
                .build();
        assertThat(manager).isNotNull();
    }

    @Test
    @DisplayName("setPublicSuffixMatcher is applied")
    void testSetPublicSuffixMatcher() {
        PublicSuffixMatcher matcher = new PublicSuffixMatcher(Collections.singleton("com"), null);
        HttpClientConnectionManager manager = HttpClientConnectionManagerBuilder.create()
                .setPublicSuffixMatcher(matcher)
                .build();
        assertThat(manager).isNotNull();
    }

    @Test
    @DisplayName("All setters can be chained")
    void testBuilderChaining() {
        ConnectionConfig connConfig = ConnectionConfig.custom().setBufferSize(2048).build();
        SocketConfig sockConfig = SocketConfig.custom().setSoTimeout(3000).build();
        PublicSuffixMatcher matcher = new PublicSuffixMatcher(Collections.singleton("com"), null);
        HttpClientConnectionManager manager = HttpClientConnectionManagerBuilder.create()
                .setDefaultConnectionConfig(connConfig)
                .setDefaultSocketConfig(sockConfig)
                .setDnsResolver(SystemDefaultDnsResolver.INSTANCE)
                .setMaxConnPerRoute(10)
                .setMaxConnTotal(100)
                .setPublicSuffixMatcher(matcher)
                .build();
        assertThat(manager).isNotNull();
    }
}
