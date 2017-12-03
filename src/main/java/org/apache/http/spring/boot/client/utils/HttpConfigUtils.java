package org.apache.http.spring.boot.client.utils;

import java.util.Properties;

import org.apache.http.spring.boot.client.HttpClientConfig;
import org.apache.http.spring.boot.client.HttpClientParams;

public class HttpConfigUtils extends ConfigUtils {

	public static String HTTP_CONNECTION_LOCATION = "httpclient.properties";
	public static String HTTP_CONNECTION_HOST_LOCATION = "httpclient-host.properties";
	public static String HTTP_CONNECTION_HEADER_LOCATION = "httpclient-header.properties";
	public static HttpClientConfig clientConfig = null;
	
	public static HttpClientConfig getConfig() {

		if(clientConfig == null){
			
			clientConfig = new HttpClientConfig();
			
			try {

				Properties properties = ConfigUtils.getProperties(HttpConfigUtils.class, HTTP_CONNECTION_LOCATION);

				clientConfig.setUserManager(getBooleanProperty(properties, HttpClientParams.HTTP_CONNECTION_MANAGER));
				clientConfig.setKeepAlive(getLongProperty(properties, HttpClientParams.HTTP_CONNECTION_KEEPALIVE));
				clientConfig.setMaxPoolSize(getIntProperty(properties, HttpClientParams.HTTP_CONNECTION_MAX_POOLSIZE));
				clientConfig.setRetryTime(getIntProperty(properties, HttpClientParams.HTTP_CONNECTION_RETRY_TIME));
				clientConfig.setMaxLineLength(getIntProperty(properties, HttpClientParams.HTTP_CONNECTION_MAX_LINE_LENGTH));
				clientConfig.setMaxHeaderCount(getIntProperty(properties, HttpClientParams.HTTP_CONNECTION_MAX_HEADER_COUNT));
				clientConfig.setCharset(getStringProperty(properties, HttpClientParams.HTTP_CONNECTION_CONFIG_CHARSET));
				clientConfig.setDnsLookupsDisabled(getBooleanProperty(properties, HttpClientParams.HTTP_CONNECTION_DNS_LOOKUPS_DISABLED));
				clientConfig.setMetricsDisabled(getBooleanProperty(properties, HttpClientParams.HTTP_CONNECTION_METRICS_DISABLED));
				clientConfig.setMetricsRegistryName(getStringProperty(properties, HttpClientParams.HTTP_CONNECTION_METRICS_REGISTRYNAME));
				clientConfig.setTcpNoDelay(getBooleanProperty(properties, HttpClientParams.HTTP_SOCKET_TCPNODELAY));
				clientConfig.setSoTimeout(getIntProperty(properties, HttpClientParams.HTTP_SOCKET_SO_TIMEOUT));
				clientConfig.setConnectTimeout(getIntProperty(properties, HttpClientParams.HTTP_REQUEST_CONNECT_TIMEOUT));
				clientConfig.setSocketTimeout(getIntProperty(properties, HttpClientParams.HTTP_REQUEST_SOCKET_TIMEOUT));
				clientConfig.setHeartbeatInitialDelay(getIntProperty(properties, HttpClientParams.HTTP_HEARTBEAT_INITIALDELAY));
				clientConfig.setHeartbeatPeriod(getIntProperty(properties, HttpClientParams.HTTP_HEARTBEAT_PERIOD));
				clientConfig.setSslProtocol(getStringProperty(properties, HttpClientParams.HTTP_SSL_PROTOCOL));
				
			} catch (Exception ex) {
				LOG.warn("Could not load properties from classes/{0} : {1}", HTTP_CONNECTION_LOCATION, ex.getMessage());
			}

			try {

				Properties hostProperties = ConfigUtils.getProperties(HttpConfigUtils.class, HTTP_CONNECTION_HOST_LOCATION);
				clientConfig.setHostProperties(hostProperties);
			} catch (Exception ex) {
				LOG.warn("Could not load properties from classes/{0} : {1}", HTTP_CONNECTION_HOST_LOCATION, ex.getMessage());
			}

			try {

				Properties headerProperties = ConfigUtils.getProperties(HttpConfigUtils.class, HTTP_CONNECTION_HEADER_LOCATION);
				clientConfig.setHeaderProperties(headerProperties);
			} catch (Exception ex) {
				LOG.warn("Could not load properties from classes/{0} : {1}", HTTP_CONNECTION_HEADER_LOCATION, ex.getMessage());
			}
		}

		return clientConfig;
	}

	public static boolean getBooleanProperty(Properties properties, HttpClientParams key) { 
		String property = properties.getProperty(key.getName(), key.getDefault()); 
		return Boolean.getBoolean(property);
	}
	
	public static int getIntProperty(Properties properties, HttpClientParams key) { 
		String property = properties.getProperty(key.getName(), key.getDefault()); 
		return Integer.parseInt(property);
	}
	
	public static long getLongProperty(Properties properties, HttpClientParams key) { 
		String property = properties.getProperty(key.getName(), key.getDefault()); 
		return Long.parseLong(property);
	}
 
	public static String getStringProperty(Properties properties, HttpClientParams key) { 
		return StringUtils.getSafeStr(properties.getProperty(key.getName(), key.getDefault()));
	}
	
	protected static String setParams(String message, Object... params) {
		if (null != params && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				message = message.replaceFirst("\\{" + i + "\\}", String.valueOf(params[i]));
			}
		}
		return message;
	}

}
