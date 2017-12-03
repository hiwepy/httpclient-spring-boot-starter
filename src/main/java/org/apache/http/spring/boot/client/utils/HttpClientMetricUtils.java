/*
 * Copyright (c) 2010-2020, wandalong (hnxyhcwdl1003@163.com).
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
package org.apache.http.spring.boot.client.utils;

import java.util.concurrent.TimeUnit;

import org.apache.http.config.Registry;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpRequestExecutor;
import org.springframework.util.StringUtils;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.codahale.metrics.httpclient.HttpClientMetricNameStrategies;
import com.codahale.metrics.httpclient.InstrumentedHttpClientConnectionManager;
import com.codahale.metrics.httpclient.InstrumentedHttpRequestExecutor;

public class HttpClientMetricUtils {

	protected static HttpRequestExecutor DEFAULT_EXECUTOR = new HttpRequestExecutor();
	protected static MetricRegistry metricRegistry = null;
	
	public static MetricRegistry getMetricRegistry(final String registryName){
		if(metricRegistry == null){
			try {
				if (!StringUtils.isEmpty(registryName)) {
					metricRegistry = SharedMetricRegistries.getOrCreate(registryName);
				} else {
					metricRegistry = new MetricRegistry();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return metricRegistry;
	}
	
	public static HttpRequestExecutor getHttpRequestExecutor(final String registryName) {
		if (!StringUtils.isEmpty(registryName)) {
			return DEFAULT_EXECUTOR;
		} else {
			return new InstrumentedHttpRequestExecutor(getMetricRegistry(registryName), HttpClientMetricNameStrategies.METHOD_ONLY);
		}
	}
	
	public static PoolingHttpClientConnectionManager getHttpClientConnectionManager(
			final String registryName,
            final Registry<ConnectionSocketFactory> socketFactoryRegistry,
            final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory,
            final DnsResolver dnsResolver) {
		if (!StringUtils.isEmpty(registryName)) {
			return new PoolingHttpClientConnectionManager(socketFactoryRegistry, connFactory, dnsResolver);
		} else {
			return new InstrumentedHttpClientConnectionManager(
					getMetricRegistry(registryName), socketFactoryRegistry, connFactory, null, dnsResolver, -1, TimeUnit.MILLISECONDS,
					null);
		}
	}
	
	

}
