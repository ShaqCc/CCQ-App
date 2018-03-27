package com.ccq.app.ui;

import android.support.v4.view.ViewPager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.ui.home.HomeFragment;
import com.ccq.app.ui.message.MessageFragment;
import com.ccq.app.ui.publish.PublishFragment;
import com.ccq.app.ui.user.UserFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MainActivity extends BaseActivity<MainPresenter> implements MainView {

    @BindView(R.id.main_viewpager)
    AHBottomNavigationViewPager mMainViewpager;

    @BindView(R.id.main_bottom_navigation)
    AHBottomNavigation mNavigation;

    private List<BaseFragment> mFragments = new ArrayList<>();
    private final String[] fragmentTitles = new String[]{"首页", "发布", "消息", "我的"};
    private MainFragmentAdapter mFragmentAdapter;

    @Override
    protected int inflateContentView() {
        return R.layout.activity_main;
    }


    @Override
    protected void initView() {
        initViewPager();
        initNavigation();
    }

    private void initViewPager() {
        mFragments.add(new HomeFragment());
        mFragments.add(new PublishFragment());
        mFragments.add(new MessageFragment());
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
                    ((BaseFragment) mFragmentAdapter.getItem(position)).refreshUI();
                    return true;
                }
            }
        });
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }


}
