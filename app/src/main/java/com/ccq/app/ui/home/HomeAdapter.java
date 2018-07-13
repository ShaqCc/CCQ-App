package com.ccq.app.ui.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.ccq.app.R;
import com.ccq.app.base.CcqApp;
import com.ccq.app.entity.Car;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.ui.message.SingleChatActivity;
import com.ccq.app.ui.publish.BaseMapActivity;
import com.ccq.app.ui.reprot.ReportCarActivity;
import com.ccq.app.ui.user.LoginActivity;
import com.ccq.app.ui.user.UserInfoActivity;
import com.ccq.app.ui.user.UserSubscribeActivity;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.DensityUtils;
import com.ccq.app.utils.JmessageUtils;
import com.ccq.app.utils.ToastUtils;
import com.ccq.app.utils.Utils;
import com.ccq.app.weidget.MainCarImageLayout;
import com.ccq.app.weidget.Toasty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.eventbus.EventBus;
import jiguang.chat.application.JGApplication;
import jiguang.chat.entity.Event;
import jiguang.chat.entity.EventType;
import jiguang.chat.utils.ToastUtil;
import me.drakeet.materialdialog.MaterialDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/3/27 21:19
 * 描述：
 * 版本：
 *
 **************************************************/

public class HomeAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private final int ITEM_COMMON = 1;
    private final int ITEM_EMPTY = 2;

    public static int STATUS_EMPTY = 0;
    public static int STATUS_DATA = 1;

    private List<Car> carList;
    private Activity context;

    private int dataStatus = STATUS_DATA;


    public HomeAdapter(List<Car> list) {
        this.carList = list;
    }


    public void refresh(List<Car> list) {
        if (list != null && !list.isEmpty()) {
            dataStatus = STATUS_DATA;
            this.carList = list;
        } else {
            dataStatus = STATUS_EMPTY;
            this.carList.clear();
        }
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
        if (viewType == ITEM_EMPTY) {
            View inflate = inflater.inflate(R.layout.item_no_data, parent, false);
            return new FooterHolder(inflate);
        } else {
            View inflate = inflater.inflate(R.layout.item_car_layout, parent, false);
            return new CarHolder(inflate);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {

            case ITEM_COMMON:
                final CarHolder carHolder = (CarHolder) holder;
                final Car carBean = carList.get(position);
                //头像
                Glide.with(context).load(carBean.getUserInfo().getHeadimgurl()).into(carHolder.itemIvHeader);
                //用户昵称
                carHolder.itemTvUserName.setText(carBean.getUserInfo().getNickname());
                //车辆名称
                carHolder.itemTvCarName.setText(String.format("%s%s年", carBean.getName(), carBean.getYear()));
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
                    carHolder.itemGridview.setIsShowAll(false);
                    List<String> urlList = getImageUrlList(carBean.getPic_img());
                    carHolder.itemGridview.setUrlList(urlList);

                }
                //是否分享
                if (carBean.getIsshare().equals("1")) {
                    carHolder.ivMoments.setVisibility(View.VISIBLE);
                } else {
                    carHolder.ivMoments.setVisibility(View.GONE);
                }
                //点击事件
                carHolder.itemIvHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AppCache.getUserBean() == null) {
                            context.startActivity(new Intent(context, LoginActivity.class));
                            return;
                        }
                        Intent intentt =  new Intent(context ,UserInfoActivity.class);
                        intentt.putExtra("id",String.valueOf(carBean.getUserInfo().getUserid()));
                        context.startActivity(intentt);
                    }
                });
//                carHolder.itemIvHeader.setTag(carBean);
                carHolder.itemTvMessage.setOnClickListener(this);
                carHolder.itemTvMessage.setTag(carBean);
                carHolder.itemTvCall.setOnClickListener(this);
                carHolder.itemTvCall.setTag(carBean);
                carHolder.ivMoments.setOnClickListener(this);
                carHolder.itemTvCarLocation.setTag(carBean);
                carHolder.itemTvCarLocation.setOnClickListener(this);
                carHolder.ivTip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isShowJubao) {
                            carHolder.tvReport.setVisibility(View.GONE);
                        } else {
                            carHolder.tvReport.setVisibility(View.VISIBLE);
                        }
                        isShowJubao = !isShowJubao;
                    }
                });
                carHolder.tvReport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //举报
                        if (AppCache.getUserBean() == null || TextUtils.isEmpty(AppCache.getUserBean().getUserid())) {
                            Toasty.info(context, "请先登录", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent();
                            intent.setClass(context, ReportCarActivity.class);
                            intent.putExtra("car", carBean);
                            context.startActivity(intent);
                        }
                    }
                });
                break;
        }
    }

    private List<String> getImageUrlList(List<Car.PicImgBean> pic_img) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < pic_img.size(); i++) {
            result.add(pic_img.get(i).getSavename() + "!auto");
        }
        return result;
    }

    private String htmlInfo = "你可以添加分享号微信好友查看信息<br/><font color='red'>*注意<br/>请勿分享年限不实车辆<br/>请勿分享与二手装载机无关信息</font>";

    @Override
    public void onClick(View v) {
        Car car = (Car) v.getTag();
        switch (v.getId()) {
//            case R.id.item_car_iv_header:
//                if (AppCache.getUserBean() == null) {
//                    context.startActivity(new Intent(context, LoginActivity.class));
//                    return;
//                }
//                Intent intentt =  new Intent(context ,UserInfoActivity.class);
//                intentt.putExtra("id",String.valueOf(car.getUserInfo().getUserid()));
//                context.startActivity(intentt);
//                break;
            case R.id.item_tv_call:
                if (car != null)
                    Utils.call(context, car.getPhone());
                break;
            case R.id.item_tv_message:
                if (AppCache.getUserBean() == null) {
                    context.startActivity(new Intent(context, LoginActivity.class));
                    return;
                }
                UserInfo myInfo = JMessageClient.getMyInfo();
                if (myInfo == null) {
                    ToastUtil.shortToast(context, "IM未登录");
                    JmessageUtils.registerIM(AppCache.getUserBean());
                    return;
                }
                String userName = car.getUserInfo().getJiguang_name();
                if (TextUtils.isEmpty(userName)) {
                    ToastUtil.shortToast(context, "该用户未注册IM聊天系统");
                    return;
                }
//                mTargetId = intent.getStringExtra(TARGET_ID);
//                mTargetAppKey = intent.getStringExtra(TARGET_APP_KEY);
//                mTitle = intent.getStringExtra(JGApplication.CONV_TITLE);

//                String userName = JmessageUtils.getUserName(car.getUserInfo().getUserid() + "");
                Intent intent = new Intent(context, SingleChatActivity.class);
                intent.putExtra(JGApplication.CONV_TITLE, car.getUserInfo().getNickname());
                intent.putExtra(JGApplication.TARGET_ID, userName);
                intent.putExtra(JGApplication.TARGET_APP_KEY, CcqApp.jmappkey);
                context.startActivity(intent);


                Conversation conv = JMessageClient.getSingleConversation(userName, CcqApp.jmappkey);
                //如果会话为空，使用EventBus通知会话列表添加新会话
                if (conv == null) {
                    conv = Conversation.createSingleConversation(userName, CcqApp.jmappkey);
                    EventBus.getDefault().post(new Event.Builder()
                            .setType(EventType.createConversation)
                            .setConversation(conv)
                            .build());
                }
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
            case R.id.item_car_tv_car_location:
                getCarLocation(String.valueOf(car.getId()));
                break;
        }
    }

    private void getCarLocation(String carid) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("请稍后...");
        dialog.show();

        RetrofitClient.getInstance().getApiService()
                .getCarAddress(carid).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Map<String, Object> map = (Map<String, Object>) response.body();
                if (0.0 == (Double) map.get("code")) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    String address = (String) map.get("address");
                    String latitude = map.get("latitude").toString();
                    String longitude = map.get("longitude").toString();

                    Intent i = new Intent(context, BaseMapActivity.class);
                    LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    i.putExtra("latlng", point);
                    i.putExtra("address", address);
                    context.startActivity(i);

                } else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    ToastUtils.show(context, (String) map.get("message"));
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private boolean isShowJubao = false;


    @Override
    public int getItemCount() {
        if (dataStatus == STATUS_DATA) {
            if (carList != null && carList.size() > 0) return carList.size();
        }
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (dataStatus == STATUS_EMPTY) {
            return ITEM_EMPTY;
        } else return ITEM_COMMON;
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
        MainCarImageLayout itemGridview;
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
        @BindView(R.id.item_car_iv_tip)
        ImageView ivTip;
        @BindView(R.id.item_car_tv_report)
        TextView tvReport;

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
