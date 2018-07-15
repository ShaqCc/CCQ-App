package com.ccq.app.ui.user;

import android.view.View;

import com.ccq.app.R;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.http.ApiService;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.utils.AppCache;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/7/14 22:21
 * 描述：
 * 版本：
 *
 **************************************************/

public class WantBuyFragment extends BaseFragment {
    ApiService apiService = RetrofitClient.getInstance().getApiService();
    @Override
    protected int inflateContentView() {
        return R.layout.fragment_my_home;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    public void initData() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("userid",AppCache.getUserBean().getUserid());
        params.put("page",1);
        params.put("size",100);
        apiService.getQiuGouList(params).enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {

            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {

            }
        });
    }
}
