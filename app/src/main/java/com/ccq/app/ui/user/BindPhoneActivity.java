package com.ccq.app.ui.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.entity.BaseBean;
import com.ccq.app.entity.UserBean;
import com.ccq.app.entity.WxUserInfo;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.Constants;
import com.ccq.app.utils.JmessageUtils;
import com.ccq.app.utils.MMAlert;
import com.ccq.app.weidget.Toasty;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/4/10 1:02
 * 描述：用户信息绑定页面
 * 版本：
 *
 **************************************************/

public class BindPhoneActivity extends BaseActivity<SetFilePresenter> implements IsetFileView {

    private static WxUserInfo wxUserInfo;
    @BindView(R.id.phone_number)
    EditText phoneNumber;
    @BindView(R.id.send_check_code)
    TextView sendCheckCode;
    @BindView(R.id.et_check_code)
    EditText etCheckCode;
    private MyTimer myTimer;
    private ProgressDialog dialog;

    public static void launch(Activity from, WxUserInfo info) {
        if (from != null) {
            Intent intent = new Intent();
            intent.setClass(from, BindPhoneActivity.class);
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
        dialog = new ProgressDialog(this);
        MMAlert.showAlert(this,"新用户首次登录需要绑定您的手机号","铲车圈提示").show();
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
                    Toasty.info(this,"请填写手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                showProgress("发送中...");
                mPresenter.sendCheckCode(phone);
                break;
            case R.id.bt_bind:

                phone = phoneNumber.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toasty.info(this,"请填写手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                String checkCode = etCheckCode.getText().toString();
                if (TextUtils.isEmpty(checkCode)) {
                    Toasty.info(this,"请填写验证码", Toast.LENGTH_SHORT).show();
                    return;
                }

                showProgress("加载中...");
                mPresenter.bindUserInfo(wxUserInfo.getUnionid(), phone,
                        checkCode, wxUserInfo.getHeadimgurl(), "", "", wxUserInfo.getNickname());

                //检查头像是否下载完毕
//                String avatarPath = Utils.getAvatarPath();
//                File file = new File(avatarPath);
//                if (file.exists() && file.length()>10) {
//
//                } else {
//                    ToastUtils.show(CcqApp.getAppContext(),"头像下载失败");
//                }
                break;
        }
    }

    private void showProgress(String s) {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
        }
        dialog.setMessage(s);
        dialog.show();
    }

    private void dismiss(){
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public Activity get() {
        return this;
    }

    @Override
    public void bindSuccess() {
        dismiss();
        UserBean userBean = AppCache.getUserBean();
        if (userBean != null) {
            JmessageUtils.registerIM(userBean);
        }
        EventBus.getDefault().post(Constants.WX_LOGIN_SUCCESS);
        finish();
    }

    @Override
    public void bindFailure() {
        dismiss();
    }

    @Override
    public void sendCodeSuccess(BaseBean bean) {
        dismiss();
        Toasty.success(this,bean.getMessage(), Toast.LENGTH_SHORT).show();
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
        if (myTimer != null) {
            myTimer.cancel();
            myTimer = null;

        }
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
