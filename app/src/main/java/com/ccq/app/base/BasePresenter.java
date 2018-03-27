package com.ccq.app.base;

import com.ccq.app.http.ApiService;
import com.ccq.app.http.RetrofitClient;

/****************************************
 * 功能说明:  控制器基类
 *
 * Author: Created by bayin on 2018/3/26.
 ****************************************/

public class BasePresenter<V> {
    public V mView;
    public ApiService apiService;

    public BasePresenter(V view) {
        apiService = RetrofitClient.getInstance().getApiService();
        mView = view;
    }

    public void detachView(){
        this.mView = null;
    }
}
