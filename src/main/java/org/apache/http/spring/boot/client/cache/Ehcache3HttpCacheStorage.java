/*
 * Copyright (c) 2010-2020, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
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
import org.ehcache.core.Ehcache;

/**
 * 
 * @className	： Ehcache3HttpCacheStorage
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2018年1月17日 下午5:31:57
 * @version 	V1.0
 */
public class Ehcache3HttpCacheStorage implements HttpCacheStorage {

    private final Ehcache<Object, Object> cache;
    private final HttpCacheEntrySerializer serializer;
    private final int maxUpdateRetries;

    /**
     * Constructs a storage backend using the provided Ehcache3
     * with default configuration options.
     * @param cache where to store cached origin responses
     */
    public Ehcache3HttpCacheStorage(final Ehcache<Object, Object> cache) {
        this(cache, CacheConfig.DEFAULT, new DefaultHttpCacheEntrySerializer());
    }

    /**
     * Constructs a storage backend using the provided Ehcache3
     * with the given configuration options.
     * @param cache where to store cached origin responses
     * @param config cache storage configuration options - note that
     *   the setting for max object size <b>will be ignored</b> and
     *   should be configured in the Ehcache instead.
     */
    public Ehcache3HttpCacheStorage(final Ehcache<Object, Object> cache, final CacheConfig config){
        this(cache, config, new DefaultHttpCacheEntrySerializer());
    }

    /**
     * Constructs a storage backend using the provided Ehcache3
     * with the given configuration options, but using an alternative
     * cache entry serialization strategy.
     * @param cache where to store cached origin responses
     * @param config cache storage configuration options - note that
     *   the setting for max object size <b>will be ignored</b> and
     *   should be configured in the Ehcache instead.
     * @param serializer alternative serialization mechanism
     */
    public Ehcache3HttpCacheStorage(final Ehcache<Object, Object> cache, final CacheConfig config, final HttpCacheEntrySerializer serializer){
        this.cache = cache;
        this.maxUpdateRetries = config.getMaxUpdateRetries();
        this.serializer = serializer;
    }

    @Override
    public synchronized void putEntry(final String key, final HttpCacheEntry entry) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        serializer.writeTo(entry, bos);
        cache.put(key, bos.toByteArray());
    }

    @Override
    public synchronized HttpCacheEntry getEntry(final String key) throws IOException {
        final Object e = cache.get(key);
        if(e == null){
            return null;
        }
        final byte[] data = (byte[]) e;
        return serializer.readFrom(new ByteArrayInputStream(data));
    }

    @Override
    public synchronized void removeEntry(final String key) {
        cache.remove(key);
    }

    @Override
    public synchronized void updateEntry(final String key, final HttpCacheUpdateCallback callback)
            throws IOException, HttpCacheUpdateException {
        int numRetries = 0;
        do{
            final Object oldElement = cache.get(key);

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
                if (cache.replace(key, bos.toByteArray()) == null) {
                    return;
                }else{
                    numRetries++;
                }
            }
        }while(numRetries <= maxUpdateRetries);
        throw new HttpCacheUpdateException("Failed to update");
    }
}
