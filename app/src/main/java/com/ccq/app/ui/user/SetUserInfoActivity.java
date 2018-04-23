package com.ccq.app.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.entity.BaseBean;
import com.ccq.app.entity.WxUserInfo;
import com.ccq.app.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/4/10 1:02
 * 描述：用户信息绑定页面
 * 版本：
 *
 **************************************************/

public class SetUserInfoActivity extends BaseActivity<SetFilePresenter> implements IsetFileView {

    private static WxUserInfo wxUserInfo;
    @BindView(R.id.phone_number)
    EditText phoneNumber;
    @BindView(R.id.send_check_code)
    TextView sendCheckCode;
    @BindView(R.id.et_check_code)
    EditText etCheckCode;
    private MyTimer myTimer;

    public static void launch(Activity from, WxUserInfo info) {
        if (from != null) {
            Intent intent = new Intent();
            intent.setClass(from, SetUserInfoActivity.class);
            wxUserInfo = info;
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
    }

    @Override
    protected SetFilePresenter createPresenter() {
        return new SetFilePresenter(this);
    }


    @OnClick({R.id.send_check_code, R.id.bt_bind})
    public void onViewClicked(View view) {
        String phone;
        switch (view.getId()) {
            case R.id.send_check_code:
                phone = phoneNumber.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.show(this, "请填写手机号！");
                    return;
                }
                mPresenter.sendCheckCode(phone);
                break;
            case R.id.bt_bind:

                phone = phoneNumber.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.show(this, "请填写手机号！");
                    return;
                }
                String checkCode = etCheckCode.getText().toString();
                if (TextUtils.isEmpty(checkCode)) {
                    ToastUtils.show(this, "请填写验证码！");
                    return;
                }

                mPresenter.bindUserInfo(wxUserInfo.getUnionid(), phone,
                        checkCode, wxUserInfo.getHeadimgurl(), "", "", wxUserInfo.getNickname());

                break;
        }
    }

    @Override
    public Activity get() {
        return getCurrentActivity();
    }

    @Override
    public void onReceiveBindResult() {

    }

    @Override
    public void sendCodeSuccess(BaseBean bean) {
        ToastUtils.show(get(), bean.getMessage());
        if (bean.getCode() == 0) {
            if (myTimer != null) {
                myTimer.cancel();
            }
            myTimer = new MyTimer(60 * 1000, 1000);
            myTimer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myTimer.cancel();
        myTimer = null;
    }

    class MyTimer extends CountDownTimer {

        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            sendCheckCode.setText(String.valueOf(l / 1000) + "秒后重新获取");
            sendCheckCode.setTextColor(Color.parseColor("#999999"));
        }

        @Override
        public void onFinish() {
            sendCheckCode.setText("获取验证码");
            sendCheckCode.setTextColor(get().getResources().getColor(R.color.colorPrimary));
        }
    }
}
