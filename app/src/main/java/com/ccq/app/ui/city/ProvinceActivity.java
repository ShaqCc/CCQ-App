package com.ccq.app.ui.city;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.entity.Province;
import com.ccq.app.http.ApiParams;
import com.ccq.app.utils.ItemDivideLine;
import com.ccq.app.utils.OnListItemClickListener;
import com.ccq.app.utils.RequestCode;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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
    @BindView(R.id.tvSideBarHint)
    TextView mTvSideBarHint;
    @BindView(R.id.province_indexbar)
    IndexBar indexBar;
    @BindView(R.id.drawlayout_right_title)
    TextView drawlayoutRightTitle;


    private LinearLayoutManager mMainManager;
    private List<Province.CityBean> mProvinceData = new ArrayList<>();
    private HeaderRecyclerAndFooterWrapperAdapter mHeaderAdapter;
    private ProvinceAdapter mProviceAdapter;
    private SuspensionDecoration mDecor;
    private ActionBarDrawerToggle mDrawerToggle;
    private int lastItemPosition = -1;

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

        mMainManager = new LinearLayoutManager(this);
        mMainRecyclerView.setLayoutManager(mMainManager);
        mProviceAdapter = new ProvinceAdapter(mProvinceData);
        mProviceAdapter.setOnItemClickListener(new OnListItemClickListener<Province.CityBean>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Province.CityBean o, int position) {

                if (lastItemPosition >= 0 && lastItemPosition != position) {
                    //改变之前item的状态
                    int firstVisibleItemPosition = mMainManager.findFirstVisibleItemPosition();
                    int lastVisibleItemPosition = mMainManager.findLastVisibleItemPosition();
                    if (firstVisibleItemPosition <= lastItemPosition
                            && lastItemPosition <= lastVisibleItemPosition) {
                        //在第一个可见与最后一个可见item之间
                        mProviceAdapter.setCityItemSelected(false, lastItemPosition);
                        mMainManager.findViewByPosition(lastItemPosition).setSelected(false);
                    }
                }
                mProviceAdapter.setCityItemSelected(true, position);
                mMainManager.findViewByPosition(position).setSelected(true);
                lastItemPosition = position;
                //请求数据
                drawlayoutRightTitle.setText(o.getName());
                mPresenter.getCitys(String.valueOf(o.getId()));
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Province.CityBean o, int position) {
                return false;
            }
        });
        mHeaderAdapter = new HeaderRecyclerAndFooterWrapperAdapter(mProviceAdapter) {
            @Override
            protected void onBindHeaderHolder(BaseHolder holder, int headerPos, int layoutId, Object o) {
                holder.setText(R.id.item_city_text, (String) o);
            }
        };

//        mHeaderAdapter.setHeaderView(R.layout.item_city, "全国");
        mMainRecyclerView.setAdapter(mHeaderAdapter);
        mDecor = new SuspensionDecoration(this, mProvinceData).setHeaderViewCount(mHeaderAdapter.getHeaderViewCount());
        mMainRecyclerView.addItemDecoration(mDecor);
        mMainRecyclerView.addItemDecoration(new ItemDivideLine(this, ItemDivideLine.HORIZONTAL_LIST, R.drawable.shape_divide_line_1px));
        //使用indexBar
        indexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mMainManager);//设置RecyclerView的LayoutManager


        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open_drawer, R.string.close_drawer) {
            //菜单打开
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                indexBar.setVisibility(View.GONE);
            }

            // 菜单关闭
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                indexBar.setVisibility(View.VISIBLE);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public void setProvinceData(List<Province> list) {
        List<Province.CityBean> provinces = generateData(list);
        indexBar.setmSourceDatas(provinces)//设置数据
//                .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount())//设置HeaderView数量
                .invalidate();

        mProviceAdapter.refresh(provinces);
        mHeaderAdapter.notifyDataSetChanged();
        mDecor.setmDatas(provinces);
    }

    /**
     * 调整接口返回的数据
     *
     * @param list
     * @return
     */
    private List<Province.CityBean> generateData(List<Province> list) {
        mProvinceData = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            List<Province.CityBean> city = list.get(i).getCity();
            mProvinceData.addAll(city);
        }
        return mProvinceData;
    }

    @Override
    public void setCityData(List<Province.CityBean> list) {
        mRightRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        CityNameAdapter adapter = new CityNameAdapter(list);
        adapter.setOnCityClickListener(new CityNameAdapter.OnCityClickListener() {
            @Override
            public void onCityClicked(Province.CityBean city) {
                //set result
                ApiParams.setCityid(String.valueOf(city.getId()));
                finish();
            }
        });
        mRightRecyclerView.setAdapter(adapter);
        mDrawerLayout.openDrawer(Gravity.END, true);
    }

}
