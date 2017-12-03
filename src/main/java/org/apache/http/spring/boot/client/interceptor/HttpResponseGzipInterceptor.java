 package org.apache.http.spring.boot.client.interceptor;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.entity.DeflateDecompressingEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.protocol.HttpContext;

/**
 * 
 * @className	： HttpResponseGzipInterceptor
 * @description	： http响应监听，遇到压缩的响应头，使用响应的对象进行处理
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年12月3日 下午4:15:12
 * @version 	V1.0
 */
public class HttpResponseGzipInterceptor implements HttpResponseInterceptor {

	@Override
	public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
		HttpEntity entity = response.getEntity();  
        Header ceheader = entity.getContentEncoding();  
        if (ceheader != null) {  
            HeaderElement[] codecs = ceheader.getElements();  
            for (int i = 0; i < codecs.length; i++) {  
                if (codecs[i].getName().equalsIgnoreCase("gzip")) {  
                    response.setEntity(new GzipDecompressingEntity(response.getEntity()));   
                    return;  
                } else if (codecs[i].getName().equalsIgnoreCase("deflate")) {  
                    response.setEntity(new DeflateDecompressingEntity(response.getEntity()));   
                    return;  
                }
            }  
        }
	}

}

 
