 package org.apache.http.spring.boot.client.handler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.http.spring.boot.client.interceptor.HttpRequestHeaderInterceptor;

/**
 * 
 * *******************************************************************
 * @className	： DnsResolverHandler
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date		： Mar 8, 2016 11:53:09 AM
 * @version 	V1.0 
 * *******************************************************************
 */
public class DnsResolverHandler extends SystemDefaultDnsResolver {

	protected static Logger LOG = LoggerFactory.getLogger(HttpRequestHeaderInterceptor.class);
	
	protected Map<String,InetAddress[]> addressMap = new HashMap<String,InetAddress[]>();
	protected Properties properties;
	
	public DnsResolverHandler() {
		this(new Properties());
	}
	
	public DnsResolverHandler(Properties properties){
		this.properties = properties;
	}
	
	@Override
    public InetAddress[] resolve(final String host) throws UnknownHostException {
		if(!addressMap.isEmpty() && addressMap.containsKey(host)){
			return addressMap.get(host);
		} else if(!properties.isEmpty() && properties.containsKey(host)){
			String ip = properties.getProperty(host);
			if(ip != null){
				addressMap.put(host, InetAddress.getAllByName(ip));
			}
			return addressMap.get(host);
		} else if (host.equalsIgnoreCase("localhost")) {
            return new InetAddress[] { InetAddress.getByAddress(new byte[] {127, 0, 0, 1}) };
        } else {
            return super.resolve(host);
        }
    }
	
}

 
