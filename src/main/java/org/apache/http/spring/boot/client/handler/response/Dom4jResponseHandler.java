package org.apache.http.spring.boot.client.handler.response;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

/**
 * 
 * @className	： Dom4jResponseHandler
 * @description	： http请求响应处理：返回org.dom4j.Document对象
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年12月3日 下午4:14:13
 * @version 	V1.0
 */
public class Dom4jResponseHandler extends AbstractResponseHandler<Document> {

	// 读取输入流
	protected SAXReader reader = new SAXReader();

	public Dom4jResponseHandler( String charset) {
		super(null, charset);
	}
	
	public Dom4jResponseHandler(HttpClientContext context, String charset) {
		super(context, charset);
	}

	@Override
	public Document handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		
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
			if (! contentType.toString().equalsIgnoreCase(ContentType.APPLICATION_XML.toString())) {
                throw new ClientProtocolException("Unexpected content type:" + contentType);
            }
			
			//String charset = contentType.getCharset() == null ? getCharset() : contentType.getCharset().name();
			// 从request中取得输入流
			InputStream inputStream = null;
			Document document = null;
			try {
				inputStream = entity.getContent();
				document = reader.read(inputStream);
			} catch (DocumentException ex) {
				throw new ClientProtocolException("Malformed XML document",ex);
			} finally {
				// 释放资源
				IOUtils.closeQuietly(inputStream);
				inputStream = null;
			}
			return document;
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
	}
	
}
