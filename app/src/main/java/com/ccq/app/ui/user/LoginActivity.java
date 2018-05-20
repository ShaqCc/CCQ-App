package com.ccq.app.ui.user;

import android.os.Handler;
import android.view.View;

import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.utils.Constants;
import com.ccq.app.utils.RequestCode;
import com.ccq.app.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jiguang.chat.entity.Event;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/4/23 0:17
 * 描述：
 * 版本：
 *
 **************************************************/

public class LoginActivity extends BaseActivity {
    @Override
    protected int inflateContentView() {
        return R.layout.login_activity;
    }

    @Override
    protected void initView() {
        super.initView();
        EventBus.getDefault().register(this);
        setToolBarTitle("登陆");
        setBackIconVisible(true);
        findViewById(R.id.wechat_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.login(getCurrentActivity());
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        finish();
//                    }
//                },500);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finishSelf(Integer eventId) {
        if (eventId.equals(Constants.WX_LOGIN_SUCCESS))
            finish();
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
