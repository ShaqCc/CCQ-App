package com.ccq.app.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccq.app.R;
import com.ccq.app.entity.Car;
import com.ccq.app.weidget.MyGridView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/3/27 21:19
 * 描述：
 * 版本：
 *
 **************************************************/

public class HomeAdapter extends RecyclerView.Adapter {

    private final int ITEM_COMMON = 1;
    private final int ITEM_FOOTER = 2;

    private List<Car> carList;
    private Context context;


    public HomeAdapter(List<Car> list) {
        this.carList = list;
    }


    public void refresh(List<Car> list) {
        this.carList = list;
        notifyDataSetChanged();
    }

    public void loadMore(List<Car> list) {
        carList.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM_FOOTER) {
            View inflate = inflater.inflate(R.layout.loadmore_layout, null, false);
            return new FooterHolder(inflate);
        } else {
            View inflate = inflater.inflate(R.layout.item_car_layout, null, false);
            return new CarHolder(inflate);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (carList != null && carList.size() > 0) return carList.size() + 1;
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1)
            return ITEM_FOOTER;

        else return ITEM_COMMON;
    }


    static class CarHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_car_iv_header)
        ImageView itemCarIvHeader;
        @BindView(R.id.item_car_tv_user_name)
        TextView itemCarTvUserName;
        @BindView(R.id.item_car_tv_car_name)
        TextView itemCarTvCarName;
        @BindView(R.id.item_car_tv_car_price)
        TextView itemCarTvCarPrice;
        @BindView(R.id.item_car_gridview)
        MyGridView itemCarGridview;
        @BindView(R.id.item_car_tv_car_info)
        TextView itemCarTvCarInfo;
        @BindView(R.id.item_car_tv_car_location)
        TextView itemCarTvCarLocation;
        @BindView(R.id.item_car_tv_publish_time)
        TextView itemCarTvPublishTime;
        @BindView(R.id.item_tv_call)
        TextView itemTvCall;
        @BindView(R.id.item_tv_message)
        TextView itemTvMessage;

        CarHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class FooterHolder extends RecyclerView.ViewHolder {

        FooterHolder(View itemView) {
            super(itemView);
        }
    }
}
