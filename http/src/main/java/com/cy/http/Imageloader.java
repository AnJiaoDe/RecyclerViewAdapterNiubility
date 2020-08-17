package com.cy.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.Html;
import android.util.LruCache;
import android.widget.ImageView;

import com.cy.http.utils.FileUtils;
import com.cy.http.utils.LogUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * ************************************************************
 * author：cy
 * version：
 * create：2019/04/11 16:32
 * desc：
 * ************************************************************
 */

public class Imageloader  {
    /**
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
     */
    private LruCache<String, Bitmap>   lruCache = new LruCache<String, Bitmap>((int) Runtime.getRuntime().maxMemory() / 8) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            return bitmap.getByteCount();
        }
    };
    private ConcurrentMap<String,ImageRequestGenerator> map_imageLoader=new ConcurrentHashMap<>();
    private Imageloader(){
    }
    private static class ImageloaderFactory{
        private static Imageloader instance=new Imageloader();
    }
    public static Imageloader getInstance(){
        return ImageloaderFactory.instance;
    }
    public ImageRequestGenerator with(Context context){
        return new ImageRequestGenerator(context);
    }
    /**
     * 将一张图片存储到LruCache中。
     *
     * @param key
     *            LruCache的键，这里传入图片的URL地址。,url相同将会覆盖
     * @param bitmap
     *            LruCache的键，这里传入从网络上下载的Bitmap对象。
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
         lruCache.put(key, bitmap);
    }
    public void recycleBitmapFromMemoryCache(String key) {
         lruCache.remove(key);
    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     *
     * @param key
     *            LruCache的键，这里传入图片的URL地址。
     * @return 对应传入键的Bitmap对象，或者null。
     */
    public Bitmap getBitmapFromMemoryCache(String key) {
        return lruCache.get(key);
    }
    public void clear(){
        lruCache.evictAll();
    }

    public void cancelLoad(String url){
        ImageRequestGenerator imageRequestGenerator=map_imageLoader.get(url);
        if(imageRequestGenerator!=null){
            imageRequestGenerator.cancel();
            map_imageLoader.remove(imageRequestGenerator);
        }
    }
    public void cancelAllLoad(){
        for(String url:map_imageLoader.keySet()){
            ImageRequestGenerator imageRequestGenerator=map_imageLoader.get(url);
            if(imageRequestGenerator!=null){
                imageRequestGenerator.cancel();
                map_imageLoader.remove(imageRequestGenerator);
            }
        }
    }
}
