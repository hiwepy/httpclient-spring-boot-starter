package org.apache.http.spring.boot.client.handler.response;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.spring.boot.client.utils.IOUtils;
import org.apache.http.util.EntityUtils;


/**
 * 
 * @className	： PlainTextResponseHandler
 * @description	： http请求响应处理：返回字符串结果对象
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年12月3日 下午4:13:07
 * @version 	V1.0
 */
public class PlainTextResponseHandler extends AbstractResponseHandler<String> {

	public PlainTextResponseHandler(String charset) {
		super(HttpClientContext.create(), charset);
	}
	
	public PlainTextResponseHandler(HttpClientContext context, String charset) {
		super(context, charset);
	}

	@Override
	public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		
		// 从response中取出HttpEntity对象
		HttpEntity entity = response.getEntity();
		if (entity == null) {
			throw new ClientProtocolException("Response contains no content");
		}
		
		StatusLine statusLine = response.getStatusLine();
		int status = statusLine.getStatusCode();
		if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
			try {
				ContentType contentType = ContentType.getOrDefault(entity);
				String charset = contentType.getCharset() == null ? getCharset() : contentType.getCharset().name();
				// 响应内容
				return EntityUtils.toString(entity, charset);
			} finally {
				if (entity != null) {
					IOUtils.closeQuietly(entity.getContent());
				}
				// 销毁
				EntityUtils.consume(entity);
			}
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
	}

}
