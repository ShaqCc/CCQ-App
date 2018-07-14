package com.ccq.app.ui.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ccq.app.R;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.base.CcqApp;
import com.ccq.app.entity.UserBean;
import com.ccq.app.http.ApiService;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.ui.ImageWatchActivity;
import com.ccq.app.ui.message.SingleChatActivity;
import com.ccq.app.ui.user.adapter.MyFragmentAdapter;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.Constants;
import com.ccq.app.utils.JmessageUtils;
import com.ccq.app.utils.SharedPreferencesUtils;
import com.ccq.app.utils.ToastUtils;
import com.ccq.app.utils.Utils;
import com.ccq.app.weidget.SlidingTabLayout;
import com.ccq.app.weidget.Toasty;
import com.google.gson.jpush.JsonObject;
import com.google.gson.jpush.JsonParser;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/****************************************
 * 功能说明:  我的
 *
 * Author: Created by bayin on 2018/3/26.
 ****************************************/

public class UserFragment extends BaseFragment implements IWXAPIEventHandler {

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
    Button btnInviteAttation;
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

    private MyFragmentAdapter adapter;

    @BindView(R.id.user_iv_header)
    ImageView ivHeader;

    @BindView(R.id.tv_userName)
    TextView tvName;
    @BindView(R.id.tv_account)
    TextView tvPhone;
    @BindView(R.id.tv_user_location)
    TextView tvLocation;

    Unbinder unbinder;
    @BindView(R.id.vp_my_info)
    ViewPager vpMyInfo;
    @BindView(R.id.user_pagerTabStrip)
    SlidingTabLayout pagerTabStrip;
    @BindView(R.id.usercenter_icon_vip)
    ImageView iconVipLogo;

    private ProgressDialog dialogProgress;

    public boolean isMine = true;//用户资料信息

    private UserBean userBean;

    @OnClick(R.id.user_iv_header)
    public void login() {
        if (userBean == null) {
            userBean = AppCache.getUserBean();
        }
        if (userBean != null) {
            ArrayList<String> objects = new ArrayList<>();
            objects.add(userBean.getHeadimgurl());
            ImageWatchActivity.launch(get(), objects, 0);
        } else {
            startActivity(new Intent(get(), LoginActivity.class));
        }
    }

    List<String> titles = new ArrayList<String>(Arrays.asList("首页", "求购", "简介"));
    List<BaseFragment> fragments = new ArrayList<BaseFragment>();

    @Override
    protected int inflateContentView() {
        return R.layout.fragment_user;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
//        EventBus.getDefault().register(this);
        userBean = (UserBean) get().getIntent().getSerializableExtra("bean");
        if (userBean != null) {
            isMine = false;
            userOpt.setVisibility(View.VISIBLE);
            layoutMySubscribe.setClickable(false);
            layoutMySubscribe.setFocusable(false);
            layoutMySubscribeFans.setClickable(false);
            layoutMySubscribeFans.setFocusable(false);
            btnInviteAttation.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {
        if (userBean == null) {
            userBean = AppCache.getUserBean();
        } else {
            RetrofitClient.getInstance().getApiService().checkSubscribe(AppCache.getUserBean().getUserid(), userBean.getUserid()).enqueue(new Callback<Object>() {

                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Map<String, Object> map = (Map<String, Object>) response.body();
                    if (0.0 == (Double) map.get("code")) {
                        tvSubscribe.setText("已关注");
                    } else {
                        tvSubscribe.setText("未关注");
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });
        }
        if (userBean != null) {
            Glide.with(get()).load(userBean.getHeadimgurl()).into(ivHeader);
            setView();
            getData();
        } else {
            resetUserInfo();
        }
    }

    private void resetUserInfo() {
        ivHeader.setImageResource(R.drawable.icon_no_login);
        tvName.setText("请登录");
        tvPhone.setText("手机号");
        tvLocation.setText("当前位置");
    }

    private void showProgress(String msg) {
        if (dialogProgress == null) {
            dialogProgress = new ProgressDialog(get());
        }
        dialogProgress.setMessage(msg);
        dialogProgress.show();
    }

    void dismiss() {
        if (dialogProgress != null && dialogProgress.isShowing()) {
            dialogProgress.dismiss();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isMine) {
            String unionId = (String) SharedPreferencesUtils.getParam(get(), Constants.KEY_UNIONID, "");
            if (!TextUtils.isEmpty(unionId)) {
                showProgress("刷新中...");
                RetrofitClient.getInstance().getApiService().getUserByUniondId(unionId)
                        .enqueue(new Callback<UserBean>() {
                            @Override
                            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                                dismiss();
                                UserBean userBean = response.body();
                                if (getActivity() != null && response.isSuccessful() && userBean != null) {
                                    AppCache.setUserBean(userBean);
                                    Glide.with(get()).load(userBean.getHeadimgurl()).into(ivHeader);
                                    if (userBean.getVip() == 1) {
                                        iconVipLogo.setImageResource(R.drawable.icon_vip_enable);
                                    } else {
                                        iconVipLogo.setImageResource(R.drawable.icon_no_vip);
                                    }
                                    setView();
                                    getData();
                                }
                                Toasty.success(get(), "更新成功！").show();
                            }

                            @Override
                            public void onFailure(Call<UserBean> call, Throwable t) {
                                dismiss();
                                Toasty.error(get(), t.getMessage()).show();
                            }
                        });
            }
        }
    }

    public void setView() {
        if (userBean == null) {
            userBean = AppCache.getUserBean();
        }
        tvName.setText(userBean.getNickname());
        tvPhone.setText(userBean.getMobile());
        tvLocation.setText(userBean.getProvinceName() + "·" + userBean.getCityName());

//        llyoutMyInfo.setVisibility(View.VISIBLE);
        llyoutMyAttention.setVisibility(View.VISIBLE);
        fragments.add(new TabHomeFragment());
        fragments.add(new WantBuyFragment());
        fragments.add(new TabIntroFragment());
        adapter = new MyFragmentAdapter(getActivity().getSupportFragmentManager(), fragments, titles);

        vpMyInfo.setAdapter(adapter);

        pagerTabStrip.setDistributeEvenly(true);
        pagerTabStrip.setCustomTabView(R.layout.custorm_tab_layout, R.id.tv_tab);
        pagerTabStrip.setSelectedIndicatorColors(getActivity().getResources().getColor(R.color.colorPrimary));
        pagerTabStrip.setTitleTextColor(getResources().getColor(R.color.black_de), getResources().getColor(R.color.text_black_color));
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

    public void getData() {
        ApiService apiService = RetrofitClient.getInstance().getApiService();
        apiService.getSubscribeCount(userBean.getUserid()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response != null && response.body() != null) {
                    Object obj = response.body();
                    if (obj != null) {
                        JsonObject returnData = new JsonParser().parse(obj.toString()).getAsJsonObject();
                        //   v1:10  我订阅的， v2:10   订阅我的
                        tvMyAttentionCount.setText(String.valueOf(returnData.get("v1").getAsInt()));
                        tvMyFansCount.setText(String.valueOf(returnData.get("v2").getAsInt()));
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        int result = 0;

        Toast.makeText(getHostActivity(), "baseresp.getType = " + baseResp.getType(), Toast.LENGTH_SHORT).show();

        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_success;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = R.string.errcode_unsupported;
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }

        Toast.makeText(getHostActivity(), result, Toast.LENGTH_LONG).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.layout_my_subscribe, R.id.layout_my_subscribe_fans, R.id.btn_vip_setting, R.id.btn_invite_attation, R.id.tv_sms, R.id.tv_tel, R.id.tv_subscribe})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.layout_home:
//                vpMyInfo.setCurrentItem(0);
//                selectHome();
//                break;
//            case R.id.layout_intro:
//                vpMyInfo.setCurrentItem(1);
//                selectIntro();
//                break;
            case R.id.layout_my_subscribe:
                Intent i = new Intent(getActivity(), UserSubscribeActivity.class);
                i.putExtra("type", 0);
                getActivity().startActivity(i);
                break;
            case R.id.layout_my_subscribe_fans:
                Intent ii = new Intent(getActivity(), UserSubscribeActivity.class);
                ii.putExtra("type", 1);
                getActivity().startActivity(ii);
                break;
            case R.id.btn_vip_setting:
                startActivity(new Intent(get(), OpenVipActivity.class));
                break;
            case R.id.btn_invite_attation:


                break;
            case R.id.tv_sms:
                if (AppCache.getUserBean() == null) {
                    get().startActivity(new Intent(get(), LoginActivity.class));
                    return;
                }
                UserInfo myInfo = JMessageClient.getMyInfo();
                if (myInfo == null) {
                    ToastUtil.shortToast(get(), "IM未登录");
                    JmessageUtils.registerIM(AppCache.getUserBean());
                    return;
                }
                String userName = userBean.getJiguang_name();
                if (TextUtils.isEmpty(userName)) {
                    ToastUtil.shortToast(get(), "该用户未注册IM聊天系统");
                    return;
                }
//                mTargetId = intent.getStringExtra(TARGET_ID);
//                mTargetAppKey = intent.getStringExtra(TARGET_APP_KEY);
//                mTitle = intent.getStringExtra(JGApplication.CONV_TITLE);

//                String userName = JmessageUtils.getUserName(car.getUserInfo().getUserid() + "");
                Intent intent = new Intent(get(), SingleChatActivity.class);
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
                Utils.call(get(), userBean.getMobile());

                break;

            case R.id.tv_subscribe:
                //订阅
                if ("未关注".equals(tvSubscribe.getText())) {
                    RetrofitClient.getInstance().getApiService().setUserSubAdd(AppCache.getUserBean().getUserid(), userBean.getUserid()).enqueue(new Callback<Object>() {

                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            Map<String, Object> map = (Map<String, Object>) response.body();
                            if (0.0 == (Double) map.get("code")) {
                                ToastUtils.show(get(), "设置成功");
                                tvSubscribe.setText("已关注");
                            }
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


    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(get());
        builder.setTitle("操作提示：");
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap map = new HashMap<>();
                map.put("userid", AppCache.getUserBean().getUserid());
                map.put("subuser", userBean.getUserid());
                RetrofitClient.getInstance().getApiService().setUserSubRemove(map).enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        Map<String, Object> map = (Map<String, Object>) response.body();
                        if (0.0 == (Double) map.get("code")) {
                            ToastUtils.show(get(), "设置成功");
                            tvSubscribe.setText("未关注");
                        }

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

}
