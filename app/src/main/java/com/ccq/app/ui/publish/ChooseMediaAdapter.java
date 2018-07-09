package com.ccq.app.ui.publish;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ccq.app.R;
import com.ccq.app.weidget.Toasty;

import java.util.ArrayList;
import java.util.List;

import jiguang.chat.utils.imagepicker.ImageGridActivity;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/6/11 23:59
 * 描述：
 * 版本：
 *
 **************************************************/

public class ChooseMediaAdapter extends BaseAdapter {

    private List<String> mediaList = new ArrayList<>();

    private final int TYPE_CHOOSE_VIDEO = 2;
    private final int TYPE_CHOOSE_PIC = 1;
    private final int TYPE_NORMAL = 0;

    private Activity mActivity;

    public ChooseMediaAdapter(List<String> mediaList, Activity activity) {
        this.mediaList = mediaList;
        this.mActivity = activity;
    }

    public void refresh(List<String> list) {
        this.mediaList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mediaList.size() + 1) {
            //最后一个，视频选择
            return TYPE_CHOOSE_VIDEO;
        } else if (position == mediaList.size()) {
            //图片选择
            return TYPE_CHOOSE_PIC;
        } else {
            //图片
            return TYPE_NORMAL;
        }
    }

    @Override
    public int getCount() {
        return mediaList.size() + 2;
    }

    @Override
    public String getItem(int i) {
        return mediaList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder")
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gridview_photo_item,
                viewGroup, false);
        ImageView ivPic = inflate.findViewById(R.id.gridviewimg);
        View btDelete = inflate.findViewById(R.id.delete_iv);
        TextView tvType = inflate.findViewById(R.id.item_photo_tv_type);
        View tvFirstPage = inflate.findViewById(R.id.pakage_tv);
        View tvLeftFeng = inflate.findViewById(R.id.left_tips_textview);
        if (getItemViewType(position) == TYPE_NORMAL) {
            final String mediaPath = mediaList.get(position);
            //图片
            btDelete.setVisibility(View.VISIBLE);
            tvFirstPage.setVisibility(View.VISIBLE);

            if (mediaPath.contains(".jpg") || mediaPath.contains(".jpeg") || mediaPath.contains(".png") || mediaPath.contains(".bmp")) {
                Glide.with(mActivity).load(mediaPath).into(ivPic);
            } else {
                MediaMetadataRetriever media = new MediaMetadataRetriever();
                media.setDataSource(mediaPath);
                Bitmap bitmap = media.getFrameAtTime();
                ivPic.setImageBitmap(bitmap);
            }
            if (position == 0) {
                tvLeftFeng.setVisibility(View.VISIBLE);
                tvFirstPage.setBackgroundColor(Color.parseColor("#15B427"));
            } else {
                tvLeftFeng.setVisibility(View.GONE);
                tvFirstPage.setBackgroundColor(Color.parseColor("#88000000"));
            }
            //删除图片
            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mediaList.size() > position) {
                        mediaList.remove(position);
                        notifyDataSetChanged();
                    }
                }
            });
            //切换封面
            tvFirstPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position > 0) {
                        String first = mediaList.get(0);
                        mediaList.set(0, mediaPath);
                        mediaList.set(position, first);
                        notifyDataSetChanged();
                    }
                }
            });
        } else if (getItemViewType(position) == TYPE_CHOOSE_PIC) {
            btDelete.setVisibility(View.GONE);
            tvFirstPage.setVisibility(View.GONE);
            tvType.setText("图片");
            //选择图片
            ivPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mActivity, ImageGridActivity.class);
                    mActivity.startActivityForResult(intent, PublishFragment.RESULT_LOAD_IMAGE);
                }
            });
        } else {
            btDelete.setVisibility(View.GONE);
            tvFirstPage.setVisibility(View.GONE);
            tvType.setText("视频");
            //选择视频
            ivPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //视频
                    Intent intentPic = new Intent(
                            Intent.ACTION_GET_CONTENT);
                    intentPic.addCategory(Intent.CATEGORY_OPENABLE);
                    intentPic.setType("video/*");
                    mActivity.startActivityForResult(intentPic,
                            PublishFragment.RESULT_LOAD_VIDEO);
                    Toasty.info(view.getContext(), "选择视频。。。").show();
                }
            });
        }
        return inflate;
    }
}
