package com.ccq.app.ui.city;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.entity.Province;
import com.ccq.app.utils.RequestCode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/****************************************
 * 功能说明:  省份选择
 *
 * Author: Created by bayin on 2018/3/29.
 ****************************************/

public class ProvinceActivity extends BaseActivity<ProvincePresenter> implements IProvinceView {

    @BindView(R.id.drawlayout_main)
    RecyclerView mMainRecyclerView;
    @BindView(R.id.drawlayout_right_recyclerview)
    RecyclerView mRightRecyclerView;
    @BindView(R.id.drawlayout_right)
    LinearLayout mDrawlayoutRight;
    @BindView(R.id.province_drawerlayout)
    DrawerLayout mDrawerLayout;

    public static void launch(Activity activity, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(activity, ProvinceActivity.class);
        activity.startActivityForResult(intent, RequestCode.GET_PROVINCE);
    }

    @Override
    protected int inflateContentView() {
        return R.layout.activity_province;
    }

    @Override
    protected ProvincePresenter createPresenter() {
        return new ProvincePresenter(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.getProvince();
    }

    @Override
    protected void initView() {
        super.initView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolBarTitle("选择城市");
        ActionBarDrawerToggle drawerbar = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open_drawer, R.string.close_drawer) {
            //菜单打开
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            // 菜单关闭
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(drawerbar);
    }

    @Override
    public void setProvinceData(List<Province> list) {

    }

    @Override
    public void setCityData(List<Province.CityBean> list) {

    }

}
