package com.sumit.kaushik.HBTUFacemash.util;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by sumitkaushik on 1/10/16.
 */
public class LruBitmapCache extends LruCache<String,Bitmap> implements ImageLoader.ImageCache{

    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        return cacheSize;
    }
    public LruBitmapCache(int maxSize) {
        super(maxSize);
    }
    public LruBitmapCache() {
        this(getDefaultLruCacheSize());
    }
    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
    put(url,bitmap);
    }
}
