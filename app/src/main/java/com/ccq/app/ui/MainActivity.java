package com.ccq.app.ui;

import android.content.Intent;
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
import com.ccq.app.ui.user.UserFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jiguang.chat.activity.fragment.ConversationListFragment;


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
//        StatusBarUtils.setStatusBarColor(this,getResources().getColor(R.color.secondary_text));
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
                            break;
                        case 1:
                            setToolBarVisible(true);
                            setToolBarTitle("发布");
                            break;
                        case 2:
                            setToolBarVisible(true);
                            setToolBarTitle("消息");
                            break;
                        case 3:
                            setToolBarVisible(true);
                            setToolBarTitle("个人中心");
                            break;
                    }
                    return true;
                }
            }
        });
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
