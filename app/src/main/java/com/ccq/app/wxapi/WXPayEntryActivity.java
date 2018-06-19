package com.ccq.app.wxapi;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ccq.app.R;
import com.ccq.app.utils.Constants;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;
    private TextView tvResult;
    private Button btAction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        tvResult = findViewById(R.id.pay_result_tv);
        btAction = findViewById(R.id.btn_back);
        btAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = btAction.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    if (text.equals("返回")) {
                        setResult(66);
                        finish();
                    } else if (text.equals("重新支付")) {
                        setResult(-123);
                        finish();
                    }
                }
            }
        });
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case 0:
                    //支付成功
                    btAction.setText("返回");
                    tvResult.setText(R.string.pay_success);
                    break;
                case -1:
                    btAction.setText("重新支付");
                    tvResult.setText("支付出问题了，请重试");
                    break;
                case -2:
                    btAction.setText("重新支付");
                    tvResult.setText("支付取消");
                    break;

            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_tip);
            builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
            builder.show();
        }
    }
}