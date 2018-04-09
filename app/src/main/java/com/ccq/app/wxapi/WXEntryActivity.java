package com.ccq.app.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.ccq.app.R;
import com.ccq.app.entity.WxLoginResultBean;
import com.ccq.app.entity.WxUserInfo;
import com.ccq.app.http.ApiService;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.ui.user.BindPhoneActivity;
import com.ccq.app.utils.Constants;
import com.ccq.app.utils.KLog;

import com.ccq.app.utils.SharedPreferencesUtils;
import com.ccq.app.utils.ToastUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/4/7 23:35
 * 描述：
 * 版本：
 *
 **************************************************/

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    /**
     * 微信登录相关
     */
    private IWXAPI api;

    private String tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
    private ApiService apiService;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, true);
        api.registerApp(Constants.WX_APP_ID);
        try {
            boolean result = api.handleIntent(getIntent(), this);
            if (!result) {
                KLog.d("参数不合法，未被SDK处理，退出");
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        api.handleIntent(data, this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        KLog.d("baseReq:" + baseReq.openId);
    }

    @Override
    public void onResp(BaseResp baseResp) {

        KLog.d("baseResp:" + baseResp.errStr + "," + baseResp.openId + "," + baseResp.transaction + "," + baseResp.errCode);
        String result;
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "授权成功";
                String code = ((SendAuth.Resp) baseResp).code;
                getAccessToken(code);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "授权取消";
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "授权拒绝";
                finish();
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = "该版本不支持";
                finish();
                break;
            default:
                result = "授权返回";
                finish();
                break;
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();

    }

    private void getAccessToken(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
        final String wxUserUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
        url = String.format(url, Constants.WX_APP_ID, Constants.WX_APPSECRET, code);
        apiService = RetrofitClient.getInstance().getApiService();
        apiService.getAccessToken(url)
                .enqueue(new Callback<WxLoginResultBean>() {
                    @Override
                    public void onResponse(Call<WxLoginResultBean> call, Response<WxLoginResultBean> response) {
                        //根据unid获取账户信息
                        if (response != null && response.body() != null) {
                            SharedPreferencesUtils.setParam(WXEntryActivity.this, Constants.KEY_UNIONID, response.body().getUnionid());
                            final String access_token = response.body().getAccess_token();
                            final String openid = response.body().getOpenid();
                            apiService.getUserInfo(response.body().getUnionid())//获取系统内的用户信息
                                    .enqueue(new Callback<Object>() {
                                        @Override
                                        public void onResponse(Call<Object> call, Response<Object> response) {
                                            if (response == null || response.body() == null) {
                                                apiService.getWxUserInfo(String.format(wxUserUrl, access_token, openid))
                                                        .enqueue(new Callback<WxUserInfo>() {
                                                            @Override
                                                            public void onResponse(Call<WxUserInfo> call, Response<WxUserInfo> response) {
                                                                //跳转到绑定手机页面
                                                                Bundle bundle = new Bundle();
                                                                bundle.putParcelable("wxuser",response.body());
                                                                BindPhoneActivity.launch(WXEntryActivity.this,bundle);
                                                            }

                                                            @Override
                                                            public void onFailure(Call<WxUserInfo> call, Throwable t) {
                                                                finish();
                                                            }
                                                        });
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Object> call, Throwable t) {
                                            finish();
                                        }
                                    });
                        }

                    }

                    @Override
                    public void onFailure(Call<WxLoginResultBean> call, Throwable t) {
                        ToastUtils.show(WXEntryActivity.this, "微信登陆失败，请重试");
                        finish();
                    }
                });
    }
}
