package com.ccq.app.wxapi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.ccq.app.R;
import com.ccq.app.base.CcqApp;
import com.ccq.app.entity.UserBean;
import com.ccq.app.entity.WxLoginResultBean;
import com.ccq.app.entity.WxUserInfo;
import com.ccq.app.http.ApiService;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.ui.MainActivity;
import com.ccq.app.ui.user.BindPhoneActivity;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.Constants;

import com.ccq.app.utils.JmessageUtils;
import com.ccq.app.utils.KLog;

import com.ccq.app.utils.SharedPreferencesUtils;
import com.ccq.app.utils.ToastUtils;
import com.ccq.app.weidget.Toasty;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import jiguang.chat.model.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private ProgressDialog loginProgress;


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
                Toasty.success(this, result, Toast.LENGTH_SHORT).show();
                String code = ((SendAuth.Resp) baseResp).code;
                loginProgress = new ProgressDialog(this);
                loginProgress.setMessage(getString(R.string.wechat_login_ing));
                loginProgress.show();
                getAccessToken(code);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "授权取消";
                Toasty.warning(this, result, Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "授权拒绝";
                Toasty.warning(this, result, Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = "该版本不支持";
                Toasty.error(this, result, Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                result = "授权返回";
                Toasty.info(this, result, Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    //获取微信登录access_token
    String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    //获取微信用户信息
    String wxUserUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";

    private void getAccessToken(String code) {

        accessTokenUrl = String.format(accessTokenUrl, Constants.WX_APP_ID, Constants.WX_APPSECRET, code);
        apiService = RetrofitClient.getInstance().getApiService();
        apiService.getAccessToken(accessTokenUrl)
                .enqueue(new Callback<WxLoginResultBean>() {
                    @Override
                    public void onResponse(@NonNull Call<WxLoginResultBean> call, @NonNull Response<WxLoginResultBean> response) {
                        //根据unid获取账户信息
                        if (response.body() != null) {
                            //获取到微信的Unionid
                            String unionid = response.body().getUnionid();
                            //存储unionid
                            SharedPreferencesUtils.setParam(WXEntryActivity.this, Constants.KEY_UNIONID,
                                    unionid);
                            //access_token
                            final String access_token = response.body().getAccess_token();
                            //openid
                            final String openid = response.body().getOpenid();
                            SharedPreferencesUtils.setParam(WXEntryActivity.this, Constants.KEY_OPEN_ID, openid);
                            //获取系统内的用户信息
                            getUserInfoByUnionid(unionid, access_token, openid);
                        }
                    }

                    @Override
                    public void onFailure(Call<WxLoginResultBean> call, Throwable t) {
                        ToastUtils.show(WXEntryActivity.this, "微信登陆失败，请重试");
                        finish();
                    }
                });
    }

    /**
     * 获取铲车圈系统内用户的信息
     *
     * @param unionid
     * @param access_token
     * @param openid
     */
    private void getUserInfoByUnionid(String unionid, final String access_token, final String openid) {
        apiService.getUserByUniondId(unionid)
                .enqueue(new Callback<UserBean>() {
                    @Override
                    public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                        if (response.body() == null) {
                            //用户首次登陆，获取用户微信里的昵称，头像等
                            getWxUserInfo(wxUserUrl, access_token, openid);
                        } else {
                            SharedPreferencesUtils.setParam(WXEntryActivity.this, Constants.USER_ID, response.body().getUserid());
                            //用户非首次登陆
                            AppCache.setUserBean(response.body());
                            startActivity(new Intent(WXEntryActivity.this, MainActivity.class));
                            EventBus.getDefault().post(Constants.WX_LOGIN_SUCCESS);
                            String jiguang_name = response.body().getJiguang_name();
                            if (TextUtils.isEmpty(jiguang_name)) {
                                //注册登录极光
                                JmessageUtils.registerIM(response.body());
                            } else {
                                //登陆极光im
                                JmessageUtils.loginIM(CcqApp.getAppContext(), jiguang_name, jiguang_name);
                            }
                            //登录完成
                            if (loginProgress != null) {
                                loginProgress.dismiss();
                            }
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserBean> call, Throwable t) {
                        if (loginProgress != null) {
                            loginProgress.dismiss();
                        }
                        Toasty.error(WXEntryActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }

    /**
     * 获取微信用户信息
     *
     * @param wxUserUrl
     * @param access_token
     * @param openid
     */
    private void getWxUserInfo(String wxUserUrl, String access_token, String openid) {

        apiService.getWxUserInfo(String.format(wxUserUrl, access_token, openid))
                .enqueue(new Callback<WxUserInfo>() {
                    @Override
                    public void onResponse(Call<WxUserInfo> call, Response<WxUserInfo> response) {
                        if (loginProgress != null) {
                            loginProgress.dismiss();
                        }
                        //跳转到绑定手机页面
                        BindPhoneActivity.launch(WXEntryActivity.this, response.body());
//                        //下载用户头像
//                        ApiService apiService = RetrofitClient.getInstance().getApiService();
//                        HttpClient.getInstance(WXEntryActivity.this).download(apiService.downloadPic(response.body().getHeadimgurl()));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 200);
                    }

                    @Override
                    public void onFailure(Call<WxUserInfo> call, Throwable t) {
                        Toasty.error(WXEntryActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }
}
