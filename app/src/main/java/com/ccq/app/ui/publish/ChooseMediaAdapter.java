package com.ccq.app.ui.publish;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ccq.app.R;
import com.ccq.app.weidget.Toasty;
import com.dmcbig.mediapicker.PickerActivity;
import com.dmcbig.mediapicker.PickerConfig;

import java.util.ArrayList;
import java.util.Hashtable;
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
    private boolean islocal;

    public void setDeleteItemClickListener(DeleteItemClickListener deleteItemClickListener) {
        this.deleteItemClickListener = deleteItemClickListener;
    }

    private DeleteItemClickListener deleteItemClickListener;

    public ChooseMediaAdapter(List<String> mediaList, Activity activity,boolean islocal) {
        this.mediaList = mediaList;
        this.mActivity = activity;
        this.islocal = islocal;
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
                if(!mediaPath.startsWith("http:")){
                    Glide.with(mActivity).load(mediaPath).into(ivPic);
                }else{
                    Glide.with(mActivity).load(mediaPath + "!300auto").into(ivPic);
                }

            } else {
                if(!mediaPath.startsWith("http:")){
                    MediaMetadataRetriever media = new MediaMetadataRetriever();
                    media.setDataSource(mediaPath);
                    Bitmap bitmap = media.getFrameAtTime();
                    ivPic.setImageBitmap(bitmap);
                }else{
                    createVideoThumbnail(mediaPath ,MediaStore.Images.Thumbnails.MICRO_KIND);
                }

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
                    deleteItemClickListener.onListItemClickListener(position);
//                    if (mediaList.size() > position) {
//                        mediaList.remove(position);
//                        notifyDataSetChanged();
//                    }
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
                    choosePic(mActivity);
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
                    chooseVideo(mActivity);
                }
            });
        }
        return inflate;
    }

    void choosePic(Activity activity) {
        Intent intent = new Intent(activity, PickerActivity.class);
        intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE);//default image and video (Optional)
//        long maxSize = 1024 * 1024 * 2;//long long long long类型
//        intent.putExtra(PickerConfig.MAX_SELECT_SIZE, maxSize); //default 180MB (Optional)
        intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 18);  //default 40 (Optional)
//        intent.putExtra(PickerConfig.DEFAULT_SELECTED_LIST, defaultSelect); //(Optional)默认选中的照片
        activity.startActivityForResult(intent, 200);
    }

    void chooseVideo(Activity activity) {
        Intent intent = new Intent(activity, PickerActivity.class);
        intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_VIDEO);//default image and video (Optional)
//        long maxSize = 1024 * 1024 * 5;//long long long long类型
//        intent.putExtra(PickerConfig.MAX_SELECT_SIZE, maxSize); //default 180MB (Optional)
        intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 1);  //default 40 (Optional)
//        intent.putExtra(PickerConfig.DEFAULT_SELECTED_LIST, defaultSelect); //(Optional)默认选中的照片
        activity.startActivityForResult(intent, 400);
    }

    public static Bitmap createVideoThumbnail(String filePath,int kind){
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (filePath.startsWith("http://")
                    || filePath.startsWith("https://")
                    || filePath.startsWith("widevine://")) {
                retriever.setDataSource(filePath,new Hashtable<String,String>());
            }else {
                retriever.setDataSource(filePath);
            }
            bitmap =retriever.getFrameAtTime(-1);
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
            ex.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
                ex.printStackTrace();
            }
        }

        if (bitmap==null) {
            return null;
        }

        if (kind== MediaStore.Images.Thumbnails.MINI_KIND) {
            // Scale down the bitmap if it's too large.
            int width= bitmap.getWidth();
            int height= bitmap.getHeight();
            int max =Math.max(width, height);
            if(max >512) {
                float scale=512f / max;
                int w =Math.round(scale * width);
                int h =Math.round(scale * height);
                bitmap = Bitmap.createScaledBitmap(bitmap,w, h, true);
            }
        } else if (kind== MediaStore.Images.Thumbnails.MICRO_KIND) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap,
                    96,
                    96,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    public interface DeleteItemClickListener {
        void onListItemClickListener(int position);
    }
}
