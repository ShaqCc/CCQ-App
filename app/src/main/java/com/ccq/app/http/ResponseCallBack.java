package com.ccq.app.http;

/****************************************
 * 功能说明:  网络请求回调
 *
 * Author: Created by bayin on 2018/3/27.
 ****************************************/

public abstract class ResponseCallBack<D> {

    protected abstract void onStart();

    protected abstract void onSuccess(D data);

    protected void onFailure(Throwable throwable) {
        throwable.printStackTrace();
    }

    protected abstract void onFinish();
}
