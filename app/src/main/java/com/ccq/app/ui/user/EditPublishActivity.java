package com.ccq.app.ui.user;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.entity.Car;
import com.ccq.app.http.ApiService;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.ui.publish.PublishFragment;
import com.ccq.app.utils.AppCache;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPublishActivity extends BaseActivity {

    @BindView(R.id.ic_finish)
    ImageView icFinish;
    @BindView(R.id.toobar_title)
    TextView toobarTitle;

    private Car car;

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
        toobarTitle.setText("修改信息");

        car = (Car) getIntent().getSerializableExtra("bean");
        getCarInfo();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment fragment = new PublishFragment();
        transaction.add(R.id.add_fragment,fragment);
        transaction.commit();
        setToolBarVisible(false);
    }

    private void getCarInfo() {
        RetrofitClient.getInstance().getApiService().getCarInfo(AppCache.getUserBean().getUserid(),String.valueOf(car.getId())).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.body()!=null){
                    Map<String,Object> map = (Map<String, Object>) response.body();

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }

    @OnClick(R.id.ic_finish)
    public void onViewClicked() {
        finish();
    }




}
