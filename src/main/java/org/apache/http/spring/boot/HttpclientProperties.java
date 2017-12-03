package org.apache.http.spring.boot;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.spring.boot.client.HttpClientConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = HttpclientProperties.PREFIX)
public class HttpclientProperties extends HttpClientConfig {

	public static final String PREFIX = "httpclient";

	/**
	 * 详细参数参见：com.google.code.kaptcha.Constants
	 */
	private Map<String,String> parameters = new HashMap<String,String>();

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
}
