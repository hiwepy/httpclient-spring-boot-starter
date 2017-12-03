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
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

/**
 *  http请求响应处理：返回JSONObject对象
 */
public class JSONResponseHandler extends AbstractResponseHandler<JSONObject> {

	public JSONResponseHandler( String charset) {
		super(null, charset);
	}
	
	public JSONResponseHandler(HttpClientContext context, String charset) {
		super(context, charset);
	}

	@Override
	public JSONObject handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		
		// 从response中取出HttpEntity对象
		HttpEntity entity = response.getEntity();
		if (entity == null) {
			throw new ClientProtocolException("Response contains no content");
		}
		
		StatusLine statusLine = response.getStatusLine();
		int status = statusLine.getStatusCode();
		if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
			//获取响应类型
			ContentType contentType = ContentType.getOrDefault(entity);
			if (! contentType.toString().equalsIgnoreCase(ContentType.APPLICATION_JSON.toString())) {
                throw new ClientProtocolException("Unexpected content type:" + contentType);
            }
			
			String charset = contentType.getCharset() == null ? getCharset() : contentType.getCharset().name();
			return JSONObject.parseObject(EntityUtils.toString(entity,charset));
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
	}
 
}
