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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ccq.app.R;

import java.util.ArrayList;
import java.util.List;

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

    public static void launch(Context context, ArrayList<String> urlList, int position) {
        Intent intent = new Intent();
        intent.setClass(context, ImageWatchActivity.class);
        imageUrlList = urlList;
        currentPosition = position;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_view_image_layout);
        viewPager = findViewById(R.id.image_vp);
        ImageAdapter adapter = new ImageAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentPosition);
    }

    private static class ImageAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = (ImageView) LayoutInflater.from(container.getContext()).inflate(R.layout.item_imageview, container, false);
            Glide.with(container.getContext()).load(imageUrlList.get(position)).into(imageView);
            container.addView(imageView);
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
}
