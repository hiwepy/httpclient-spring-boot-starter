package org.apache.http.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = HttpClientMetricProperties.PREFIX)
public class HttpClientMetricProperties {

	public final static String PREFIX = "httpclient.metrics";

	/** Whether Enable HttpClient Metric. */
	private boolean enabled = false;
	private String name;
	private int waitForContinue;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWaitForContinue() {
		return waitForContinue;
	}

	public void setWaitForContinue(int waitForContinue) {
		this.waitForContinue = waitForContinue;
	}

}
