package org.apache.http.spring.boot.client.interceptor;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = HttpRequestHeaderProperties.PREFIX)
public class HttpRequestHeaderProperties {

	public static final String PREFIX = "httpclient.header";

	private Map<String, String> headers = new LinkedHashMap<String, String>();

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
}
