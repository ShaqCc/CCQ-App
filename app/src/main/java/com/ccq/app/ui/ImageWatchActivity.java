package com.ccq.app.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ccq.app.R;
import com.ccq.app.utils.statusbar.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;


/**************************************************
 *
 * 作者：巴银
 * 时间：2018/7/1 11:49
 * 描述：
 * 版本：
 *
 **************************************************/

public class ImageWatchActivity extends FragmentActivity {


    public static String KEY_IMAGE_LIST = "image_list";

    private static List<String> imageUrlList = new ArrayList();
    private ViewPager viewPager;
    private static int currentPosition;
    private RequestOptions requestOptions = new RequestOptions().fitCenter()
            .placeholder(R.drawable.icon_image_holder)
            .error(R.mipmap.default_image);
    private TextView tvPostion;

    public static void launch(Context context, ArrayList<String> urlList, int position) {
        Intent intent = new Intent();
        intent.setClass(context, ImageWatchActivity.class);
        imageUrlList = urlList;
        currentPosition = position;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_view_image_layout);
        StatusBarUtils.setStatusBarColor(this,0xff000000);
        viewPager = findViewById(R.id.image_vp);
        ImageAdapter adapter = new ImageAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentPosition);
        tvPostion = findViewById(R.id.tv_picture_position);
        tvPostion.setText(String.format("%s/%s", currentPosition + 1, imageUrlList.size()));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvPostion.setText(String.format("%s/%s", position + 1, imageUrlList.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class ImageAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView imageView = (PhotoView) LayoutInflater.from(container.getContext()).inflate(R.layout.item_imageview, container, false);
            Glide.with(container.getContext()).load(getAutoImageUrl(imageUrlList.get(position)))
                    .apply(requestOptions)
                    .into(imageView);
            container.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            return imageView;
        }

        @Override
        public int getCount() {
            if (imageUrlList != null && !imageUrlList.isEmpty())
                return imageUrlList.size();
            else return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private String getAutoImageUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            return url.replace("150auto", "auto");
        }
        return url;
    }
}
