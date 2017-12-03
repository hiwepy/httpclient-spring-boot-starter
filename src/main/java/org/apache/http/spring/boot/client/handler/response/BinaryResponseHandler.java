package org.apache.http.spring.boot.client.handler.response;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
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
 * @className	： BinaryResponseHandler
 * @description	： http请求响应处理：返回byte[]对象
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年12月3日 下午3:25:51
 * @version 	V1.0
 */
public class BinaryResponseHandler extends AbstractResponseHandler<byte[]> {

	public BinaryResponseHandler( String charset) {
		super(null, charset);
	}

	public BinaryResponseHandler(HttpClientContext context, String charset) {
		super(context, charset);
	}
	
	@Override
	public byte[] handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		
		// 从response中取出HttpEntity对象
		HttpEntity httpEntity = response.getEntity();
		if (httpEntity == null) {
			throw new ClientProtocolException("Response contains no content");
		}
		
		StatusLine statusLine = response.getStatusLine();
		int status = statusLine.getStatusCode();
		if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
			byte[] content = null;
			try {
				// 响应内容
				content = EntityUtils.toByteArray(httpEntity);
			} finally {
				if (httpEntity != null) {
					IOUtils.closeQuietly(httpEntity.getContent());
					// 销毁
					EntityUtils.consume(httpEntity);
				}
			}
			return content;
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
	}

}
