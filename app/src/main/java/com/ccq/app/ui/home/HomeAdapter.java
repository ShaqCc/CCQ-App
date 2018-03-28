package com.ccq.app.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ccq.app.R;
import com.ccq.app.entity.BannerBean;
import com.ccq.app.entity.Car;
import com.ccq.app.utils.GlideImageLoader;
import com.ccq.app.weidget.MyGridView;
import com.youth.banner.Banner;

import java.lang.reflect.Array;
import java.util.ArrayList;
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

    private final int ITEM_BANNER = 0;
    private final int ITEM_COMMON = 1;
    private final int ITEM_FOOTER = 2;

    private List<Car> carList;
    private List<BannerBean> bannerList;
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

    public void setBanner(List<BannerBean> banner) {
        bannerList = banner;
        notifyItemChanged(0);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM_FOOTER) {
            View inflate = inflater.inflate(R.layout.loadmore_layout, null, false);
            return new FooterHolder(inflate);
        } else if (viewType == ITEM_BANNER) {
            View inflate = inflater.inflate(R.layout.item_banner, null, false);
            return new BannerHolder(inflate);
        } else {
            View inflate = inflater.inflate(R.layout.item_car_layout, null, false);
            return new CarHolder(inflate);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM_BANNER:
                BannerHolder bannerHolder = (BannerHolder) holder;
                //设置轮播图
                //设置图片加载器
                bannerHolder.banner.setImageLoader(new GlideImageLoader());
                //设置图片集合
                bannerHolder.banner.setImages(getImages());
                //banner设置方法全部调用完毕时最后调用
                bannerHolder.banner.start();
                break;
            case ITEM_COMMON:
                CarHolder carHolder = (CarHolder) holder;
                Car carBean = carList.get(position-1);
                Glide.with(context).load(carBean.getUserInfo().getHeadimgurl()+"!50auto")
                        .placeholder(R.mipmap.ic_default_thumb).into(carHolder.itemIvHeader);
                carHolder.itemTvUserName.setText(carBean.getUserInfo().getNickname());
                carHolder.itemTvCarPrice.setText(carBean.getPrice());
                break;
        }
    }

    private List<String> getImages(){
        ArrayList<String> strings = new ArrayList<>();
        if (bannerList!=null && bannerList.size()>0){
            for (BannerBean bean : bannerList) {
                strings.add(bean.getImage()+"!auto");
            }
        }
        return strings;
    }

    @Override
    public int getItemCount() {
        if (carList != null && carList.size() > 0) return carList.size() + 2;
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return ITEM_BANNER;

        else if (position == getItemCount() - 1)
            return ITEM_FOOTER;

        else return ITEM_COMMON;
    }


    static class BannerHolder extends RecyclerView.ViewHolder {

        private final Banner banner;

        public BannerHolder(View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.banner);
        }
    }


    static class CarHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_car_iv_header)
        ImageView itemIvHeader;
        @BindView(R.id.item_car_tv_user_name)
        TextView itemTvUserName;
        @BindView(R.id.item_car_tv_car_name)
        TextView itemTvCarName;
        @BindView(R.id.item_car_tv_car_price)
        TextView itemTvCarPrice;
        @BindView(R.id.item_car_gridview)
        MyGridView itemGridview;
        @BindView(R.id.item_car_tv_car_info)
        TextView itemTvCarInfo;
        @BindView(R.id.item_car_tv_car_location)
        TextView itemTvCarLocation;
        @BindView(R.id.item_car_tv_publish_time)
        TextView itemTvPublishTime;
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
