package com.ccq.app.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ccq.app.R;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.base.CcqApp;
import com.ccq.app.entity.UserBean;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.Constants;
import com.ccq.app.utils.ToastUtils;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/****************************************
 * 功能说明:  我的--简介 页签
 *****************************************/

public class TabIntroFragment extends BaseFragment {


    @BindView(R.id.tv_on_sale_count)
    TextView tvOnSaleCount;
    @BindView(R.id.tv_sale_out_count)
    TextView tvSaleOutCount;
    @BindView(R.id.tv_myinfo_edit)
    TextView tvMyinfoEdit;
    @BindView(R.id.tv_myinfo_content)
    TextView tvMyinfoContent;
    @BindView(R.id.tv_myimg_edit)
    TextView tvMyimgEdit;
    @BindView(R.id.myinfo_banner)
    Banner myinfoBanner;
    @BindView(R.id.tv_mylocation_edit)
    TextView tvMylocationEdit;
    @BindView(R.id.layout_mylocation_map)
    FrameLayout layoutMylocationMap;
    Unbinder unbinder;
    String defaultInfo = "我是%s，我来自%s，我的联系方式是：%s，如有业务请与我联系我吧";

    UserBean userBean;

    @Override
    protected int inflateContentView() {
        return R.layout.fragment_my_intro;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        userBean = (UserBean) get().getIntent().getSerializableExtra("bean");
        if(userBean==null){
            userBean = AppCache.getUserBean();
        }else{
            tvMyinfoEdit.setVisibility(View.GONE);
        }
        if (userBean != null) {
            String content = String.format(defaultInfo, userBean.getNickname(), userBean.getProvinceName() + userBean.getCityName(), userBean.getMobile());
            if (TextUtils.isEmpty(userBean.getContent())) {
                tvMyinfoContent.setText(content);
            } else {
                tvMyinfoContent.setText(userBean.getContent());
            }

            tvOnSaleCount.setText(String.valueOf(userBean.getZaishou_count()));
            tvSaleOutCount.setText(String.valueOf(userBean.getYishou_count()));
        }

    }

    @Subscribe
    public void onReceiveLoginSuccess(Integer eventId) {
        if(eventId == Constants.WX_LOGIN_SUCCESS){
            initData();
        }
    }

    @Subscribe
    public void onRefreshData(Integer eventId) {
        if(eventId == Constants.REFRESH_EVENT){
            getUser(AppCache.getUserBean().getUserid());
        }

    }

    /**
     * 获取用户信息
     * @param userid
     */
    void getUser(String userid){
        RetrofitClient.getInstance().getApiService().getUser(userid).enqueue(new Callback<UserBean>() {
            @Override
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                if (response.body()!=null){
                    AppCache.setUserBean(response.body());
                    initData();
                }
            }

            @Override
            public void onFailure(Call<UserBean> call, Throwable t) {
                ToastUtils.show(CcqApp.getAppContext(),t.getMessage());
            }
        });
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

    @OnClick(R.id.tv_myinfo_edit)
    public void onViewClicked() {
        Intent i = new Intent(get(),EditMyIntroActivity.class);
        get().startActivity(i);
    }
}
