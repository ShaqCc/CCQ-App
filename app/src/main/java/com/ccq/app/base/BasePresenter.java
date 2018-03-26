package com.ccq.app.base;

/****************************************
 * 功能说明:  控制器基类
 *
 * Author: Created by bayin on 2018/3/26.
 ****************************************/

public class BasePresenter<V> {
    private V mView;

    public BasePresenter(V view) {
        mView = view;
    }

    public void detachView(){
        this.mView = null;
    }
}
