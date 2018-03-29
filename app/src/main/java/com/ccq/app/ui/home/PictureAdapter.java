package com.ccq.app.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ccq.app.R;
import com.ccq.app.entity.Car;
import com.ccq.app.utils.DensityUtils;
import com.previewlibrary.ThumbViewInfo;

import java.util.ArrayList;
import java.util.List;

/****************************************
 * 功能说明:  车辆图片适配器
 *
 * Author: Created by bayin on 2018/3/29.
 ****************************************/

public class PictureAdapter extends BaseAdapter {

    private List<Car.PicImgBean> mPicList;
    private int count;
    private ArrayList<ThumbViewInfo> mThumbList;

    public PictureAdapter(List<Car.PicImgBean> list, int count) {
        mPicList = list;
        this.count = count;
        mThumbList = new ArrayList<>();
        for (int i = 0; i < mPicList.size(); i++) {
            mThumbList.add(new ThumbViewInfo(mPicList.get(i).getSavename() + "!auto"));
        }
    }

    public ArrayList<ThumbViewInfo> getThumbList() {
        return mThumbList;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thumbpic, parent, false);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
        layoutParams.width = DensityUtils.dp2px(parent.getContext(), 90);
        layoutParams.height = DensityUtils.dp2px(parent.getContext(), 90);
        holder.imageView.setLayoutParams(layoutParams);

        Glide.with(parent.getContext())
                .load(getImageUrl(mPicList.get(position).getSavename()))
                .into(holder.imageView);
        return convertView;
    }

    private String getImageUrl(String url) {
        return url + "!150auto";
    }

    static class Holder {
        public ImageView imageView;
    }
}
