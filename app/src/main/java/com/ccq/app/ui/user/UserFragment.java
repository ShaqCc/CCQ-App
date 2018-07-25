package com.ccq.app.ui.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.base.CcqApp;
import com.ccq.app.entity.BaseBean;
import com.ccq.app.entity.SubscriberCountBean;
import com.ccq.app.entity.UserBanner;
import com.ccq.app.entity.UserBean;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.ui.ImageWatchActivity;
import com.ccq.app.ui.message.SingleChatActivity;
import com.ccq.app.ui.user.adapter.MyFragmentAdapter;
import com.ccq.app.ui.user.introduce.TabIntroFragment;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.Constants;
import com.ccq.app.utils.JmessageUtils;
import com.ccq.app.utils.MMAlert;
import com.ccq.app.utils.SharedPreferencesUtils;
import com.ccq.app.utils.ToastUtils;
import com.ccq.app.utils.Utils;
import com.ccq.app.weidget.SlidingTabLayout;
import com.dmcbig.mediapicker.PickerActivity;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.entity.Media;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dmcbig.mediapicker.PickerConfig.RESULT_CODE;

/****************************************
 * 功能说明:  我的
 *
 * Author: Created by bayin on 2018/3/26.
 ****************************************/

public class UserFragment extends BaseFragment<UserPresenter> implements IWXAPIEventHandler, IUserView {

    private static final int CHANGE_BANNER = 11;//更换banner请求
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
    @BindView(R.id.user_iv_banner)
    ImageView ivBanner;
    @BindView(R.id.user_center_ll_setting)
    LinearLayout llSetting;


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
    protected UserPresenter createPresenter() {
        return new UserPresenter(this);
    }

    @Override
    protected void initView(View rootView) {
//        EventBus.getDefault().register(this);
        userBean = (UserBean) get().getIntent().getSerializableExtra("bean");
        if (userBean != null) {
            isMine = false;
            //底部操作
            userOpt.setVisibility(View.VISIBLE);
            //会员
            btnUserVip.setVisibility(View.GONE);
            btnInviteAttation.setVisibility(View.GONE);
            llSetting.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {
        if (isMine) {
            //用户自己的
            userBean = AppCache.getUserBean();
        } else {
            /*查看别人的信息*/
            //头像
            Glide.with(get()).load(userBean.getHeadimgurl()).into(ivHeader);
            //是否关注了该用户
            mPresenter.getSubscirberInfo(userBean.getUserid());
        }
        if (userBean != null) {
            //获取用户信息
            mPresenter.getUserInfo(userBean.getUserid());
            //banner
            mPresenter.getUserBanner(userBean.getUserid());
            //关注人数，被关注人数
            mPresenter.getSubscribeCount(userBean.getUserid());
        } else {
            resetUserInfo();
        }
    }

    /**
     * c重置界面
     */
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //接收banner的结果
        if (requestCode == CHANGE_BANNER && resultCode == RESULT_CODE) {
            ArrayList<Media> select = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            if (select != null && !select.isEmpty()) {
                Glide.with(getHostActivity()).load(select.get(0).path).into(ivBanner);
                //上传图片
                mPresenter.changeBanner(select.get(0).path);
            }
        }

    }

    /**
     * 设置界面信息，viewpager
     */
    public void setView() {
        if (userBean == null) {
            userBean = AppCache.getUserBean();
        }
        tvName.setText(userBean.getNickname());
        tvPhone.setText(userBean.getMobile());
        tvLocation.setText(userBean.getProvinceName() + "·" + userBean.getCityName());

        llyoutMyAttention.setVisibility(View.VISIBLE);
        TabHomeFragment tabHomeFragment = new TabHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(TabHomeFragment.KEY_IS_SELF, isMine);
        tabHomeFragment.setArguments(bundle);
        fragments.add(tabHomeFragment);
        fragments.add(new WantBuyFragment());
        fragments.add(new TabIntroFragment());
        adapter = new MyFragmentAdapter(getActivity().getSupportFragmentManager(), fragments, titles);

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

    private void showUserSettingDialog() {
        String[] items = {"二维码水印", "修改图片", "切换账号", "更新用户信息"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(getHostActivity());
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent i = new Intent(getHostActivity(), SetWechatQRActivity.class);
                        startActivity(i);
                        break;
                    case 1:
                        //非会员或过期提醒
                        changeBanner();
                        break;
                    case 2:
                        //退出登录
                        SharedPreferencesUtils.setParam(getHostActivity(), Constants.USER_ID, "");
                        AppCache.setUserBean(null);
                        resetUserInfo();
                        break;
                    case 3:
                        initData();
                        break;
                }
            }
        });
        listDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.layout_my_subscribe, R.id.layout_my_subscribe_fans,
            R.id.btn_vip_setting, R.id.btn_invite_attation, R.id.tv_sms,
            R.id.tv_tel, R.id.tv_subscribe, R.id.user_center_ll_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_center_ll_setting:
                //设置
                showUserSettingDialog();
                break;
            case R.id.layout_my_subscribe:
                if (isMine) {
                    Intent i = new Intent(getActivity(), UserSubscribeActivity.class);
                    i.putExtra("type", 0);
                    getActivity().startActivity(i);
                }
                break;
            case R.id.layout_my_subscribe_fans:
                if (isMine) {
                    Intent ii = new Intent(getActivity(), UserSubscribeActivity.class);
                    ii.putExtra("type", 1);
                    getActivity().startActivity(ii);
                }
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
                final Intent intent = new Intent(get(), SingleChatActivity.class);
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

    private void changeBanner() {
        if (AppCache.getUserBean() != null && isMine) {
            if (AppCache.getUserBean().isMember() || AppCache.getUserBean().isBusiness()) {
                //切换banner
                Intent intent = new Intent(getHostActivity(), PickerActivity.class);
                intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE);//default image and video (Optional)
                long maxSize = 1024 * 1024 * 2;//long long long long类型
                intent.putExtra(PickerConfig.MAX_SELECT_SIZE, maxSize); //default 180MB (Optional)
                intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 1);  //default 40 (Optional)
                getHostActivity().startActivityForResult(intent, CHANGE_BANNER);
            } else {
                //非会员提示
                MMAlert.showAlert(getHostActivity(), "此功能只有会员可用，是否开通会员", "提示", "开通会员", "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //开会员
                                Intent intent1 = new Intent(getHostActivity(), OpenVipActivity.class);
                                startActivity(intent1);
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //取消
                            }
                        });
            }
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
    public BaseActivity getHostActivity() {
        return (BaseActivity) getActivity();
    }

    @Override
    public void setUserView(UserBean userBean) {
        Glide.with(get()).load(userBean.getHeadimgurl()).into(ivHeader);
        if (userBean.getVip() == 1) {
            iconVipLogo.setImageResource(R.drawable.icon_vip_enable);
        } else {
            iconVipLogo.setImageResource(R.drawable.icon_no_vip);
        }
        setView();
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
        //   v1:10  我订阅的， v2:10   订阅我的
        tvMyAttentionCount.setText(String.valueOf(bean.getV1()));
        tvMyFansCount.setText(String.valueOf(bean.getV2()));
    }

    @Override
    public void dismissProgress() {
        dismiss();
    }
}
