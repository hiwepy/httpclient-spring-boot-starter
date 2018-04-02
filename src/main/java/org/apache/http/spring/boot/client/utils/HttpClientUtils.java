package org.apache.http.spring.boot.client.utils;


import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.GzipCompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.spring.boot.client.HttpClientConfig;
import org.apache.http.spring.boot.client.ResponseContent;
import org.apache.http.spring.boot.client.connection.HttpClientConnectionManagerBuilder;
import org.apache.http.spring.boot.client.handler.ConnectionKeepAliveStrategyHandler;
import org.apache.http.spring.boot.client.handler.HttpRequestExceptionRetryHandler;
import org.apache.http.spring.boot.client.handler.response.AbstractResponseHandler;
import org.apache.http.spring.boot.client.handler.response.BinaryResponseHandler;
import org.apache.http.spring.boot.client.handler.response.NestedResponseHandler;
import org.apache.http.spring.boot.client.handler.response.PlainTextResponseHandler;
import org.apache.http.spring.boot.client.handler.response.StreamResponseHandler;
import org.apache.http.spring.boot.client.interceptor.HttpRequestGzipInterceptor;
import org.apache.http.spring.boot.client.interceptor.HttpRequestHeaderInterceptor;
import org.apache.http.spring.boot.client.interceptor.HttpRequestSummaryInterceptor;
import org.apache.http.spring.boot.client.interceptor.HttpResponseGzipInterceptor;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 
 * @className	： HttpClientUtils
 * @description	： 使用 Apache HttpClient 组件进行get,post方式的请求
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年12月3日 下午4:17:56
 * @version 	V1.0
 */
public abstract class HttpClientUtils extends HttpRequestUtils {

	protected static Logger LOG = LoggerFactory.getLogger(HttpClientUtils.class);
	
	/**
     * 处理响应结果为字符串对象结果工具类
     */
    public static final AbstractResponseHandler<String> DEFAULT_TEXT_HANDLER= new PlainTextResponseHandler(UTF_8);
    /**
     * 处理响应结果为byte[]对象结果工具类
     */
    public static final AbstractResponseHandler<byte[]> DEFAULT_BINARY_HANDLER = new BinaryResponseHandler(UTF_8);
    /**
     * 处理响应结果为ByteArrayInputStream对象结果工具类
     */
    public static final AbstractResponseHandler<ByteArrayInputStream> DEFAULT_STESAM_HANDLER = new StreamResponseHandler(UTF_8);
    /**
     * 处理响应结果为ResponseContent对象结果工具类
     */
    public static final AbstractResponseHandler<ResponseContent> DEFAULT_CONTENT_HANDLER = new NestedResponseHandler(UTF_8);
	
    
    
	 /**
	  * 
	  * @description	： 使用Apache HttpClient 组件使用Get方式的访问URL
	  * @author 		： vindell
	  * @date 			：Jun 30, 2015 6:20:19 PM
	  * @param baseURL
	  * @return
	  * @throws IOException
	  */
	public static <T> T httpRequestWithGet(String baseURL,AbstractResponseHandler<T> handler) throws IOException {
      return httpRequestWithGet(baseURL, null , handler);
	}
	

	/**
	 * 
	 * @description ：使用Apache HttpClient 组件使用get方式的访问URL
	 * @author ： vindell
	 * @date ：2015-6-24 上午09:13:23
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 * @throws IOException
	 */
	public static <T> T httpRequestWithGet(String baseURL, Map<String, Object> paramsMap,AbstractResponseHandler<T> handler) throws  IOException {
		return httpRequestWithGet(baseURL, paramsMap, config.getCharset() , handler);
	}
	
	/**
	 * 
	 * @description ：使用apache HttpClient 组件进行get方式的请求
	 * @author ： vindell
	 * @date ：2015-6-24 上午09:13:23
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	public static <T> T httpRequestWithGet(String baseURL,Map<String, Object> paramsMap, String charset,AbstractResponseHandler<T> handler) throws  IOException {
		return httpRequestWithGet(baseURL, paramsMap, charset, null , handler);
	}
	
	/**
	 * 
	 * @description ：使用apache HttpClient 组件进行get方式的请求
	 * @author ： vindell
	 * @date ：2015-6-24 上午09:13:23
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	public static <T> T httpRequestWithGet(String baseURL,Map<String, Object> paramsMap, String charset,Map<String, String> headers,AbstractResponseHandler<T> handler) throws  IOException {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClientUtils.getCloseableHttpClient(httpConnectionManager);
        //Httpget对象
        HttpGet httpRequest = null;
		try {
			//创建httpget
	        httpRequest = getHttpGet(baseURL, paramsMap, charset , headers);
	        //判断回话上下文对象是否为空
	        if(StringUtils.isEmpty(handler.getContext())){
	        	//创建回话上下文对象
	        	handler.setContext(HttpClientContext.create());
	        }
	        //执行请求
			return httpclient.execute(httpRequest, handler , handler.getContext());
		} finally {
			//没有使用连接池情况下，需要手动释放链接和释放资源
			if (!config.isUserManager() ){
				releaseQuietly(httpRequest);
				closeQuietly(httpclient);
			}
			/* // When HttpClient instance is no longer needed,
		      // shut down the connection manager to ensure
		      // immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();*/
		}
	}
	
	/**
	 * 
	 * @description ： 使用apache HttpClient 组件进行post方式的请求;且对参数使用UTF-8编码
	 * @author ： vindell
	 * @date ：2015-6-24 上午09:13:35
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 * @throws IOException
	 */
	public static <T> T httpRequestWithPost(String baseURL,Map<String, Object> paramsMap,AbstractResponseHandler<T> handler) throws IOException {
		//连接字符编码格式
		return httpRequestWithPost(baseURL, paramsMap, config.getCharset(), handler);
	}
	
	/**
	 * 
	 * @description ： 使用apache HttpClient 组件进行post方式的请求；Content-Type 为  application/x-www-form-urlencoded
	 * @author ： vindell
	 * @date ：2015-6-24 上午09:13:35
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 * @throws IOException
	 */
	public static <T> T httpRequestWithPost(String baseURL,Map<String, Object> paramsMap, String charset,AbstractResponseHandler<T> handler) throws IOException {
		return httpRequestWithPost(baseURL, paramsMap, charset, APPLICATION_FORM_URLENCODED + "; charset=" + charset, handler);
	}
	
	 /**
     * 
     * @description	： 使用apache HttpClient 组件进行post方式的请求;可上传文件对象
     * @author 		： vindell
     * @date 		：Jun 30, 2015 7:14:54 PM
     * @param baseURL
     * @param paramsMap
     * @param charset
     * @param bodyType
     * @param contentType
     * @return
     * @throws IOException
     */
    public static <T> T httpRequestWithPost(String baseURL, Map<String, Object> paramsMap,final String charset,String contentType,AbstractResponseHandler<T> handler)  throws IOException {
    	return httpRequestWithPost(baseURL, paramsMap, charset, contentType, null, handler);
    }
    
    /**
     * 
     * @description	： 使用apache HttpClient 组件进行post方式的请求;可上传文件对象
     * @author 		： vindell
     * @date 		：Jun 30, 2015 7:14:54 PM
     * @param baseURL
     * @param paramsMap
     * @param charset
     * @param bodyType
     * @param contentType
     * @return
     * @throws IOException
     */
    public static <T> T httpRequestWithPost(String baseURL, Map<String, Object> paramsMap,String charset,String contentType,Map<String, String> headers,AbstractResponseHandler<T> handler)  throws IOException {
    	//定义初始对象
        HttpPost httpRequest =null;
		// 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClientUtils.getCloseableHttpClient(httpConnectionManager);
		try {
			httpRequest = getHttpPost(baseURL,charset,headers);
			//判断请求头信息
	        if (contentType != null) {
	            httpRequest.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
	        }else{
	        	//由于下面使用的是new StringEntity(....),所以默认发出去的请求报文头中CONTENT_TYPE值为text/plain; charset=ISO-8859-1  
	            //这就有可能会导致服务端接收不到POST过去的参数,比如运行在Tomcat6.0.36中的Servlet,所以我们手工指定CONTENT_TYPE头消息  
	        	httpRequest.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_FORM_URLENCODED + ";charset=" + charset);
	        }
	        //有实体对象参数，表示可能有文件上传
        	if(HttpRequestUtils.isMultipart(paramsMap)){
        		//有文件上传
                httpRequest.setHeader(HttpHeaders.CONTENT_TYPE, MULTIPART_FORM_DATA + ";charset=" + charset);
        	}
	        //得到请求实体对象
	        HttpEntity httpEntity = HttpRequestUtils.getHttpEntity(baseURL, paramsMap, charset);
        	//根据响应头判断是否启用gzip压缩
            boolean isGzip = HttpRequestUtils.isGzip(httpRequest);
            //设置请求实体参数对象
            if(isGzip){
            	httpRequest.setEntity(new GzipCompressingEntity(httpEntity));
            }else{
            	httpRequest.setEntity(httpEntity);
            }
        	//判断回话上下文对象是否为空
	        if(StringUtils.isEmpty(handler.getContext())){
	        	//创建回话上下文对象
	        	handler.setContext(HttpClientContext.create());
	        }
	        //执行请求
			return httpclient.execute(httpRequest, handler , handler.getContext());
		} finally {
			//没有使用连接池情况下，需要手动释放链接和释放资源
			if (!config.isUserManager() ){
				releaseQuietly(httpRequest);
				closeQuietly(httpclient);
			}
		}
	}

    
    /**
	 * 
	 * @description ： 使用apache HttpClient 组件进行post方式的请求；Content-Type 为  application/json
	 * @author ： vindell
	 * @date ：2015-6-24 上午09:13:35
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 * @throws IOException
	 */
	public static <T> T httpRequestWithPost(String baseURL,String json,AbstractResponseHandler<T> handler) throws IOException {
		return httpRequestWithPost(baseURL, json, UTF_8 , null ,handler);
	}
	
    /**
	 * 
	 * @description ： 使用apache HttpClient 组件进行post方式的请求；Content-Type 为  application/json
	 * @author ： vindell
	 * @date ：2015-6-24 上午09:13:35
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 * @throws IOException
	 */
	public static <T> T httpRequestWithPost(String baseURL,String json,String charset,AbstractResponseHandler<T> handler) throws IOException {
		return httpRequestWithPost(baseURL, json, charset, null ,handler);
	}
	
    /**
	 * 
	 * @description ： 使用apache HttpClient 组件进行post方式的请求；Content-Type 为  application/json
	 * @author ： vindell
	 * @date ：2015-6-24 上午09:13:35
	 * @param baseURL
	 * @param paramsMap
	 * @return
	 * @throws IOException
	 */
	public static <T> T httpRequestWithPost(String baseURL,String json, String charset,Map<String, String> headers,AbstractResponseHandler<T> handler) throws IOException {
		//定义初始对象
        HttpPost httpRequest = null;
		// 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClientUtils.getCloseableHttpClient(httpConnectionManager);
		try {
			//如果服务器需要通过HTTPS连接，那只需要将下面URL中的http换成https
			httpRequest = HttpRequestUtils.getHttpRequest(new HttpPost(baseURL), headers);
			// 将JSON进行UTF-8编码,以便传输中文
	        String encoderJson = URLEncoder.encode(json != null ? json : "{}" ,charset);
	        //构建字符串参数对象
	        StringEntity entity = new StringEntity(encoderJson);
	        entity.setContentType(TEXT_JSON);
	        entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
	        //设置请求头信息
	        httpRequest.setHeader(HttpHeaders.CONTENT_ENCODING , charset);
            httpRequest.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON );
            //根据响应头判断是否启用gzip压缩
            boolean isGzip = HttpRequestUtils.isGzip(httpRequest);
	        //设置请求实体参数对象
            if(isGzip){
            	httpRequest.setEntity(new GzipCompressingEntity(entity));
            }else{
            	 httpRequest.setEntity(entity);
            }
            //判断回话上下文对象是否为空
	        if(StringUtils.isEmpty(handler.getContext())){
	        	//创建回话上下文对象
	        	handler.setContext(HttpClientContext.create());
	        }
	        //执行请求
			return httpclient.execute(httpRequest, handler , handler.getContext());
		} finally {
			//没有使用连接池情况下，需要手动释放链接和释放资源
			if (!config.isUserManager() ){
				releaseQuietly(httpRequest);
				closeQuietly(httpclient);
			}
		}
		
	}
	
	/**
	 * 
	 * @description: 无条件的释放链接
	 * <p>
	 * Example Code:
	 * <pre>
	 * HttpPost httpRequest = null;
	 * try {
	 * 	 httpRequest = new HttpPost(baseURL);
	 * } catch (Exception e) {
	 * 	// error handling
	 * } finally {
	 * 	 HttpClientUtils.releaseQuietly(httpRequest);
	 * }
	 * @author : vindell
	 * @date 上午12:15:37 2015-9-4 
	 * @param httpRequest：要释放链接的  HttpRequestBase子对象, 可能为空或者已经关闭.
	 * @return  void 
	 * @throws  
	 */
	public static void releaseQuietly(HttpRequestBase httpRequest) {
		//关闭连接,释放资源
        if (httpRequest != null){
        	try {
        		httpRequest.releaseConnection();
        		httpRequest = null;
        	} catch (Exception ex) {
			}
        }
	}
	
	/**
	 * 
	 * @description: 无条件关闭HttpResponse。
	 *<p>
	 * Example Code:
	 * 
	 * <pre>
	 * HttpResponse httpResponse = null;
	 * try {
	 * 	httpResponse = httpClient.execute(httpGet);
	 * } catch (Exception e) {
	 * 	// error handling
	 * } finally {
	 * 	HttpClientUtils.closeQuietly(httpResponse);
	 * }
	 * </pre>
	 * @author : vindell
	 * @date 上午12:18:50 2015-9-4 
	 * @param response：要释放资源的  HttpResponse对象, 可能为空或者已经关闭.
	 * @return  void 
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public static void closeQuietly(final HttpResponse response) {
		if (response != null) {
			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				try {
					EntityUtils.consume(entity);
				} catch (final IOException ex) {
				}
			}
		}
	}

	/**
	 * 
	 * @description:  无条件关闭HttpResponse。
	 * <p>
	 * Example Code:
	 * 
	 * <pre>
	 * HttpResponse httpResponse = null;
	 * try {
	 * 	httpResponse = httpClient.execute(httpGet);
	 * } catch (Exception e) {
	 * 	// error handling
	 * } finally {
	 * 	HttpClientUtils.closeQuietly(httpResponse);
	 * }
	 * </pre>
	 * @author : vindell
	 * @date 上午12:17:49 2015-9-4 
	 * @param response：要释放资源的  HttpResponse对象, 可能为空或者已经关闭.
	 * @return  void 
	 * @throws  
	 */
	public static void closeQuietly(CloseableHttpResponse response) {
		if (response != null) {
			try {
				try {
					EntityUtils.consume(response.getEntity());
				} finally {
					response.close();
					response = null;
				}
			} catch (final IOException ignore) {
			}
		}
	}

	/**
	 * 
	 * @description: 无条件关闭httpClient。关闭底层的连接管理器，释放资源。
	 * <p>
	 * Example Code:
	 * 
	 * <pre>
	 * HttpClient httpClient = HttpClients.createDefault();
	 * try {
	 * 	httpClient.execute(request);
	 * } catch (Exception e) {
	 * 	// error handling
	 * } finally {
	 * 	HttpClientUtils.closeQuietly(httpClient);
	 * }
	 * </pre>
	 * @author : vindell
	 * @date 上午12:16:16 2015-9-4 
	 * @param httpClient ：要关闭的  HttpClient对象, 可能为空或者已经关闭.
	 * @return  void 返回类型
	 * @throws  
	 */
	public static void closeQuietly(HttpClient httpClient) {
		if (httpClient != null) {
			if (httpClient instanceof Closeable) {
				try {
					((Closeable) httpClient).close();
					httpClient = null;
				} catch (final IOException ignore) {
				}
			}
		}
	}

}
