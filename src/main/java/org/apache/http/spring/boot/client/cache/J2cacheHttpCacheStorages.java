 package org.apache.http.spring.boot.client.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.client.cache.HttpCacheEntrySerializer;
import org.apache.http.client.cache.HttpCacheStorage;
import org.apache.http.client.cache.HttpCacheUpdateCallback;
import org.apache.http.client.cache.HttpCacheUpdateException;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.DefaultHttpCacheEntrySerializer;

import net.oschina.j2cache.CacheChannel;

/**
 * 
 * @className	： J2cacheHttpCacheStorages
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2018年1月17日 下午5:31:36
 * @version 	V1.0
 */
public class J2cacheHttpCacheStorages implements HttpCacheStorage{
	
	private final String region;
	private final CacheChannel channel;
    private final HttpCacheEntrySerializer serializer;
    private final int maxUpdateRetries;

    /**
     * Constructs a storage backend using the provided J2cache
     * with default configuration options.
     * @param cache where to store cached origin responses
     */
    public J2cacheHttpCacheStorages(final String region,final CacheChannel channel) {
        this(region, channel, CacheConfig.DEFAULT, new DefaultHttpCacheEntrySerializer());
    }

    /**
     * Constructs a storage backend using the provided J2cache
     * with the given configuration options.
     * @param cache where to store cached origin responses
     * @param config cache storage configuration options - note that
     *   the setting for max object size <b>will be ignored</b> and
     *   should be configured in the J2cache instead.
     */
    public J2cacheHttpCacheStorages(final String region,final CacheChannel channel, final CacheConfig config){
        this(region, channel, config, new DefaultHttpCacheEntrySerializer());
    }

    /**
     * Constructs a storage backend using the provided J2cache
     * with the given configuration options, but using an alternative
     * cache entry serialization strategy.
     * @param cache where to store cached origin responses
     * @param config cache storage configuration options - note that
     *   the setting for max object size <b>will be ignored</b> and
     *   should be configured in the J2cache instead.
     * @param serializer alternative serialization mechanism
     */
    public J2cacheHttpCacheStorages(final String region,final CacheChannel channel, final CacheConfig config, final HttpCacheEntrySerializer serializer){
    	this.region = region;
        this.channel = channel;
        this.maxUpdateRetries = config.getMaxUpdateRetries();
        this.serializer = serializer;
    }

    @Override
    public synchronized void putEntry(final String key, final HttpCacheEntry entry) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        serializer.writeTo(entry, bos);
        channel.set(region, key, bos.toByteArray());
    }

    @Override
    public synchronized HttpCacheEntry getEntry(final String key) throws IOException {
        final Object e = channel.get(region, key);
        if(e == null){
            return null;
        }
        final byte[] data = (byte[]) e;
        return serializer.readFrom(new ByteArrayInputStream(data));
    }

    @Override
    public synchronized void removeEntry(final String key) {
    	channel.evict(region, key);
    }

    @Override
    public synchronized void updateEntry(final String key, final HttpCacheUpdateCallback callback)
            throws IOException, HttpCacheUpdateException {
        int numRetries = 0;
        do{
            final Object oldElement = channel.get(region, key);

            HttpCacheEntry existingEntry = null;
            if(oldElement != null){
                final byte[] data = (byte[])oldElement;
                existingEntry = serializer.readFrom(new ByteArrayInputStream(data));
            }

            final HttpCacheEntry updatedEntry = callback.update(existingEntry);

            if (existingEntry == null) {
                putEntry(key, updatedEntry);
                return;
            } else {
                // Attempt to do a CAS replace, if we fail then retry
                // While this operation should work fine within this instance, multiple instances
                //  could trample each others' data
                final ByteArrayOutputStream bos = new ByteArrayOutputStream();
                serializer.writeTo(updatedEntry, bos);
                if ( channel.exists(region, key)) {
                	channel.set(region, key, bos.toByteArray());
                    return;
                }else{
                	channel.set(region, key, bos.toByteArray());
                    numRetries++;
                }
            }
        }while(numRetries <= maxUpdateRetries);
        throw new HttpCacheUpdateException("Failed to update");
    }
	
}

 
