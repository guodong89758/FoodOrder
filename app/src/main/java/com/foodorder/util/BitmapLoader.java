package com.foodorder.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by guodong on 2016/7/7 14:55.
 */
public class BitmapLoader {

    private static volatile BitmapLoader ins = null;

    private BitmapLoader() {

    }

    public static BitmapLoader ins() {
        BitmapLoader mins = ins;
        if (mins == null) {
            synchronized (BitmapLoader.class) {
                mins = ins;
                if (mins == null) {
                    mins = new BitmapLoader();
                    ins = mins;
                }
            }
        }
        return mins;
    }

    public void loadImage(String url, int placeholder, ImageView imageView) {
        DisplayImageOptions options;
        if (placeholder > 0) {
            options = new DisplayImageOptions.Builder().showImageOnLoading(placeholder)
                    .showImageOnFail(placeholder).showImageForEmptyUri(placeholder).cacheInMemory(true)
                    .cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).resetViewBeforeLoading(false)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .displayer(new FadeInBitmapDisplayer(100))
                    .build();
        } else {
            options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
                    .resetViewBeforeLoading(false).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .displayer(new FadeInBitmapDisplayer(100))
                    .build();
        }
        ImageLoader.getInstance().displayImage(url, imageView, options);
    }

    public void loadBitmap(String url, int placeholder, SimpleImageLoadingListener listener) {
        DisplayImageOptions options;
        if (placeholder > 0) {
            options = new DisplayImageOptions.Builder().showImageOnLoading(placeholder)
                    .showImageOnFail(placeholder).showImageForEmptyUri(placeholder).cacheInMemory(true)
                    .cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).resetViewBeforeLoading(false)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .build();
        } else {
            options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
                    .resetViewBeforeLoading(false).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .build();
        }
        ImageLoader.getInstance().loadImage(url, options, listener);
    }

}
