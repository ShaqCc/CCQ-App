package com.ccq.app.ui.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.entity.Car;
import com.ccq.app.entity.UserBean;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.ui.publish.PublishFragment;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.JmessageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.ic_finish)
    ImageView icFinish;
    @BindView(R.id.toobar_title)
    TextView toobarTitle;

    public UserBean usr;

    @Override
    protected int inflateContentView() {
        return R.layout.activity_edit_publish;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        super.initView();
        toobarTitle.setText("用户信息");
        setToolBarVisible(false);

        String  usrid =  getIntent().getStringExtra("id");
        getCarInfo(usrid);

    }

    private void showPage(){

        getIntent().putExtra("bean",usr);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(TabHomeFragment.KEY_IS_SELF,false);
        fragment.setArguments(bundle);
        transaction.add(R.id.add_fragment,fragment);
        transaction.commit();
    }

    private void getCarInfo(String userid) {
        if (!TextUtils.isEmpty(userid)) {
            RetrofitClient.getInstance().getApiService().getUser(userid)
                    .enqueue(new Callback<UserBean>() {
                        @Override
                        public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                            if (response.isSuccessful() && response.body()!=null) {
                                usr = response.body();
                                showPage();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserBean> call, Throwable t) {

                        }
                    });
        }

    }

    @OnClick(R.id.ic_finish)
    public void onViewClicked() {
        finish();
    }

}
