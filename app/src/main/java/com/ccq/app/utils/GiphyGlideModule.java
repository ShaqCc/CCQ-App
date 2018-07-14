package com.ccq.app.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/7/11 23:20
 * 描述：
 * 版本：
 *
 **************************************************/

@GlideModule
public class GiphyGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setMemoryCache(new LruResourceCache(10 * 1024 * 1024));
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        super.registerComponents(context, glide, registry);
    }
}
