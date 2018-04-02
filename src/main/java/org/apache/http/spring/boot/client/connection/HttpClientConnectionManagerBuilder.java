package org.apache.http.spring.boot.client.connection;

import java.io.File;
import java.nio.charset.CodingErrorAction;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.Consts;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.spring.boot.client.HttpClientConfig;
import org.apache.http.spring.boot.client.handler.DnsResolverHandler;
import org.apache.http.spring.boot.client.utils.HttpClientMetricUtils;
import org.apache.http.spring.boot.client.utils.HttpMessageFactoryUtils;
import org.apache.http.spring.boot.client.utils.SSLContextUtils;
import org.apache.http.spring.boot.client.utils.TrustManagerUtils;
import org.apache.http.spring.boot.client.utils.TrustStrategyUtils;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
/**
 * 
 * *******************************************************************
 * @className	： HttpClientConnectionManagerBuilder
 * @description	： Http连接池管理工具
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">vindell</a>
 * @date		： Mar 8, 2016 11:54:14 AM
 * @version 	V1.0 
 * *******************************************************************
 */
public class HttpClientConnectionManagerBuilder {
	
	protected static Logger LOG = LoggerFactory.getLogger(HttpClientConnectionManagerBuilder.class);
	protected static HttpClientConnectionManagerBuilder connectionManagerBuilder = null;
	protected static IdleConnectionTimeoutThread idleThread = new IdleConnectionTimeoutThread();
    protected final HttpMessageWriterFactory<HttpRequest> REQUEST_WRITER_FACTORY = HttpMessageFactoryUtils.getRequestWriterFactory();
    protected final HttpMessageParserFactory<HttpResponse> RESPONSE_PARSER_FACTORY = HttpMessageFactoryUtils.getResponseParserFactory();
    protected final TrustManager TRUST_ACCEPT_ALL = TrustManagerUtils.getAcceptAllTrustManager();
    protected final TrustStrategy STRATEGY_ACCEPT_ALL = TrustStrategyUtils.getAcceptAllTrustStrategy();
    protected HttpClientConfig config = null;
    
    //私有构造器
    private HttpClientConnectionManagerBuilder(HttpClientConfig config){
    	try {
    		this.config = config;
	        /**
	         *  定时清除失效链接
			 * 	period：两次开始执行最小间隔时间
	         */
	        idleThread.setConnectionTimeout(config.getSoTimeout());
	        idleThread.setTimeoutInterval(config.getHeartbeatPeriod());
	        idleThread.start();
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    public static HttpClientConnectionManagerBuilder getInstance(HttpClientConfig config){
		if(connectionManagerBuilder == null){
			synchronized (connectionManagerBuilder) {
				if(connectionManagerBuilder == null){
					connectionManagerBuilder = new HttpClientConnectionManagerBuilder(config);
				}
			}
    	}
    	return connectionManagerBuilder;
    }
    
    /**
     * 
     * @description	： 创建一个不安全连接 的池管理对象;全局只有一个的对象
     * @author 		： <a href="https://github.com/vindell">vindell</a>
     * @date 		：2017年12月3日 下午4:44:29
     * @return
     */
    public PoolingHttpClientConnectionManager getUnsafeSSLConnectionManager(){
    	SSLContext sslContext = null;
		try {
			sslContext = SSLContextUtils.createSSLContext(SSLConnectionSocketFactory.TLS, null, TRUST_ACCEPT_ALL);
        } catch (Exception e) {
        	try {
        		SSLContextUtils.createSSLContext(null, STRATEGY_ACCEPT_ALL);
			} catch (Exception e1) {
				LOG.error(e.getMessage());
	            // SSL context for secure connections can be created either based on
				// system or application specific properties.
				sslContext = SSLContexts.createSystemDefault();
			}
        }
        return getNewConnectionManager(sslContext);
    }
    
    /**
     * 
     * @description	： 创建一个 安全连接 的池管理对象;全局只有一个的对象
     * @author 		： <a href="https://github.com/vindell">vindell</a>
     * @date 		：2017年12月3日 下午4:44:21
     * @param keystore
     * @param storePassword
     * @return
     */
    public PoolingHttpClientConnectionManager getSafeSSLConnectionManager(File keystore,String storePassword){
		//初始化证书
	    SSLContext sslContext = null;
		try {
			String protocol = StringUtils.hasText(config.getSslProtocol()) ? config.getSslProtocol() : SSLConnectionSocketFactory.TLS;
			sslContext = SSLContextUtils.createSSLContext(protocol, keystore, storePassword, STRATEGY_ACCEPT_ALL);
	    } catch (Exception e) {
            LOG.error(e.getMessage());
            // SSL context for secure connections can be created either based on
			// system or application specific properties.
			sslContext = SSLContexts.createSystemDefault();
		}
		return getNewConnectionManager(sslContext);
    }
	
    public PoolingHttpClientConnectionManager getSafeSSLConnectionManager(KeyStore keystore){
		//初始化证书
	    SSLContext sslContext = null;
		try {
			sslContext = SSLContextUtils.createSSLContext(keystore, STRATEGY_ACCEPT_ALL);
	    } catch (Exception e) {
            LOG.error(e.getMessage());
            // SSL context for secure connections can be created either based on
			// system or application specific properties.
			sslContext = SSLContexts.createSystemDefault();
		}
        return getNewConnectionManager(sslContext);
    }
    
    public PoolingHttpClientConnectionManager getHttpConnectionManager(){
	    return getNewConnectionManager(null);
    }
    
    /**
     * 
     * @description	： 根据SSLContext 创建一个Http连接池管理对象;每次调用会产生一个新的对象
     * @author 		： <a href="https://github.com/vindell">vindell</a>
     * @date 		：2017年12月3日 下午4:44:10
     * @param sslContext
     * @return
     */
    public PoolingHttpClientConnectionManager getNewConnectionManager(SSLContext sslContext){
		
    	//CachingHttpClientBuilder.create().build()
    	
	    // Create a registry of custom connection socket factories for supported
	    // protocol schemes.
	    RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create()
	    .register("http", PlainConnectionSocketFactory.INSTANCE);
	    //根据sslContext做出调整
	    if(sslContext != null){
	    	registryBuilder.register("https", new SSLConnectionSocketFactory(sslContext));
	    }
	    Registry<ConnectionSocketFactory> socketFactoryRegistry = registryBuilder.build();

	    // Use a custom connection factory to customize the process of
	    // initialization of outgoing HTTP connections. Beside standard connection
	    // configuration parameters HTTP connection factory can define message
	    // parser / writer routines to be employed by individual connections.
	    HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(REQUEST_WRITER_FACTORY,RESPONSE_PARSER_FACTORY);

	    /**------------------------------设置连接池参数---------------------------------------------------------------------*/
	    
	    // Use custom DNS resolver to override the system DNS resolution.
	    DnsResolver dnsResolver = null;
	    // Create a connection manager with custom configuration.
	    if(!config.isDnsLookupsDisabled()){
	    	dnsResolver = new DnsResolverHandler(config.getHostProperties());
	    }
	    PoolingHttpClientConnectionManager connectionManager = null;
	    if(config.isMetricsDisabled()){
	    	connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry, connFactory, dnsResolver);
	    } else {
	    	connectionManager = HttpClientMetricUtils.getHttpClientConnectionManager(config.getMetricsRegistryName(), socketFactoryRegistry, connFactory, dnsResolver);
	    }
        
	    /**PoolingHttpClientConnectionManager维护的连接数在每个路由基础和总数上都有限制。
         * 默认，每个路由基础上的连接不超过2个，总连接数不能超过20。
         * 在实际应用中，这个限制可能会太小了，尤其是当服务器也使用Http协议时。
         * 
         * 下面调整连接池的参数：
    	 */
        // Configure total max or per route limits for persistent connections
        // that can be kept in the pool or leased by the connection manager.
        connectionManager.setMaxTotal(config.getMaxPoolSize());
        // 是路由的默认最大连接（该值默认为2），限制数量实际使用DefaultMaxPerRoute并非MaxTotal。
        // 设置过小无法支持大并发(ConnectionPoolTimeoutException: Timeout waiting for connection from pool)，路由是对maxTotal的细分。
        connectionManager.setDefaultMaxPerRoute(connectionManager.getMaxTotal());//（目前只有一个路由，因此让他等于最大值）
		
        //connManager.setMaxPerRoute(new HttpRoute(new HttpHost("somehost", 80)), 20);
        
        /**-----------------------------设置请求默认参数----------------------------------------------------------------------*/
        
        MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(config.getMaxHeaderCount()).setMaxLineLength(config.getMaxLineLength()).build();
        // 创建默认连接参数
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
            .setMalformedInputAction(CodingErrorAction.IGNORE)
            .setUnmappableInputAction(CodingErrorAction.IGNORE)
            .setCharset( "UTF-8".equalsIgnoreCase(config.getCharset()) ? Consts.UTF_8 : ( "US-ASCII".equalsIgnoreCase(config.getCharset()) ? Consts.ASCII : Consts.ISO_8859_1))
            .setMessageConstraints(messageConstraints)
            .build();
        
        /**
         * 多线程同时访问httpclient，例如同时从一个站点上下载多个文件。对于同一个HttpConnection同一个时间只能有一个线程访问，
         * 为了保证多线程工作环境下不产生冲突，httpclient使用了一个多线程连接管理器的类：MultiThreadedHttpConnectionManager，
         * 要使用这个类很简单，只需要在构造HttpClient实例的时候传入即可，代码如下：
         **/
        // MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager(); 
        connectionManager.setDefaultConnectionConfig(connectionConfig);
        //connManager.setConnectionConfig(new HttpHost("somehost", 80), ConnectionConfig.DEFAULT);
        
        /**-----------------------------设置读取默认参数----------------------------------------------------------------------*/
	    
        // 创建默认Socket参数
        SocketConfig socketConfig = SocketConfig.custom()
        //nagle算法默认是打开的，会引起delay的问题；所以要手工关掉
        .setTcpNoDelay(config.isTcpNoDelay())
        //设置读数据超时时间(单位毫秒) 
        .setSoTimeout(config.getSoTimeout()).build();
        //设置默认Socket连接参数
        connectionManager.setDefaultSocketConfig(socketConfig);
       
        /**-------------------------------启用连接池空连接处理线程--------------------------------------------------------------------*/
       
        idleThread.addConnectionManager(connectionManager);
        
	    //返回创建的连接池
    	return connectionManager;
    }
    
}
