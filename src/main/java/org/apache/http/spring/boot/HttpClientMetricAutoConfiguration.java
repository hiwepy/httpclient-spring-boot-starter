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

@Configuration
@ConditionalOnClass({ MetricRegistry.class, HttpRequestExecutor.class, InstrumentedHttpRequestExecutor.class })
@ConditionalOnProperty(prefix = HttpClientMetricProperties.PREFIX, value = "enabled", havingValue = "true")
@AutoConfigureAfter(HttpClientDependsOnAutoConfiguration.class)
@EnableConfigurationProperties(value = { HttpClientManagerProperties.class, HttpClientMetricProperties.class })
public class HttpClientMetricAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(MetricRegistry.class)
	public MetricRegistry metricsRegistry() {
		return new MetricRegistry();
	}

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
	
	@Bean
	public HttpRequestExecutor httpRequestExecutor(MetricRegistry metricsRegistry,
			HttpClientMetricNameStrategy metricNameStrategy, HttpClientMetricProperties properties) {
		return new InstrumentedHttpRequestExecutor(metricsRegistry, metricNameStrategy, properties.getName(),
				properties.getWaitForContinue());
	}
	
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
	

	static class InstrumentedHttpClientConnectionManagerBuilder extends HttpClientConnectionManagerBuilder {

		private final MetricRegistry metricsRegistry;
		private final String name;

		public static InstrumentedHttpClientConnectionManagerBuilder create(MetricRegistry metricsRegistry,
				String name) {
			return new InstrumentedHttpClientConnectionManagerBuilder(metricsRegistry, name);
		}

		protected InstrumentedHttpClientConnectionManagerBuilder(MetricRegistry metricsRegistry, String name) {
			super();
			this.metricsRegistry = metricsRegistry;
			this.name = name;
		}

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
