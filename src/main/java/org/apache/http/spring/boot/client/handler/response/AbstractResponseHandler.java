package org.apache.http.spring.boot.client.handler.response;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.protocol.HttpClientContext;

/**
 * 
 * @className	： AbstractResponseHandler
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年12月3日 下午3:25:44
 * @version 	V1.0 
 * @param <T>
 */
public abstract class AbstractResponseHandler<T> implements ResponseHandler<T> {

	protected HttpClientContext context;
	protected String charsetStr;

	public AbstractResponseHandler(HttpClientContext context, String charset) {
		this.context = context;
		this.charsetStr = charset;
	}

	public HttpClientContext getContext() {

		return context;
	}

	public void setContext(HttpClientContext context) {

		this.context = context;
	}

	public String getCharset() {

		return charsetStr;
	}

	public void setCharset(String charset) {

		this.charsetStr = charset;
	}

}
