package com.ccq.app.ui.home;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ccq.app.R;
import com.ccq.app.entity.BannerBean;
import com.ccq.app.entity.Car;
import com.ccq.app.http.ApiParams;
import com.ccq.app.ui.city.ProvinceActivity;
import com.ccq.app.utils.DensityUtils;
import com.ccq.app.utils.GlideImageLoader;
import com.ccq.app.utils.Utils;
import com.ccq.app.weidget.MyGridView;
import com.previewlibrary.PhotoActivity;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.materialdialog.MaterialDialog;
import me.gujun.android.taggroup.TagGroup;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/3/27 21:19
 * 描述：
 * 版本：
 *
 **************************************************/

public class HomeAdapter extends RecyclerView.Adapter implements View.OnClickListener{

    private final int ITEM_COMMON = 1;
    private final int ITEM_FOOTER = 2;

    private List<Car> carList;
    private Activity context;


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
        context = (Activity) parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM_FOOTER) {
            View inflate = inflater.inflate(R.layout.loadmore_layout, parent, false);
            return new FooterHolder(inflate);
        } else {
            View inflate = inflater.inflate(R.layout.item_car_layout, parent, false);
            return new CarHolder(inflate);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
//            case ITEM_BANNER:
//                BannerHolder bannerHolder = (BannerHolder) holder;
//                setBannerHolder((BannerHolder) holder);
//                //设置轮播图
//                //设置图片加载器
//                bannerHolder.banner.setImageLoader(new GlideImageLoader());
//                //设置图片集合
//                bannerHolder.banner.setImages(getBannerImages());
//                //banner设置方法全部调用完毕时最后调用
//                bannerHolder.banner.start();
//                //点击事件
//                bannerHolder.mItemBannerRbCity.setOnCheckedChangeListener(this);
//                bannerHolder.mItemBannerRbBrand.setOnClickListener(this);
//                bannerHolder.mItemBannerRbSize.setOnClickListener(this);
//                bannerHolder.mItemBannerRbAge.setOnClickListener(this);
//                bannerHolder.mItemBannerRbOrder.setOnClickListener(this);
//                //tag view
//                updateTagGroup();
//                break;
            case ITEM_COMMON:
                final CarHolder carHolder = (CarHolder) holder;
                Car carBean = carList.get(position);
                //头像
                Glide.with(context).load(carBean.getUserInfo().getHeadimgurl())
                        .placeholder(R.mipmap.ic_default_thumb).into(carHolder.itemIvHeader);
                //用户昵称
                carHolder.itemTvUserName.setText(carBean.getUserInfo().getNickname());
                //车辆名称
                carHolder.itemTvCarName.setText(String.format("%s  %s年", carBean.getName(), carBean.getYear()));
                //车辆价格
                try {
                    String price = carBean.getPrice();
                    if (Float.parseFloat(price) > 0) {
                        carHolder.itemTvCarPrice.setText(String.format("%s万", price));
                    } else {
                        carHolder.itemTvCarPrice.setText("面议");
                    }
                } catch (Exception e) {
                    carHolder.itemTvCarPrice.setText("面议");
                }
                //车辆介绍
                if ((TextUtils.isEmpty(carBean.getContent()))) {
                    carHolder.itemTvCarInfo.setVisibility(View.GONE);
                } else {
                    carHolder.itemTvCarInfo.setVisibility(View.VISIBLE);
                    carHolder.itemTvCarInfo.setText(carBean.getContent());
                }
                //车辆地址
                carHolder.itemTvCarLocation.setText(String.format("%s·%s", carBean.getProvinceName(), carBean.getCityName()));
                //发布时间
                carHolder.itemTvPublishTime.setText(carBean.getAddtime_format());
                //图片
                ViewGroup.LayoutParams layoutParams = carHolder.itemGridview.getLayoutParams();
                layoutParams.width = DensityUtils.dp2px(context, 90 * 3 + 8);
                carHolder.itemGridview.setLayoutParams(layoutParams);
                if (carBean.getPic_img() == null || carBean.getPic_img().size() == 0) {
                    carHolder.itemGridview.setVisibility(View.GONE);
                } else {
                    carHolder.itemGridview.setVisibility(View.VISIBLE);
                    final PictureAdapter adapter = new PictureAdapter(carBean.getPic_img(), carBean.getPic_img_count());
                    carHolder.itemGridview.setAdapter(adapter);
                    //准备图片数据
                    carHolder.itemGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            computeBoundsBackward(carHolder.itemGridview);
                            PhotoActivity.startActivity(context, adapter.getThumbList(), position);
                        }
                    });
                }
                //是否分享
                if (carBean.getIsshare().equals("1")) {
                    carHolder.ivMoments.setVisibility(View.VISIBLE);
                } else {
                    carHolder.ivMoments.setVisibility(View.GONE);
                }
                //点击事件
                carHolder.itemTvMessage.setOnClickListener(this);
                carHolder.itemTvMessage.setTag(carBean);
                carHolder.itemTvCall.setOnClickListener(this);
                carHolder.itemTvCall.setTag(carBean);
                carHolder.ivMoments.setOnClickListener(this);
                break;
        }
    }


    private String htmlInfo = "你可以添加分享号微信好友查看信息<br/><font color='red'>*注意<br/>请勿分享年限不实车辆<br/>请勿分享与二手装载机无关信息</font>";

    @Override
    public void onClick(View v) {
        Car car = (Car) v.getTag();
        switch (v.getId()) {
            case R.id.item_tv_call:
                if (car != null)
                    Utils.call(context, car.getPhone());
                break;
            case R.id.item_tv_message:
                //todo
                break;
            case R.id.item_car_iv_moments:
                final MaterialDialog dialog = new MaterialDialog(context);
                dialog.setTitle("此条信息分享号已经分享成功")
                        .setMessage(Html.fromHtml(htmlInfo))
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                dialog.show();
                break;
            case R.id.item_car_iv_tip:
                //todo
                break;
        }
    }



    public interface OnFilterListener {
        void onFilter();
    }

    private void computeBoundsBackward(MyGridView gridView) {
        PictureAdapter adapter = (PictureAdapter) gridView.getAdapter();
        for (int i = gridView.getFirstVisiblePosition(); i < adapter.getCount(); i++) {
            View itemView = gridView.getChildAt(i);
            Rect bounds = new Rect();
            if (itemView != null) {
                ImageView thumbView = itemView.findViewById(R.id.imageview);
                thumbView.getGlobalVisibleRect(bounds);
            }
            adapter.getThumbList().get(i).setBounds(bounds);
        }
    }


    @Override
    public int getItemCount() {
        if (carList != null && carList.size() > 0) return carList.size() + 1;
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount())
            return ITEM_FOOTER;

        else return ITEM_COMMON;
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
        @BindView(R.id.item_car_iv_moments)
        ImageView ivMoments;

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
