package org.apache.http.spring.boot.client.handler.response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.spring.boot.client.utils.HttpClientUtils;
import org.apache.http.spring.boot.client.utils.IOUtils;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;


/**
 * 
 * @className	： FileResponseHandler
 * @description	： http请求响应处理：返回文件对象
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年12月3日 下午4:14:28
 * @version 	V1.0
 */
public class FileResponseHandler extends AbstractResponseHandler<File> {

	private File destFile;
	
	public FileResponseHandler(File destFile) {
		super(null, HttpClientUtils.UTF_8);
		Assert.notNull(destFile, "destFile is null ");
		this.destFile = destFile;
	}
	
	public FileResponseHandler(HttpClientContext context, File destFile) {
		super(context, HttpClientUtils.UTF_8);
		Assert.notNull(destFile, "destFile is null ");
		this.destFile = destFile;
	}

	@Override
	public File handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		
		// 从response中取出HttpEntity对象
		HttpEntity httpEntity = response.getEntity();
		if (httpEntity == null) {
			throw new ClientProtocolException("Response contains no content");
		}
		
		StatusLine statusLine = response.getStatusLine();
		int status = statusLine.getStatusCode();
		if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
			
			//ContentType contentType = ContentType.getOrDefault(httpEntity);
			//String charset = contentType.getCharset() == null ? getCharset() : contentType.getCharset().name();
			InputStream input = null;
			FileOutputStream output = null;
			try {
				// 先存为临时文件，等全部下完再改回原来的文件名
				File storeFile = new File(destFile.getParent() , destFile.getName()  + ".tmp"); 
				output = new FileOutputStream(storeFile);
				input = httpEntity.getContent();
				IOUtils.copy(input, output);
				storeFile.renameTo(destFile);
				return destFile;
			} finally {
				IOUtils.closeQuietly(input);
				IOUtils.closeQuietly(output);
				// 销毁
				EntityUtils.consume(httpEntity);
			}
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}
	}

}
