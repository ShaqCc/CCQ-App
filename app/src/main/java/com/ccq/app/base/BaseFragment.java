package com.ccq.app.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ccq.app.R;
import com.ccq.app.utils.ViewState;

import butterknife.ButterKnife;

/****************************************
 * 功能说明:  fragment基类
 *
 * Author: Created by bayin on 2018/3/26.
 ****************************************/

public abstract class BaseFragment<T extends BasePresenter> extends LazyLoadFragment implements IBaseView {
    public T mPresenter;
    private BaseActivity mHostActivity;
    private View mRootView;
    private View errorLayout;
    private View emptyLayout;
    private View loadingLayout;
    private FrameLayout contentLayout;
    private View contentView;

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
    public void setViewState(ViewState state) {
        switch (state) {
            case STATE_CONTENT:
                loadingSuccess();
                break;
            case STATE_EMPTY:
                loadingEmpty();
                break;
            case STATE_ERROR:
                loadingError();
                break;
            case STATE_LOADING:
                loading();
                break;
        }
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




    /**
     * 显示加载页面
     */
    @Override
    public void loading() {
        loadingLayout.setVisibility(View.VISIBLE);
        contentLayout.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
    }




    /**
     * 显示加载成功的内容页面
     */
    @Override
    public void loadingSuccess() {
        contentLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
    }




    /**
     * 加载错误页面
     */
    @Override
    public void loadingError() {
        errorLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        contentLayout.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
    }




    /**
     * 空页面
     */
    @Override
    public void loadingEmpty() {
        emptyLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        contentLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
    }


    /**
     * @return 宿主activity
     */
    @Override
    public Activity get() {
        return mHostActivity;
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
            errorLayout = mRootView.findViewById(R.id.fragment_base_error_layout);
            emptyLayout = mRootView.findViewById(R.id.fragment_base_empty_layout);
            loadingLayout = mRootView.findViewById(R.id.fragment_base_loading_layout);
            contentLayout = mRootView.findViewById(R.id.fragment_base_content);
            contentView = inflater.inflate(inflateContentView(), null, false);//子view视图
            contentLayout.addView(contentView);
            ButterKnife.bind(this, contentView);
            initView(contentView);
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
