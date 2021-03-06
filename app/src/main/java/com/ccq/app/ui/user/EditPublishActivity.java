package com.ccq.app.ui.user;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.entity.Car;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.ui.publish.PublishFragment;
import com.ccq.app.utils.AppCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
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
    Fragment fragment;

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
        setToolBarVisible(false);

        car = (Car) getIntent().getSerializableExtra("bean");
        getCarInfo();


    }

    private void showPage(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        fragment = new PublishFragment();
        transaction.add(R.id.add_fragment,fragment);
        transaction.commit();
    }

    private void getCarInfo() {
        RetrofitClient.getInstance().getApiService().getCarInfo(AppCache.getUserBean().getUserid(),String.valueOf(car.getId())).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.body()!=null){
                    Map<String,Object>  map = (Map<String, Object>) response.body();
                    Map<String,Object>  maps = ((Map)map.get("data"));
                    car.setNumberName((String) maps.get("NumberName"));
                    car.setBrandName((String) maps.get("BrandName"));
                    car.setTonnageName((String) maps.get("TonnageName"));
                    car.setPic((String) maps.get("pic"));
                    car.setVideoIds((String) maps.get("video"));

                    car.setBrandId( String.valueOf(Double.valueOf(maps.get("b_id").toString()).intValue()));
                    car.setNumberId(String.valueOf(Double.valueOf(maps.get("n_id").toString()).intValue()));
                    car.setTonnageId(String.valueOf(Double.valueOf(maps.get("t_id").toString()).intValue()));
                    showPage();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                showPage();
            }
        });

    }

    @OnClick(R.id.ic_finish)
    public void onViewClicked() {
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(fragment!=null) fragment.onActivityResult(requestCode,resultCode,data);
    }
}
