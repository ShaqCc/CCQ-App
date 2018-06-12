package com.ccq.app.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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
import com.ccq.app.ui.user.UserFragment;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.statusbar.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MainActivity extends BaseActivity<MainPresenter> implements MainView {

    @BindView(R.id.main_viewpager)
    AHBottomNavigationViewPager mMainViewpager;

    @BindView(R.id.main_bottom_navigation)
    AHBottomNavigation mNavigation;


    private List<Fragment> mFragments = new ArrayList<>();
    private final String[] fragmentTitles = new String[]{"首页", "发布", "消息", "我的"};
    private MainFragmentAdapter mFragmentAdapter;
    private MessageFragment conversationListFragment;


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
            }
        }
    }



    private void initViewPager() {
        mFragments.add(new HomeFragment());
        mFragments.add(new PublishFragment());
        conversationListFragment = new MessageFragment();
        mFragments.add(conversationListFragment);
        mFragments.add(new UserFragment());
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
                            btSetting.setVisibility(View.VISIBLE);
                            break;
                    }
                    return true;
                }
            }
        });
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
                        //非会员或过期提醒
                        if (AppCache.getUserBean().getVip() == 0) {
                            showComfirmDialog("此功能只有会员可用，是否开通会员", 1);
                        }
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

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
