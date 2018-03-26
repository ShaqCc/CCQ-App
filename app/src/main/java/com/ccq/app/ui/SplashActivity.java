package com.ccq.app.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.base.BasePresenter;


/**************************************************
 *
 * 作者：巴银
 * 时间：2018/3/27 0:31
 * 描述：启动页面
 * 版本：1.0
 *
 **************************************************/

public class SplashActivity extends BaseActivity {

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected int inflateContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initData() {
        super.initData();
        mHandler.sendEmptyMessageDelayed(0, 2000);
    }
}
