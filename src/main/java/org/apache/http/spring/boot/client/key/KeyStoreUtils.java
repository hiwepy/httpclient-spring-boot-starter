 package org.apache.http.spring.boot.client.key;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.spring.boot.client.utils.IOUtils;
import org.apache.http.util.EntityUtils;

/**
 * 
 * @className	： KeyStoreUtils
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年12月3日 下午4:15:32
 * @version 	V1.0
 */
public class KeyStoreUtils {

	public static KeyStore getLocalKeyStore(File keyStore,String password) throws KeyStoreException, IOException{
		return KeyStoreUtils.getLocalKeyStore(keyStore, password, KeyStore.getDefaultType());
	}
	
	public static KeyStore getLocalKeyStore(File keyStore,String password,String type) throws KeyStoreException, IOException{
		FileInputStream instream = null;
		KeyStore trustStore = null;
		try {
			trustStore = KeyStore.getInstance(type);
			instream = new FileInputStream(keyStore);
			// 加载keyStore
			trustStore.load(instream, password.toCharArray());
		} catch (CertificateException e) {
			e.printStackTrace();
		}catch (NoSuchAlgorithmException e) {
			
		} finally {
			IOUtils.closeQuietly(instream);
		}
		return trustStore;
	}
	
	public KeyStore getRemoteKeyStore(String httpPath,Map<String,String> paramsMap) {
		KeyStore keyStore = null;
		// 创建默认的httpClient实例.  
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httppost  
		HttpPost httppost = new HttpPost(httpPath);
		// 创建参数队列  
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		for (String key1 : paramsMap.keySet()) {
			formparams.add(new BasicNameValuePair(key1, paramsMap.get(key1)));
		}
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			System.out.println("executing request " + httppost.getURI());
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					System.out.println("---------------Response Object Stream -----------------------");
					ObjectInputStream keyIn = new ObjectInputStream(response.getEntity().getContent());
					//读取SecretKey对象
					keyStore = (KeyStore) keyIn.readObject();
					//关闭输入流
					keyIn.close();
					System.out.println("--------------------------------------");
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源  
			IOUtils.closeQuietly(httpclient);
		}
		return keyStore;
	}
	
	public String getRemoteSecretKey(String httpPath,Map<String,String> paramsMap) {
		// 创建默认的httpClient实例.  
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httppost  
		HttpPost httppost = new HttpPost(httpPath);
		// 创建参数队列  
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		for (String key1 : paramsMap.keySet()) {
			formparams.add(new BasicNameValuePair(key1, paramsMap.get(key1)));
		}
		UrlEncodedFormEntity uefEntity;
		String key = null;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			System.out.println("executing request " + httppost.getURI());
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					System.out.println("--------------------------------------");
					key = EntityUtils.toString(entity, "UTF-8");
					System.out.println("--------------------------------------");
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源  
			IOUtils.closeQuietly(httpclient);
		}
		return key;
	}
	
}

 
