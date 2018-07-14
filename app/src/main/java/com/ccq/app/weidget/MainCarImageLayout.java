package com.ccq.app.weidget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.ccq.app.R;
import com.ccq.app.ui.ImageWatchActivity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/7/1 12:27
 * 描述：
 * 版本：
 *
 **************************************************/

public class MainCarImageLayout extends NineGridLayout {

    protected static final int MAX_W_H_RATIO = 3;

    public MainCarImageLayout(Context context) {
        super(context);
    }

    public MainCarImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean displayOneImage(final ImageView imageView, String url, final int parentWidth) {

        Glide.with(mContext).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                int w = resource.getWidth();
                int h = resource.getHeight();

                int newW;
                int newH;
                if (h > w * MAX_W_H_RATIO) {//h:w = 5:3
                    newW = parentWidth / 2;
                    newH = newW * 5 / 3;
                } else if (h < w) {//h:w = 2:3
                    newW = parentWidth * 2 / 3;
                    newH = newW * 2 / 3;
                } else {//newH:h = newW :w
                    newW = parentWidth / 2;
                    newH = h * newW / w;
                }
                setOneImageLayoutParams(imageView, newW, newH);
            }
        });
        return false;
    }

    @Override
    protected void displayImage(ImageView imageView, String url) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.icon_image_holder)
                .error(R.drawable.icon_image_holder);
        Log.i("MainCarImageLayout", url);
        Glide.with(mContext)
                .load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.e("MainCarImageLayout", "报错：" + e.getMessage());
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Log.e("MainCarImageLayout", "ready");
                return true;
            }
        }).into(imageView).onLoadFailed(getContext().getResources().getDrawable(R.drawable.icon_image_holder));
    }

    @Override
    protected void onClickImage(int i, String url, List<String> urlList) {
        ImageWatchActivity.launch(mContext, (ArrayList<String>) urlList, i);
    }
}
