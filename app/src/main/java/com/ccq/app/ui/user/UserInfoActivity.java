package com.ccq.app.ui.user;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.base.CcqApp;
import com.ccq.app.entity.BaseBean;
import com.ccq.app.entity.SubscriberCountBean;
import com.ccq.app.entity.UserBanner;
import com.ccq.app.entity.UserBean;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.ui.message.SingleChatActivity;
import com.ccq.app.ui.user.adapter.MyFragmentAdapter;
import com.ccq.app.ui.user.introduce.TabIntroFragment;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.JmessageUtils;
import com.ccq.app.utils.Utils;
import com.ccq.app.weidget.SlidingTabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.eventbus.EventBus;
import jiguang.chat.application.JGApplication;
import jiguang.chat.entity.Event;
import jiguang.chat.entity.EventType;
import jiguang.chat.pickerimage.utils.ScreenUtil;
import jiguang.chat.utils.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends BaseActivity<UserPresenter> implements IUserView {

    @BindView(R.id.tv_my_attention_count)
    TextView tvMyAttentionCount;
    @BindView(R.id.tv_my_fans_count)
    TextView tvMyFansCount;
    @BindView(R.id.llyout_my_attention)
    RelativeLayout llyoutMyAttention;
    @BindView(R.id.layout_my_subscribe)
    LinearLayout layoutMySubscribe;
    @BindView(R.id.layout_my_subscribe_fans)
    LinearLayout layoutMySubscribeFans;
    @BindView(R.id.btn_invite_attation)
    TextView btnInviteAttation;
    @BindView(R.id.btn_vip_setting)
    LinearLayout btnUserVip;
    @BindView(R.id.tv_subscribe)
    TextView tvSubscribe;
    @BindView(R.id.tv_sms)
    TextView tvSms;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.user_opt)
    LinearLayout userOpt;


    @BindView(R.id.user_iv_header)
    ImageView ivHeader;

    @BindView(R.id.tv_userName)
    TextView tvName;
    @BindView(R.id.tv_account)
    TextView tvPhone;
    @BindView(R.id.tv_user_location)
    TextView tvLocation;
    @BindView(R.id.vp_my_info)
    ViewPager vpMyInfo;
    @BindView(R.id.user_pagerTabStrip)
    SlidingTabLayout pagerTabStrip;
    @BindView(R.id.usercenter_icon_vip)
    ImageView iconVipLogo;
    @BindView(R.id.user_iv_banner)
    ImageView ivBanner;
    @BindView(R.id.user_center_ll_setting)
    LinearLayout llSetting;

    private ProgressDialog dialogProgress;
    private String usrid;
    private MyFragmentAdapter adapter;
    private UserBean userBean;
    List<String> titles = new ArrayList<String>(Arrays.asList("首页", "求购", "简介"));
    List<BaseFragment> fragments = new ArrayList<BaseFragment>();

    @OnClick({R.id.tv_sms, R.id.tv_tel, R.id.tv_subscribe})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sms:
                //发信息
                if (AppCache.getUserBean() == null) {
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                UserInfo myInfo = JMessageClient.getMyInfo();
                if (myInfo == null) {
                    ToastUtil.shortToast(this, "IM未登录");
                    JmessageUtils.registerIM(AppCache.getUserBean());
                    return;
                }
                String userName = userBean.getJiguang_name();
                if (TextUtils.isEmpty(userName)) {
                    ToastUtil.shortToast(this, "该用户未注册IM聊天系统");
                    return;
                }
                final Intent intent = new Intent(this, SingleChatActivity.class);
                intent.putExtra(JGApplication.CONV_TITLE, AppCache.getUserBean().getNickname());
                intent.putExtra(JGApplication.TARGET_ID, userName);
                intent.putExtra(JGApplication.TARGET_APP_KEY, CcqApp.jmappkey);
                startActivity(intent);
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
            case R.id.tv_tel:
                //打电话
                Utils.call(this, userBean.getMobile());
                break;
            case R.id.tv_subscribe:
                //加关注
                if (getString(R.string.add_watch).equals(tvSubscribe.getText())) {
                    RetrofitClient.getInstance().getApiService().setUserSubAdd(AppCache.getUserBean().getUserid(), userBean.getUserid()).enqueue(new Callback<Object>() {

                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            tvSubscribe.setText(R.string.cancel_watch);
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {

                        }
                    });
                } else {
                    showDialog("是否要取消关注");
                }
                break;
        }
    }

    /**
     * 弹窗提示
     * @param message
     */
    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("操作提示：");
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String, String> map = new HashMap<>();
                map.put("userid", AppCache.getUserBean().getUserid());
                map.put("subuser", userBean.getUserid());
                RetrofitClient.getInstance().getApiService().setUserSubRemove(map).enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        tvSubscribe.setText(R.string.add_watch);
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }

    @Override
    protected int inflateContentView() {
        return R.layout.fragment_user;
    }

    @Override
    protected UserPresenter createPresenter() {
        return new UserPresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        usrid = getIntent().getStringExtra("id");
        setToolBarTitle("用户信息");
        //底部操作
        userOpt.setVisibility(View.VISIBLE);
        //会员
        btnUserVip.setVisibility(View.GONE);
        btnInviteAttation.setVisibility(View.GONE);
        llSetting.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.getUserInfo(usrid);
        //是否关注了该用户
        mPresenter.getSubscirberInfo(usrid);
        //获取用户信息
        mPresenter.getUserInfo(usrid);
        //banner        mPresenter.getUserBanner(usrid);
        //关注人数，被关注人数
        mPresenter.getSubscribeCount(usrid);
    }


    @Override
    public BaseActivity getHostActivity() {
        return this;
    }

    @Override
    public void setUserView(UserBean userBean) {
        this.userBean = userBean;
        Glide.with(this).load(userBean.getHeadimgurl()).into(ivHeader);
        if (userBean.getVip() == 1) {
            iconVipLogo.setImageResource(R.drawable.icon_vip_enable);
        } else {
            iconVipLogo.setImageResource(R.drawable.icon_no_vip);
        }
        setView();
    }

    public void setView() {
        tvName.setText(userBean.getNickname());
        tvPhone.setText(userBean.getMobile());
        tvLocation.setText(userBean.getProvinceName() + "·" + userBean.getCityName());

        llyoutMyAttention.setVisibility(View.VISIBLE);
        TabHomeFragment tabHomeFragment = new TabHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(TabHomeFragment.KEY_IS_SELF, false);
        bundle.putSerializable("bean",userBean);
        tabHomeFragment.setArguments(bundle);
        fragments.add(tabHomeFragment);
        fragments.add(new WantBuyFragment());
        TabIntroFragment info = new TabIntroFragment();
        info.setArguments(bundle);
        fragments.add(info);
        adapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments, titles);

        vpMyInfo.setAdapter(adapter);

        pagerTabStrip.setDistributeEvenly(true);
        pagerTabStrip.setCustomTabView(R.layout.custorm_tab_layout, R.id.tv_tab);
        pagerTabStrip.setSelectedIndicatorColors(Color.parseColor("#256ba6"));
        pagerTabStrip.setTitleTextColor(Color.parseColor("#256ba6"), getResources().getColor(R.color.black_de));
        pagerTabStrip.setTabStripWidth(ScreenUtil.getDisplayWidth() / 3);
        pagerTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                adapter.getItem(position).initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pagerTabStrip.setViewPager(vpMyInfo);
    }

    @Override
    public void setBanner(UserBanner banner) {
        if (!TextUtils.isEmpty(banner.getImage())) {
            Glide.with(getHostActivity()).load(banner.getImage()).into(ivBanner);
        }
    }

    @Override
    public void setSubscriber(BaseBean baseBean) {
        if (baseBean.getCode() == 0) {
            //已经订阅
            tvSubscribe.setTextColor(R.string.cancel_watch);
        } else {
            //未订阅
            tvSubscribe.setText(R.string.add_watch);
        }
    }

    @Override
    public void setSubCount(SubscriberCountBean bean) {
        tvMyAttentionCount.setText(String.valueOf(bean.getV1()));
        tvMyFansCount.setText(String.valueOf(bean.getV2()));
    }

    @Override
    public void dismissProgress() {

    }
}
