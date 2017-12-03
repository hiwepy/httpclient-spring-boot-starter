package org.apache.http.spring.boot.client.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharsetUtils;


public abstract class HttpURIUtils {

public static URI buildURI(String baseURL, Map<String, Object> paramsMap,String charset)  {
		
		try {
			URL url = new URL(baseURL);
			if (paramsMap == null) {
				return new URI( url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), null);
			}
			
			//初始参数集合对象
			List<NameValuePair> nameValuePairs    = buildNameValuePairs(paramsMap);
			StringBuilder builder = new StringBuilder(StringUtils.isEmpty(url.getQuery()) ? "" : url.getQuery());
			builder.append(builder.indexOf("?") > 0 ? "&" : "?").append(URLEncodedUtils.format(nameValuePairs, charset)).toString();
			
			return new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), builder.toString(), null);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<NameValuePair> buildNameValuePairs(Map<String, Object> paramsMap) {
    	//初始参数集合对象
    	List<NameValuePair> nameValuePairs    = new LinkedList<NameValuePair>();
    	if(paramsMap != null && !paramsMap.isEmpty()){
    		//组织参数
            Iterator<String> iterator = paramsMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = paramsMap.get(key);
                if (value instanceof File) {
                	//什么都不做
                } else if (value instanceof byte[]) {
                	//什么都不做
                } else {
                    if (value != null && !"".equals(value)) {
                    	nameValuePairs.add(new BasicNameValuePair(key, String.valueOf(value) ));
                    } else {
                        nameValuePairs.add(new BasicNameValuePair(key, ""));
                    }
                }
            }
    	}
        return nameValuePairs;
    }
	
	public static List<NameValuePair> buildNameValuePairs(String params) {
    	//初始参数集合对象
    	List<NameValuePair> nameValuePairs    = new LinkedList<NameValuePair>();
    	if(StringUtils.isEmpty(params)){
    		return nameValuePairs;
    	}
    	//初始参数集合对象
        String[] paramArray = params.split("[&]");
        if (paramArray == null) {
        	return nameValuePairs;
        }
        for (String param : paramArray) {
            if (StringUtils.isEmpty(param)) {
                continue;
            }
            String[] keyValue = param.split("[=]", 2);
            if (keyValue == null || keyValue.length != 2) {
                continue;
            }
            nameValuePairs.add(new BasicNameValuePair(keyValue[0], keyValue[1]));
        }
        return nameValuePairs;
    }
	
	public static URIBuilder buildURIBuilder(String baseURL, Map<String, Object> paramsMap,String charset) {
		
		if (baseURL == null) {
			return null;
		}
		
		try {
			
			URL url = new URL(baseURL);
			URI uri = new URI( url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), null);
			URIBuilder uriBuilder = new URIBuilder(uri);
			//初始参数集合对象
			if(paramsMap != null && !paramsMap.isEmpty()){
	        	for (String key : paramsMap.keySet()) {
	                Object value = paramsMap.get(key);
	    			if (value instanceof File) {
	                	//什么都不做
	                } else if (value instanceof byte[]) {
	                	//什么都不做
	                } else {
	                    if (value != null && !"".equals(value)) {
	                    	uriBuilder.setParameter(key,String.valueOf(value));
	                    } else {
	                    	uriBuilder.setParameter(key,"");
	                    }
	                }
	    		}
	        }
			return uriBuilder.setCharset(charset == null ? Consts.UTF_8 : CharsetUtils.get(charset)  );
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
