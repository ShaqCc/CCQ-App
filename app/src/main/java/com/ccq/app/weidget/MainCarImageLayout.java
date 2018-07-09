package com.ccq.app.weidget;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ccq.app.R;
import com.ccq.app.ui.ImageWatchActivity;

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
    protected boolean displayOneImage(final RatioImageView imageView, String url, final int parentWidth) {

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
    protected void displayImage(RatioImageView imageView, String url) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.icon_image_holder)
                .error(R.drawable.icon_image_holder);
        Glide.with(mContext)
                .setDefaultRequestOptions(requestOptions)
                .load(url).into(imageView);
    }

    @Override
    protected void onClickImage(int i, String url, List<String> urlList) {
        ImageWatchActivity.launch(mContext, (ArrayList<String>) urlList, i);
    }
}
