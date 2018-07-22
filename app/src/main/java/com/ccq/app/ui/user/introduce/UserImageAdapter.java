package com.ccq.app.ui.user.introduce;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ccq.app.R;

import java.util.Collections;
import java.util.Comparator;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/7/22 14:34
 * 描述：
 * 版本：
 *
 **************************************************/

public class UserImageAdapter extends RecyclerView.Adapter<UserImageAdapter.ImageHolder> {

    private UserImageBean data;
    private Activity activity;

    public UserImageAdapter(UserImageBean dataBean, Activity activity) {
        this.data = dataBean;
        this.activity = activity;
        Collections.sort(data.getData());
    }

    public void refresh(UserImageBean dataBean) {
        this.data = dataBean;
        Collections.sort(data.getData());
        notifyDataSetChanged();
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.item_user_image, parent, false);
        return new ImageHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        final int adapterPosition = holder.getAdapterPosition();
        UserImageBean.DataBean dataBean = data.getData().get(adapterPosition);
        Glide.with(activity).load(dataBean.getResources().getOsspath() + "!150auto").into(holder.image);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(adapterPosition);
                }
            }
        });
    }

    private ItemClickListener listener;

    public void setListener(ItemClickListener l) {
        this.listener = l;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public int getItemCount() {
        if (data != null && data.getData() != null && !data.getData().isEmpty()) {
            return data.getData().size();
        }
        return 0;
    }

    public class ImageHolder extends RecyclerView.ViewHolder {

        private final ImageView image;
        private final View rootView;

        public ImageHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.item_root);
            image = itemView.findViewById(R.id.item_image);
        }
    }
}
