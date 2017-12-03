package org.apache.http.spring.boot.client.handler.response;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.util.EntityUtils;

/**
 * 
 * @className	： StreamResponseHandler
 * @description	： http请求响应处理：返回字符串结果对象
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年12月3日 下午4:12:56
 * @version 	V1.0
 */
public class StreamResponseHandler extends AbstractResponseHandler<ByteArrayInputStream> {

	public StreamResponseHandler(String charset) {
		super(null, charset);
	}
	
	public StreamResponseHandler(HttpClientContext context, String charset) {
		super(context, charset);
	}

	@Override
	public ByteArrayInputStream handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		
		// 从response中取出HttpEntity对象
		HttpEntity entity = response.getEntity();
		if (entity == null) {
			throw new ClientProtocolException("Response contains no content");
		}
		StatusLine statusLine = response.getStatusLine();
		int status = statusLine.getStatusCode();
		if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
			try {
				// 响应内容
				return new ByteArrayInputStream(EntityUtils.toByteArray(entity));
			} finally {
				// 销毁
				EntityUtils.consume(entity);
			}
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
	}

}
