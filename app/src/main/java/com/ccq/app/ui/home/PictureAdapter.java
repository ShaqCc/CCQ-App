package com.ccq.app.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ccq.app.R;
import com.ccq.app.base.CcqApp;
import com.ccq.app.ui.ImageWatchActivity;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.DensityUtils;
import com.ccq.app.weidget.MyGridView;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.PreviewActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/****************************************
 * 功能说明:  车辆图片适配器
 *
 * Author: Created by bayin on 2018/3/29.
 ****************************************/

public class PictureAdapter extends BaseAdapter {
    public static int columnSpace = DensityUtils.dp2px(CcqApp.getAppContext(), 3);
    private ArrayList<String> mPicList;
    private int mParentWidth = 0;
    private int imageViewSize;
    private int columnNum = 3;
    private MyGridView mParent;
    private Drawable holderImage;
    private Context mContext;

    public PictureAdapter(ArrayList<String> list, MyGridView gridView, int width) {
        this.mParent = gridView;
        mContext = gridView.getContext();
        mParentWidth = width;
        Log.e("PictureAdapter", "gridview宽度:" + mParentWidth);
        mPicList = list;
        //初始化
        holderImage = CcqApp.getAppContext().getResources().getDrawable(R.drawable.icon_image_holder);
        initUI();
    }

    private void initUI() {
        if (mPicList != null) {
            ViewGroup.LayoutParams layoutParams = mParent.getLayoutParams();
            if (mPicList.size() == 1) {
                imageViewSize = DensityUtils.dp2px(CcqApp.getAppContext(), 150);
                layoutParams.width = imageViewSize;
                columnNum = 1;
            } else if (mPicList.size() == 2 || mPicList.size() == 4) {
                imageViewSize = DensityUtils.dp2px(CcqApp.getAppContext(), 90);
                columnNum = 2;
                layoutParams.width = 2 * imageViewSize + columnSpace;
            } else {
                imageViewSize = DensityUtils.dp2px(CcqApp.getAppContext(), 90);
                layoutParams.width = 3 * imageViewSize + 2 * columnSpace;
                columnNum = 3;
            }
            mParent.setLayoutParams(layoutParams);
            mParent.setColumnWidth(imageViewSize);
            mParent.setHorizontalSpacing(columnSpace);
            mParent.setVerticalSpacing(columnSpace);
            mParent.setNumColumns(columnNum);
        }
    }

    @Override
    public int getCount() {
        if (mPicList != null) {
            return mPicList.size() > 9 ? 9 : mPicList.size();
        }
        return 0;
    }

    @Override
    public String getItem(int position) {
        return mPicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thumbpic, parent, false);
            holder.tvNumber = convertView.findViewById(R.id.tv_addition_image);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.imageView.setBackground(holderImage);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
        layoutParams.width = imageViewSize;
        layoutParams.height = imageViewSize;
        holder.imageView.setLayoutParams(layoutParams);

        if (position == 8 && mPicList.size() > 9) {
            holder.tvNumber.setText(String.format("+%s", mPicList.size() - 9));
            holder.tvNumber.setVisibility(View.VISIBLE);
            holder.tvNumber.setLayoutParams(layoutParams);
        } else {
            holder.tvNumber.setVisibility(View.GONE);
        }

        Glide.with(parent.getContext())
                .load(mPicList.get(position))
                .into(holder.imageView);

        //点击事件
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //预览
                ImageWatchActivity.launch(mContext, mPicList, position);
            }
        });
        return convertView;
    }

    static class Holder {
        public ImageView imageView;
        public TextView tvNumber;
    }
}
