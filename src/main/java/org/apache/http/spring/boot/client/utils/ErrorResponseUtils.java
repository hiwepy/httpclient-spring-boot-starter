 package org.apache.http.spring.boot.client.utils;

import java.util.Properties;

import org.apache.http.StatusLine;

/**
 * 
 * @className	： ErrorResponseUtils
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年12月3日 下午4:15:45
 * @version 	V1.0
 */
public class ErrorResponseUtils {

	/**
	 * 相应的状态码信息
	 */
	protected static Properties messages = HttpConfigUtils.getConfig().getMessages();
	
	public static String getStatusErrorJSON(StatusLine statusLine){
		int statusCode = statusLine.getStatusCode();
		StringBuilder builder = new StringBuilder();
		//{"errcode":40013,"errmsg":"invalid appid"}
		builder.append("{");
		builder.append("\"errcode\":").append(statusCode).append(",");
		builder.append("\"reason\":\"").append(statusLine.getReasonPhrase()).append("\",");
		builder.append("\"errmsg\":\"").append(messages.getProperty(statusCode + "", "")).append("\"");
		builder.append("}");
		return builder.toString();
	}
	
	
}

 
