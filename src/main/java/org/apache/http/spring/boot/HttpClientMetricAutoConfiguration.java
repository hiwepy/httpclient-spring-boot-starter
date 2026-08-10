/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.http.spring.boot;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.spring.boot.client.HttpClientConnectionManagerBuilder;
import org.apache.http.spring.boot.client.HttpClientManagerProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.httpclient.HttpClientMetricNameStrategy;
import com.codahale.metrics.httpclient.InstrumentedHttpClientConnectionManager;
import com.codahale.metrics.httpclient.InstrumentedHttpRequestExecutor;

/**
 * Optional Dropwizard Metrics-backed auto-configuration that instruments the HttpClient connection
 * manager and request executor, activated when {@code httpclient.metrics.enabled=true} and the
 * Metrics library is on the classpath.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass({ MetricRegistry.class, HttpRequestExecutor.class, InstrumentedHttpRequestExecutor.class })
@ConditionalOnProperty(prefix = HttpClientMetricProperties.PREFIX, value = "enabled", havingValue = "true")
@AutoConfigureAfter(HttpClientDependsOnAutoConfiguration.class)
@EnableConfigurationProperties(value = { HttpClientManagerProperties.class, HttpClientMetricProperties.class })
public class HttpClientMetricAutoConfiguration {

	/** Provide a Dropwizard {@link MetricRegistry} unless one already exists. @return a new MetricRegistry */
	@Bean
	@ConditionalOnMissingBean(MetricRegistry.class)
	public MetricRegistry metricsRegistry() {
		return new MetricRegistry();
	}

	/** Provide a default {@link HttpClientMetricNameStrategy} that namespaces metrics by HttpClient and name unless one already exists. @return a metric name strategy */
	@Bean
	@ConditionalOnMissingBean
	public HttpClientMetricNameStrategy metricNameStrategy() {
		return new HttpClientMetricNameStrategy() {
			@Override
			public String getNameFor(String name, HttpRequest request) {
				return MetricRegistry.name(HttpClient.class, name, request.getClass().getSimpleName());
			}
		};
	}

	/** Provide a Dropwizard-instrumented {@link HttpRequestExecutor} that records per-request metrics. @param metricsRegistry metric registry @param metricNameStrategy metric name strategy @param properties metric properties @return an InstrumentedHttpRequestExecutor */
	@Bean
	public HttpRequestExecutor httpRequestExecutor(MetricRegistry metricsRegistry,
			HttpClientMetricNameStrategy metricNameStrategy, HttpClientMetricProperties properties) {
		return new InstrumentedHttpRequestExecutor(metricsRegistry, metricNameStrategy, properties.getName(),
				properties.getWaitForContinue());
	}

	/**
	 * Build an instrumented {@link HttpClientConnectionManagerBuilder} that produces a Dropwizard-metrics
	 * aware pooling connection manager, overriding the default builder produced by
	 * {@code HttpClientBuilderAutoConfiguration}.
	 * @param connectionConfig default connection configuration
	 * @param requestConfig default request configuration
	 * @param socketConfig default socket configuration
	 * @param dnsResolver DNS resolver
	 * @param keepAliveStrategy keep-alive strategy
	 * @param publicSuffixMatcher public suffix matcher
	 * @param schemePortResolver scheme port resolver
	 * @param serviceUnavailStrategy service-unavailable retry strategy
	 * @param hostnameVerifier SSL hostname verifier
	 * @param trustManager X509 trust manager
	 * @param properties connection manager properties
	 * @param metricsRegistry metric registry
	 * @param metricProperties metric properties
	 * @return an instrumented connection manager builder
	 */
	@Bean
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
			HttpClientManagerProperties properties,
			MetricRegistry metricsRegistry,
			HttpClientMetricProperties metricProperties) {
		return InstrumentedHttpClientConnectionManagerBuilder.create(metricsRegistry, metricProperties.getName())
				.setDefaultConnectionConfig(connectionConfig)
				.setDefaultSocketConfig(socketConfig)
				.setDnsResolver(dnsResolver)
				.setMaxConnPerRoute(properties.getMaxConnPerRoute())
				.setMaxConnTotal(properties.getMaxConnTotal())
				.setPublicSuffixMatcher(publicSuffixMatcher);
	}
	

	/**
	 * Connection manager builder that produces Dropwizard-instrumented
	 * {@link InstrumentedHttpClientConnectionManager} instances.
	 */
	static class InstrumentedHttpClientConnectionManagerBuilder extends HttpClientConnectionManagerBuilder {

		private final MetricRegistry metricsRegistry;
		private final String name;

		/** Create a new instrumented connection manager builder. @param metricsRegistry metric registry @param name metric name @return a new builder */
		public static InstrumentedHttpClientConnectionManagerBuilder create(MetricRegistry metricsRegistry,
				String name) {
			return new InstrumentedHttpClientConnectionManagerBuilder(metricsRegistry, name);
		}

		/** Construct an instrumented connection manager builder. @param metricsRegistry metric registry @param name metric name */
		protected InstrumentedHttpClientConnectionManagerBuilder(MetricRegistry metricsRegistry, String name) {
			super();
			this.metricsRegistry = metricsRegistry;
			this.name = name;
		}

		/** Create the underlying instrumented pooling connection manager. @param socketFactoryRegistry socket factory registry @param connFactory connection factory @param schemePortResolver scheme port resolver @param dnsResolver DNS resolver @param connTimeToLive connection time-to-live @param connTimeToLiveTimeUnit time-to-live unit @return an InstrumentedHttpClientConnectionManager */
		@Override
		protected PoolingHttpClientConnectionManager instance(Registry<ConnectionSocketFactory> socketFactoryRegistry,
				HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory,
				SchemePortResolver schemePortResolver, DnsResolver dnsResolver, long connTimeToLive,
				TimeUnit connTimeToLiveTimeUnit) {
			return new InstrumentedHttpClientConnectionManager(metricsRegistry, socketFactoryRegistry, connFactory,
					schemePortResolver, dnsResolver, connTimeToLive, connTimeToLiveTimeUnit, name);
		}

	}
	

}
