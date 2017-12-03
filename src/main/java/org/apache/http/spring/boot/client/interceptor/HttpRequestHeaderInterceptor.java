 package org.apache.http.spring.boot.client.interceptor;

import java.io.IOException;
import java.util.Properties;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

/**
 * 
 * @className	： HttpRequestHeaderInterceptor
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年12月3日 下午4:14:54
 * @version 	V1.0
 */
public class HttpRequestHeaderInterceptor implements HttpRequestInterceptor {
	
	//初始化配置文件
	protected Properties cachedProperties = new Properties();
	
	public HttpRequestHeaderInterceptor(Properties properties){	
		this.cachedProperties.putAll(properties);
	}
	
	@Override
	public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
		//通过配置文件加载响应头信息
		if(cachedProperties != null && !cachedProperties.isEmpty()){
			for(Object key : cachedProperties.keySet()){
				if(key != null){
					request.addHeader(key.toString() , cachedProperties.getProperty(key.toString()));  
				}
			}
		}
	}

}

 
