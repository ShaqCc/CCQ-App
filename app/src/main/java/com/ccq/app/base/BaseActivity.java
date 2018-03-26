package com.ccq.app.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/****************************************
 * 功能说明:  activity 基类
 *
 * Author: Created by bayin on 2018/3/26.
 ****************************************/

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {

    private WeakReference<BaseActivity> mCurrentActivity;
    private List<BaseActivity> mActivityList = new ArrayList<>();
    private T mPresenter;

    /**
     * @return activity页面的xml布局
     */
    protected abstract int inflateContentView();

    /**
     * @return 创建页面控制器
     */
    protected abstract T createPresenter();

    /**
     * 初始化页面
     */
    protected void initView() {
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * @return 当前activity
     */
    protected BaseActivity getCurrentActivity() {
        return mCurrentActivity.get();
    }

    /**
     * @return 当前应用栈所有的activity
     */
    protected List<BaseActivity> getActivityList() {
        return mActivityList;
    }

    @Override
    final protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        synchronized (BaseActivity.class) {
            mActivityList.add(this);
        }
        mPresenter = createPresenter();
        setContentView(inflateContentView());
        ButterKnife.bind(this);
        initView();
        initData();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mCurrentActivity = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentActivity = new WeakReference<BaseActivity>(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityList.remove(this);
        if (mPresenter != null)
            mPresenter.detachView();
    }
}
