package org.apache.http.spring.boot;

import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.spring.boot.client.HttpClientConnectionManagerBuilder;
import org.apache.http.spring.boot.client.HttpClientManagerProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.httpclient.HttpClientMetricNameStrategy;
import com.codahale.metrics.httpclient.InstrumentedHttpClientConnectionManager;
import com.codahale.metrics.httpclient.InstrumentedHttpRequestExecutor;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.X509TrustManager;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HttpClientMetricAutoConfiguration Tests")
class HttpClientMetricAutoConfigurationTest {

    private final HttpClientMetricAutoConfiguration config = new HttpClientMetricAutoConfiguration();

    @Test
    @DisplayName("Auto-configuration class can be instantiated")
    void testInstantiation() {
        assertThat(config).isNotNull();
    }

    @Test
    @DisplayName("Auto-configuration class exists")
    void testClassExists() {
        assertThat(HttpClientMetricAutoConfiguration.class).isNotNull();
    }

    @Test
    @DisplayName("Auto-configuration is a configuration class")
    void testIsConfiguration() {
        assertThat(HttpClientMetricAutoConfiguration.class.isAnnotationPresent(
                org.springframework.context.annotation.Configuration.class)).isTrue();
    }

    @Test
    @DisplayName("metricsRegistry bean is created")
    void testMetricsRegistry() {
        MetricRegistry registry = config.metricsRegistry();
        assertThat(registry).isNotNull();
    }

    @Test
    @DisplayName("metricNameStrategy bean is created")
    void testMetricNameStrategy() {
        HttpClientMetricNameStrategy strategy = config.metricNameStrategy();
        assertThat(strategy).isNotNull();
        String name = strategy.getNameFor("test", new org.apache.http.message.BasicHttpRequest("GET", "/"));
        assertThat(name).contains("test");
    }

    @Test
    @DisplayName("httpRequestExecutor bean is created")
    void testHttpRequestExecutor() {
        MetricRegistry registry = new MetricRegistry();
        HttpClientMetricNameStrategy strategy = config.metricNameStrategy();
        HttpClientMetricProperties properties = new HttpClientMetricProperties();
        properties.setName("testClient");
        properties.setWaitForContinue(5000);
        HttpRequestExecutor executor = config.httpRequestExecutor(registry, strategy, properties);
        assertThat(executor).isNotNull();
        assertThat(executor).isInstanceOf(InstrumentedHttpRequestExecutor.class);
    }

    @Test
    @DisplayName("connectionManagerBuilder bean with metrics is created")
    void testConnectionManagerBuilder() {
        ConnectionConfig connConfig = ConnectionConfig.custom().build();
        RequestConfig reqConfig = RequestConfig.custom().build();
        SocketConfig sockConfig = SocketConfig.custom().build();
        DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;
        ConnectionKeepAliveStrategy keepAliveStrategy = (response, context) -> 30;
        PublicSuffixMatcher suffixMatcher = new PublicSuffixMatcher(Collections.singleton("com"), null);
        SchemePortResolver schemePortResolver = (host) -> 443;
        org.apache.http.client.ServiceUnavailableRetryStrategy retryStrategy =
                new org.apache.http.impl.client.DefaultServiceUnavailableRetryStrategy();
        HostnameVerifier hostnameVerifier = (hostname, session) -> true;
        X509TrustManager trustManager = new javax.net.ssl.X509TrustManager() {
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}
            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return new java.security.cert.X509Certificate[0]; }
        };
        HttpClientManagerProperties properties = new HttpClientManagerProperties();
        properties.setMaxConnPerRoute(20);
        properties.setMaxConnTotal(200);
        MetricRegistry registry = new MetricRegistry();
        HttpClientMetricProperties metricProperties = new HttpClientMetricProperties();
        metricProperties.setName("testClient");

        HttpClientConnectionManagerBuilder builder = config.connectionManagerBuilder(
                connConfig, reqConfig, sockConfig, dnsResolver, keepAliveStrategy,
                suffixMatcher, schemePortResolver, retryStrategy,
                hostnameVerifier, trustManager, properties, registry, metricProperties);
        assertThat(builder).isNotNull();
    }
}
