 package org.apache.http.spring.boot.client.handler.response;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


/**
 * 
 * @className	： XMLResponseHandler
 * @description	： http请求响应处理：返回org.w3c.dom.Document对象
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年12月3日 下午4:12:45
 * @version 	V1.0
 */
public class XMLResponseHandler extends AbstractResponseHandler<Document> {

	private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	public XMLResponseHandler(String charset){
		super(null, charset); 
	}
	
	public XMLResponseHandler(HttpClientContext context,String charset){
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
            if (!contentType.equals(ContentType.APPLICATION_XML)) {
                throw new ClientProtocolException("Unexpected content type:" +contentType);
            }
	        try {
	            DocumentBuilder docBuilder = factory.newDocumentBuilder();
	            String charset = contentType.getCharset() == null ? charsetStr : contentType.getCharset().name();
	            return docBuilder.parse(entity.getContent(), charset);
	        } catch (ParserConfigurationException ex) {
	            throw new IllegalStateException(ex);
	        } catch (SAXException ex) {
	            throw new ClientProtocolException("Malformed XML document", ex);
	        }
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
    }
}

 
