package com.partseazy.android.network.caching;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by Pumpkin Guy on 15/11/16.
 */

public class BitmapCacheManager extends LruCache<String, Bitmap>
        implements
        com.android.volley.toolbox.ImageLoader.ImageCache {


    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        return cacheSize;
    }

    public BitmapCacheManager() {
        this(getDefaultLruCacheSize());
    }

    public BitmapCacheManager(int maxSize) {
        super(maxSize);
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value == null ? 0 : value.getRowBytes() * value.getHeight() / 1024;
    }

}





