package com.foodorder.base;

import android.app.Application;
import android.content.Context;

import com.karumi.dexter.Dexter;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * Created by guodong on 2016/6/28 18:44.
 */
public class FOApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Dexter.initialize(this);
        initBitmap();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    private void initBitmap() {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .diskCache(new UnlimitedDiskCache(StorageUtils.getOwnCacheDirectory(this, "foodorder/images")))
                // .memoryCacheSize(8 * 1024 * 1024)
                .memoryCacheSize(((int) Runtime.getRuntime().maxMemory()) / 8).diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()).defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .threadPoolSize(3).imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000))
                .denyCacheImageMultipleSizesInMemory().build();
        ImageLoader.getInstance().init(configuration);

    }
}
