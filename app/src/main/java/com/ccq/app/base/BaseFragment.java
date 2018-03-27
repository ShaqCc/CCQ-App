package com.ccq.app.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ccq.app.R;
import com.ccq.app.weidget.MultiStateView;

import butterknife.ButterKnife;

/****************************************
 * 功能说明:  fragment基类
 *
 * Author: Created by bayin on 2018/3/26.
 ****************************************/

public abstract class BaseFragment<T extends BasePresenter> extends LazyLoadFragment {
    public T mPresenter;
    private BaseActivity mHostActivity;
    private View mRootView;
    private MultiStateView mMulStateView;

    /**
     * @return 创建页面布局
     */
    protected abstract int inflateContentView();

    /**
     * @return 创建控制器
     */
    protected abstract T createPresenter();

    /**
     * @param rootView 初始化内容布局
     */
    protected abstract void initView(View rootView);

    /**
     * 获取数据
     */
    public abstract void initData();

    /**
     * 设置页面状态显示
     *
     * @param state 状态
     */
    public void setViewState(@MultiStateView.ViewState int state) {
        mMulStateView.setViewState(state);
    }


    /**
     * 刷新当前页面数据，如果是第一次可见，不请求数据
     */
    public void refreshUI() {
        if (!isFirstEnter()) initData();
    }

    /**
     * @return 当前宿主Activity
     */
    protected BaseActivity getHostActivity() {
        return mHostActivity;
    }

    /**
     * fragment初次可见，调用initData方法
     */
    @Override
    protected void onFragmentFirstVisible() {
        initData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_base_layout, container, false);
            mMulStateView = mRootView.findViewById(R.id.fragment_base_rootview);
            ButterKnife.bind(mRootView);
            mMulStateView.setViewForState(inflateContentView(), MultiStateView.VIEW_STATE_CONTENT);
            initView(mRootView);
        } else {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) parent.removeView(mRootView);
        }
        return mRootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHostActivity = (BaseActivity) context;
    }
}
