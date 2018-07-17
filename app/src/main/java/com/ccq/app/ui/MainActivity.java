package com.ccq.app.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.ui.home.HomeFragment;
import com.ccq.app.ui.message.MessageFragment;
import com.ccq.app.ui.publish.PublishFragment;
import com.ccq.app.ui.user.OpenVipActivity;
import com.ccq.app.ui.user.SetWechatQRActivity;
import com.ccq.app.ui.user.TabHomeFragment;
import com.ccq.app.ui.user.UserFragment;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.Constants;
import com.ccq.app.utils.SharedPreferencesUtils;
import com.ccq.app.utils.statusbar.StatusBarUtils;
import com.ccq.app.weidget.Toasty;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.ccq.app.base.CcqApp.getAppContext;


public class MainActivity extends BaseActivity<MainPresenter> implements MainView {

    @BindView(R.id.main_viewpager)
    AHBottomNavigationViewPager mMainViewpager;

    @BindView(R.id.main_bottom_navigation)
    AHBottomNavigation mNavigation;


    private List<Fragment> mFragments = new ArrayList<>();
    private final String[] fragmentTitles = new String[]{"首页", "发布", "消息", "我的"};
    private MainFragmentAdapter mFragmentAdapter;
    private MessageFragment conversationListFragment;
    private Fragment currentFragment;
    private TencentLocationManager locationManager;
    private TencentLocationListener listener;
    private TencentLocationRequest request;


    @Override
    protected int inflateContentView() {
        return R.layout.activity_main_ccp;
    }


    @Override
    protected void initView() {
        initViewPager();
        initNavigation();
        getToolBar().setVisibility(View.GONE);
        StatusBarUtils.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary));
        //定位服务开启
        prepareLocationService();
    }

    private void prepareLocationService() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            if (checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, 0);
            } else {
                //开启定位
                initLocationListener();
            }
        }else {
            initLocationListener();
        }
    }

    private void initLocationListener() {
        listener = new TencentLocationListener() {
            @Override
            public void onLocationChanged(TencentLocation tencentLocation, int error, String s) {
                if (TencentLocation.ERROR_OK == error) {
                    AppCache.mLocation = tencentLocation;
                    Toasty.success(getApplication(),AppCache.mLocation.getAddress()).show();
                } else {
                    // 定位失败
                    AppCache.mLocation = null;
                    Toasty.success(getApplication(),error+"  :"+s).show();
                }
                locationManager.removeUpdates(listener);
            }

            @Override
            public void onStatusUpdate(String s, int i, String s1) {

            }
        };

        request = TencentLocationRequest.create();
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_POI);
        locationManager = TencentLocationManager.getInstance(getApplicationContext());
        locationManager.requestLocationUpdates(request, listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults.length >= 1) {
            //开启定位
            initLocationListener();
        }
    }


    private void initViewPager() {
        mFragments.add(new HomeFragment());
        mFragments.add(new PublishFragment());
        conversationListFragment = new MessageFragment();
        mFragments.add(conversationListFragment);
        //个人中心
        UserFragment user = new UserFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(TabHomeFragment.KEY_IS_SELF, true);
        user.setArguments(bundle);
        mFragments.add(user);
        mFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager(), mFragments);
        mMainViewpager.setAdapter(mFragmentAdapter);
        mMainViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mNavigation.setCurrentItem(position, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化底部导航栏
     */
    private void initNavigation() {
        mNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        AHBottomNavigationAdapter navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.navigation);
        navigationAdapter.setupWithBottomNavigation(mNavigation);
        mNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                currentFragment = mFragments.get(position);
                if (position == mMainViewpager.getCurrentItem())
                    return false;
                else {
                    mMainViewpager.setCurrentItem(position, false);
                    if (mFragmentAdapter.getItem(position) instanceof BaseFragment) {
                        ((BaseFragment) mFragmentAdapter.getItem(position)).refreshUI();
                    }

                    switch (position) {
                        case 0:
                            setToolBarVisible(false);
                            btSetting.setVisibility(View.GONE);
                            break;
                        case 1:
                            setToolBarVisible(true);
                            setToolBarTitle("发布");
                            if (mFragments.get(position) instanceof PublishFragment) {
                                ((PublishFragment) mFragments.get(position)).initVars();
                            }
                            btSetting.setVisibility(View.GONE);
                            break;
                        case 2:
                            setToolBarVisible(true);
                            setToolBarTitle("消息");
                            btSetting.setVisibility(View.GONE);
                            break;
                        case 3:
                            setToolBarVisible(true);
                            setToolBarTitle("个人中心");
                            btSetting.setVisibility(View.GONE);
                            break;
                    }
                    return true;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        currentFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void settting() {
        showUserSettingDialog();
    }

    private void showUserSettingDialog() {
        String[] items = {"二维码水印", "修改图片", "切换账号"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(this);
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent i = new Intent(MainActivity.this, SetWechatQRActivity.class);
                        startActivity(i);
                        break;
                    case 1:
                        //非会员或过期提醒
                        if (AppCache.getUserBean().getVip() == 0) {
                            showComfirmDialog("此功能只有会员可用，是否开通会员", 0);
                        }
                        break;
                    case 2:
                        //退出登录
                        SharedPreferencesUtils.setParam(getCurrentActivity(), Constants.USER_ID, "");
                        AppCache.setUserBean(null);
                        ((BaseFragment) mFragments.get(3)).initData();
                        break;
                }
            }
        });
        listDialog.show();
    }

    private void showComfirmDialog(String message, final int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("错误提示");
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainActivity.this, OpenVipActivity.class));
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (conversationListFragment != null)
            conversationListFragment.sortConvList();
    }

    public void setCurrentTab(int index) {
        if (index >= 0 && index < 4) {
            mNavigation.setCurrentItem(index, true);
        }
    }


    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public Activity get() {
        return this;
    }

}
