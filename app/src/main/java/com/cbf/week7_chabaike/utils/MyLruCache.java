package com.cbf.week7_chabaike.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Administrator on 2016/11/16 0016.
 */

public class MyLruCache extends LruCache<String,Bitmap> {

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    private static MyLruCache mMyLruCache = null;
    public MyLruCache(int maxSize) {
        super(maxSize);
    }
    public static synchronized MyLruCache obtMyLruCache(int maxSize){
        if(mMyLruCache==null){
            mMyLruCache = new MyLruCache(maxSize);
        }
        return mMyLruCache;
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes()*value.getHeight();
    }

}
