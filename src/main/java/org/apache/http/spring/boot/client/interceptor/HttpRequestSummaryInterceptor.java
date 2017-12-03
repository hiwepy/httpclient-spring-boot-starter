package org.apache.http.spring.boot.client.interceptor;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;

/**
 * 
 * @className	： HttpRequestSummaryInterceptor
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年12月3日 下午4:15:03
 * @version 	V1.0
 */
public class HttpRequestSummaryInterceptor implements HttpRequestInterceptor {
	
	public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
		// AtomicInteger是个线程安全的整型类
		//AtomicInteger count = (AtomicInteger) context.getAttribute("count");
		//request.addHeader("Count", Integer.toString(count.getAndIncrement()));
	}
	
}
