package com.ccq.app.ui.user;

import android.view.View;

import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.utils.Utils;

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
        setToolBarTitle("登陆");
        setBackIconVisible(true);
        findViewById(R.id.wechat_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.login(getCurrentActivity());
            }
        });
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
