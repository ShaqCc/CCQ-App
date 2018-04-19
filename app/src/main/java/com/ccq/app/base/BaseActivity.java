package com.ccq.app.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ccq.app.R;
import com.yan.inflaterauto.InflaterAuto;

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
    protected T mPresenter;
    private FrameLayout mContainer;
    private View mRootView;
    private RelativeLayout mToolBar;
    private TextView mToolBarTitle;
    private ImageView icFinish;

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
        setContentView(R.layout.activity_base);
        mContainer = findViewById(R.id.activity_base_container);
        //init toolbar
        mToolBar = findViewById(R.id.toolbar);
        mToolBarTitle = findViewById(R.id.toobar_title);
        icFinish = findViewById(R.id.ic_finish);
        icFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        icFinish.setVisibility(View.GONE);
        //add rootview
        mRootView = LayoutInflater.from(this).inflate(inflateContentView(), null, false);
        mContainer.addView(mRootView);

        ButterKnife.bind(this, mRootView);
        initView();
        initData();
    }

    public void setToolBarTitle(String title) {
        mToolBarTitle.setText(title);
    }

    public RelativeLayout getToolBar() {
        return mToolBar;
    }


    public void setToolBarVisible(boolean visible) {
        if (visible)
            mToolBar.setVisibility(View.VISIBLE);
        else mToolBar.setVisibility(View.GONE);
    }


    public void setBackIconVisible(boolean visible) {
        if (visible)
            icFinish.setVisibility(View.VISIBLE);
        else icFinish.setVisibility(View.GONE);
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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(InflaterAuto.wrap(base));
    }
}
