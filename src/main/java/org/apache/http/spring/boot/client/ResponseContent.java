package org.apache.http.spring.boot.client;

import java.io.InputStream;
import java.util.Map;

/**
 * Encapsulates the data returned by an HttpClient request, including status code, content bytes/text,
 * content type, headers and the underlying input stream.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class ResponseContent {

	private String encoding;

	private byte[] contentBytes;

	private int statusCode;

	private String contentText;

	private String contentType;

	private String contentTypeString;
	
	private InputStream content;
	
	private Map<String,String> allHeaders;
	/** Gets the encoding. */

	public String getEncoding() {
		return encoding;
	}
	/** Sets the encoding. */

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	/** Gets the content bytes. */

	public byte[] getContentBytes() {
		return contentBytes;
	}
	/** Sets the content bytes. */

	public void setContentBytes(byte[] contentBytes) {
		this.contentBytes = contentBytes;
	}
	/** Gets the status code. */

	public int getStatusCode() {
		return statusCode;
	}
	/** Sets the status code. */

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	/** Gets the content text. */

	public String getContentText() {
		return contentText;
	}
	/** Sets the content text. */

	public void setContentText(String contentText) {
		this.contentText = contentText;
	}
	/** Gets the content type. */

	public String getContentType() {
		return contentType;
	}
	/** Sets the content type. */

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	/** Gets the content type string. */

	public String getContentTypeString() {
		return contentTypeString;
	}
	/** Sets the content type string. */

	public void setContentTypeString(String contentTypeString) {
		this.contentTypeString = contentTypeString;
	}
	/** Gets the content. */

	public InputStream getContent() {
	
		return content;
	}
	/** Sets the content. */

	public void setContent(InputStream content) {
	
		this.content = content;
	}
	/** Gets the all headers. */

	public Map<String, String> getAllHeaders() {
		return allHeaders;
	}
	/** Sets the all headers. */

	public void setAllHeaders(Map<String, String> allHeaders) {
		this.allHeaders = allHeaders;
	}

	
}
