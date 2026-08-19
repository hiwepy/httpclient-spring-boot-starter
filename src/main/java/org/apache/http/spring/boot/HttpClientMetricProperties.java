package org.apache.http.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for HttpClient Dropwizard Metrics instrumentation, bound to the
 * {@code httpclient.metrics.*} prefix.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = HttpClientMetricProperties.PREFIX)
public class HttpClientMetricProperties {

	public final static String PREFIX = "httpclient.metrics";

	/** Whether to enable HttpClient Metric instrumentation. */
	private boolean enabled = false;
	private String name;
	private int waitForContinue;
	/**
	 * <p>Is enabled.</p>
	 * @return the boolean
	 */

	public boolean isEnabled() {
		return enabled;
	}
	/** Sets the enabled. */

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	/** Gets the name. */

	public String getName() {
		return name;
	}
	/** Sets the name. */

	public void setName(String name) {
		this.name = name;
	}
	/** Gets the wait for continue. */

	public int getWaitForContinue() {
		return waitForContinue;
	}
	/** Sets the wait for continue. */

	public void setWaitForContinue(int waitForContinue) {
		this.waitForContinue = waitForContinue;
	}

}
