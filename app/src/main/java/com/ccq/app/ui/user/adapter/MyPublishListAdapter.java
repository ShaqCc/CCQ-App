package com.ccq.app.ui.user.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ccq.app.R;
import com.ccq.app.entity.Car;

import java.util.List;

import butterknife.BindView;

/**
 * Created by littlemax on 2018/5/2.
 */

public class MyPublishListAdapter extends RecyclerView.Adapter<MyPublishListAdapter.ViewHolder> {

    List<Car> mCarList;
    Context context;
    public onItemManageInterface itemManageInterface;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_publish_info_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Car Car = mCarList.get(position);
        holder.itemImageCount.setText(String.valueOf(Car.getPic_img_count()));

        Glide.with(context)
                .load(getImageUrl(Car.getPicfmid_img().getSavename()))
                .into(holder.itemInfoImage);


        holder.itemBrandModelName.setText(Car.getName());
        holder.itemLocation.setText(Car.getCityName() + Car.getAddress());
        holder.itemTime.setText(Car.getAddtime_format() + " 展现：" +Car.getCount()+"次");

        if(Car.getPrice().equals("0")){
            holder.itemPrice.setText("面议");
        }else{
            holder.itemPrice.setText(Car.getPrice());
        }

        holder.itemManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemManageInterface.onManageClick(position);
            }
        });


    }

    private String getImageUrl(String url) {
        return url + "!300auto";
    }


    @Override
    public int getItemCount() {
        return mCarList.size();
    }

    public MyPublishListAdapter(Context context ,List<Car> mCarList, onItemManageInterface onItemManageInterface) {
        this.itemManageInterface= onItemManageInterface;
        this.mCarList = mCarList;
        this.context = context;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemInfoImage;
        TextView itemImageCount;
        TextView itemBrandModelName;
        TextView itemLocation;
        TextView itemTime;
        TextView itemPrice;
        TextView itemManage;

        public ViewHolder(View view) {
            super(view);
            itemInfoImage = (ImageView) view.findViewById(R.id.item_info_image);
            itemImageCount = (TextView) view.findViewById(R.id.item_image_count);

            itemBrandModelName = (TextView) view.findViewById(R.id.item_brand_model_name);
            itemLocation = (TextView) view.findViewById(R.id.item_location);
            itemTime = (TextView) view.findViewById(R.id.item_time);
            itemPrice = (TextView) view.findViewById(R.id.item_price);
            itemManage = (TextView) view.findViewById(R.id.item_manage);

        }
    }


    public interface onItemManageInterface{
        public void onManageClick(int position);
    }


}
