package com.ccq.app.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ccq.app.R;
import com.youth.banner.loader.ImageLoader;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/3/28 22:27
 * 描述：
 * 版本：
 *
 **************************************************/

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

        //Glide 加载图片简单用法
        Glide.with(context).load(path).placeholder(R.mipmap.ic_default_thumb).into(imageView);

    }
}