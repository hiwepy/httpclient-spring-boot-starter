 package org.apache.http.spring.boot.client.handler;

import java.util.Properties;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.protocol.HttpContext;
import org.springframework.util.StringUtils;

/**
 * 
 * *******************************************************************
 * @className	： ConnectionKeepAliveStrategyHandler
 * @description	：
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date		： Mar 8, 2016 11:53:31 AM
 * @version 	V1.0 
 * *******************************************************************
 */
public class ConnectionKeepAliveStrategyHandler extends DefaultConnectionKeepAliveStrategy {

	/**保持连接池内的长连接时长，单位毫秒，默认30秒*/
	private long keepAlive =  30 * 1000;
	/**获取是否启用连接池的标记*/
	private boolean userManager = false;
	/**
	 * Host对应的DNS、keepAlive信息
	 * [host].dns
	 * [host].keepAlive
	 */
	private Properties hostProperties = new Properties();
	
	public ConnectionKeepAliveStrategyHandler(boolean userManager, long keepAlive, Properties hostProperties){
		this.userManager = userManager;
		this.keepAlive = keepAlive;
		this.hostProperties = hostProperties;
	}
	
	 @Override
     public long getKeepAliveDuration(HttpResponse response,HttpContext context) {
		 long defaultKeepAlive = super.getKeepAliveDuration(response, context);
		 if(hostProperties != null && !hostProperties.isEmpty()){
			 HttpHost target = (HttpHost) context.getAttribute(HttpClientContext.HTTP_TARGET_HOST);
	         //尝试获取指定域名的KeepAlive值;key为： 域名+"-keepAlive"
	         String hostKeepAlive = hostProperties.getProperty(target.getHostName() + ".keepAlive");
	         if(StringUtils.hasText(hostKeepAlive)){
	             defaultKeepAlive = Long.parseLong(hostKeepAlive);
	             if (defaultKeepAlive == -1 && userManager) {
	                 //如果服务器没有设置keep-alive这个参数，我们就把它设置成指定值
	                 defaultKeepAlive = keepAlive;
	             }
	         }
		 }
         return defaultKeepAlive;
     }
	 
}

 
