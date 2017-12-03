 package org.apache.http.spring.boot.client.interceptor;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

/**
 * 
 * @className	： HttpRequestGzipInterceptor
 * @description	： 增加gzip压缩请求
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年12月3日 下午4:14:45
 * @version 	V1.0
 */
public class HttpRequestGzipInterceptor implements HttpRequestInterceptor {
	
	@Override
	public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
		
		if (!request.containsHeader("Accept-Encoding")) {  
			//设置相关的压缩文件标识，在请求头的信息中
			request.addHeader("Accept-Encoding", "gzip");  
        }  
	}

}

 
