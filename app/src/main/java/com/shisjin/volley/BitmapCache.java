package com.shisjin.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class BitmapCache implements ImageLoader.ImageCache {
    private Context context;
    private LruCache<String, Bitmap> lruCache;

    public BitmapCache(Context context) {
        this.context = context;
        long maxMemory = Runtime.getRuntime().maxMemory();
        long maxSize = maxMemory / 8;
        lruCache = new LruCache<String, Bitmap>((int) maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        String bitmapUrl = getBitmapUrl(url);
        Bitmap bitmap = lruCache.get(bitmapUrl);
        if (bitmap == null) {
            bitmap = getBitmapFromSDCard(bitmapUrl);
            if (bitmap != null) {
                lruCache.put(bitmapUrl, bitmap);
            }
        }
        return bitmap;
    }

    private Bitmap getBitmapFromSDCard(String bitmapUrl) {
        return BitmapFactory.decodeFile(new File(context.getExternalCacheDir(), bitmapUrl).getAbsolutePath());
    }

    private String getBitmapUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.length());
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        String bitmapUrl = getBitmapUrl(url);
        if (lruCache.get(bitmapUrl) == null) {
            lruCache.put(bitmapUrl, bitmap);
        }
        if (getBitmapFromSDCard(bitmapUrl) == null) {
            saveBitmap2SDCard(bitmapUrl, bitmap);
        }
    }

    private void saveBitmap2SDCard(String bitmapUrl, Bitmap bitmap) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(context.getExternalCacheDir(), bitmapUrl));
            if (bitmapUrl.endsWith(".png") || bitmapUrl.endsWith(".PNG")) {
                //如果第一个参数为PNG,则第二个参数无效
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, fos);
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 5, fos);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
