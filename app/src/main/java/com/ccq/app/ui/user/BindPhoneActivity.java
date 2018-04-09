package com.ccq.app.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.base.BasePresenter;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/4/10 1:02
 * 描述：
 * 版本：
 *
 **************************************************/

public class BindPhoneActivity extends BaseActivity {

    public static void launch(Activity from, Bundle bundle) {
        if (from != null) {
            Intent intent = new Intent();
            intent.setClass(from, BindPhoneActivity.class);
            intent.putExtra("userinfo", bundle);
            from.startActivity(intent);
        }
    }

    @Override
    protected int inflateContentView() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void initView() {
        super.initView();
        setToolBarTitle("绑定手机");
        Intent intent = getIntent();
        Bundle extras = intent.getBundleExtra("userinfo");
        if (extras != null) {

        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
