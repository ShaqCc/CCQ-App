package com.ccq.app.ui.user;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.Toast;

import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.utils.Constants;
import com.ccq.app.utils.Utils;
import com.ccq.app.weidget.Toasty;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/4/23 0:17
 * 描述：
 * 版本：微信登录页面
 *
 **************************************************/

public class LoginActivity extends BaseActivity {

    private ProgressDialog dialog;

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
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.wechat_login_ing));
        findViewById(R.id.wechat_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.login(getCurrentActivity());
                dialog.show();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finishSelf(Integer eventId) {
        if (eventId.equals(Constants.WX_LOGIN_SUCCESS)) {
            Toasty.success(this,getString(R.string.login_success), Toast.LENGTH_LONG).show();
            if (dialog!=null) {
                dialog.dismiss();
            }
            finish();
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
