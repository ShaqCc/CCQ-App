package com.ccq.app.ui.user.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ccq.app.R;
import com.ccq.app.entity.Car;
import com.ccq.app.ui.ImageWatchActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by littlemax on 2018/5/2.
 */

public class MyPublishListAdapter extends RecyclerView.Adapter<MyPublishListAdapter.ViewHolder> {

    List<Car> mCarList;
    Context context;
    public onItemManageInterface itemManageInterface;
    private boolean isUserSelf;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_publish_info_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Car Car = mCarList.get(position);
        holder.itemImageCount.setText(String.valueOf(Car.getPic_img_count()));

        Glide.with(context)
                .load(getImageUrl(Car.getPicfmid_img().getSavename()))
                .into(holder.itemInfoImage);


        holder.itemBrandModelName.setText(Car.getName());
        holder.itemLocation.setText(Car.getCityName() + Car.getAddress());
        holder.itemTime.setText(Car.getAddtime_format() + " 展现：" + Car.getCount() + "次");

        if (Car.getPrice().equals("0")) {
            holder.itemPrice.setText("面议");
        } else {
            holder.itemPrice.setText("￥" + Car.getPrice() + "万元");
        }

        if (isUserSelf) {
            holder.itemManage.setVisibility(View.VISIBLE);
            holder.itemManage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemManageInterface.onManageClick(position);
                }
            });
        } else {
            holder.itemManage.setVisibility(View.GONE);
        }


        holder.itemLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemManageInterface.onMapShowClick(position);
            }
        });

        if (Car.getType() == 1) {
            holder.itemSaleOut.setVisibility(View.VISIBLE);
        } else {
            holder.itemSaleOut.setVisibility(View.GONE);
        }

        holder.itemInfoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageWatchActivity.launch(context, getImageUrlList(Car.getPic_img()), 0);
            }
        });

    }

    public void refresh(List<Car> data) {
        this.mCarList = data;
        notifyDataSetChanged();
    }

    private String getImageUrl(String url) {
        return url + "!300auto";
    }

    private ArrayList<String> getImageUrlList(List<Car.PicImgBean> pic_img) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < pic_img.size(); i++) {
            result.add(pic_img.get(i).getSavename() + "!auto");
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return mCarList.size();
    }

    public MyPublishListAdapter(Context context, List<Car> mCarList, onItemManageInterface onItemManageInterface, boolean isSelf) {
        this.itemManageInterface = onItemManageInterface;
        this.mCarList = mCarList;
        this.context = context;
        this.isUserSelf = isSelf;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemInfoImage;
        TextView itemImageCount;
        TextView itemBrandModelName;
        TextView itemLocation;
        TextView itemTime;
        TextView itemPrice;
        TextView itemManage;
        ImageView itemSaleOut;

        public ViewHolder(View view) {
            super(view);
            itemInfoImage = view.findViewById(R.id.item_info_image);
            itemImageCount = view.findViewById(R.id.item_image_count);
            itemSaleOut = view.findViewById(R.id.item_sale_out);
            itemBrandModelName = view.findViewById(R.id.item_brand_model_name);
            itemLocation = view.findViewById(R.id.item_location);
            itemTime = view.findViewById(R.id.item_time);
            itemPrice = view.findViewById(R.id.item_price);
            itemManage = view.findViewById(R.id.item_manage);
        }
    }

    public interface onItemManageInterface {
        void onManageClick(int position);

        void onMapShowClick(int position);
    }

}
