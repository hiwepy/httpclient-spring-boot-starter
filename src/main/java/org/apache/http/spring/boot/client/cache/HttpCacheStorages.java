 package org.apache.http.spring.boot.client.cache;

import java.io.IOException;

import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.client.cache.HttpCacheStorage;
import org.apache.http.client.cache.HttpCacheUpdateCallback;
import org.apache.http.client.cache.HttpCacheUpdateException;

/**
 * 
 * *******************************************************************
 * @className	： HttpCacheStorages
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date		： Mar 8, 2016 11:54:22 AM
 * @version 	V1.0 
 * *******************************************************************
 */
public class HttpCacheStorages implements HttpCacheStorage{

	@Override
	public HttpCacheEntry getEntry(String key) throws IOException {
		
		return null;
	}

	@Override
	public void putEntry(String key, HttpCacheEntry entry) throws IOException {
		
		
	}

	@Override
	public void removeEntry(String key) throws IOException {
		
		
	}

	@Override
	public void updateEntry(String key, HttpCacheUpdateCallback callback) throws IOException, HttpCacheUpdateException {
		
	}
	
}

 
