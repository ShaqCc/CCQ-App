package com.ccq.app.ui.user;

import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.base.CcqApp;
import com.ccq.app.entity.WeixinPayBean;
import com.ccq.app.http.HttpClient;
import com.ccq.app.http.ProgressCallBack;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.Constants;
import com.ccq.app.utils.PayCommonUtil;
import com.ccq.app.utils.SharedPreferencesUtils;
import com.ccq.app.utils.SystemUtil;
import com.ccq.app.utils.Utils;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.jdom.JDOMException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import jiguang.chat.model.Constant;
import jiguang.chat.utils.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * creator by mly ,  2018/5/14
 */
public class OpenVipActivity extends BaseActivity {
    @BindView(R.id.tv_vip_advantage)
    TextView tvVipAdvantage;
    @BindView(R.id.tv_month_twelve)
    TextView tvMonthTwelve;
    @BindView(R.id.tv_month_six)
    TextView tvMonthSix;
    @BindView(R.id.tv_month_three)
    TextView tvMonthThree;
    @BindView(R.id.tv_month_one)
    TextView tvMonthOne;
    @BindView(R.id.tv_pay_money)
    TextView tvPayMoney;
    @BindView(R.id.tv_money_discount)
    TextView tvMoneyDiscount;
    @BindView(R.id.btn_pay)
    Button btnPay;


    String month;

    String xml = "<html>1、发布信息多号分享，多个微信大号同时分享信息<br>" +
            "2、添加自己二维码水印，从分享号直接聊天<br>" +
            "3、刷新在售车辆，实现车辆置顶 <br>" +
            "4、发布求购车辆信息<br>" +
            "5、抗禁言，抗举报<br>" +
            "6、更多会员特权，物超所值" +
            "</html>";


    private IWXAPI api;
    private String total_fee;


    @Override
    protected int inflateContentView() {
        return R.layout.activity_open_vip;
    }

    @Override
    protected void initView() {
        super.initView();
        setToolBarTitle("开通会员");
        setBackIconVisible(true);

        tvVipAdvantage.setText(Html.fromHtml(xml));
        getMonthPayInfo("12");
        month = "12";
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }


    @OnClick({R.id.tv_month_twelve, R.id.tv_month_six, R.id.tv_month_three, R.id.tv_month_one, R.id.btn_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_month_twelve:
                getMonthPayInfo("12");
                month = "12";
                setTabSelect(tvMonthTwelve, true);
                setTabSelect(tvMonthSix, false);
                setTabSelect(tvMonthThree, false);
                setTabSelect(tvMonthOne, false);
                break;
            case R.id.tv_month_six:
                getMonthPayInfo("6");
                month = "6";
                setTabSelect(tvMonthTwelve, false);
                setTabSelect(tvMonthSix, true);
                setTabSelect(tvMonthThree, false);
                setTabSelect(tvMonthOne, false);
                break;
            case R.id.tv_month_three:
                getMonthPayInfo("3");
                month = "3";
                setTabSelect(tvMonthTwelve, false);
                setTabSelect(tvMonthSix, false);
                setTabSelect(tvMonthThree, true);
                setTabSelect(tvMonthOne, false);
                break;
            case R.id.tv_month_one:
                getMonthPayInfo("1");
                month = "1";
                setTabSelect(tvMonthTwelve, false);
                setTabSelect(tvMonthSix, false);
                setTabSelect(tvMonthThree, false);
                setTabSelect(tvMonthOne, true);
                break;
            case R.id.btn_pay:
                checkIsSupportPay();
                break;
        }
    }

    private void checkIsSupportPay() {
        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            ToastUtil.shortToast(OpenVipActivity.this, "版本过低，不支持微信支付");
        } else {
            pay();
//            sendPayInfo();
        }

    }

    private void pay() {
        HashMap<String, String> params = new HashMap<>();
        params.put("month", "1");
        params.put("userid", AppCache.getUserBean().getUserid());
        String openid = (String) SharedPreferencesUtils.getParam(OpenVipActivity.this, Constants.KEY_OPEN_ID, "");
        params.put("openid", openid);
        params.put("ip", SystemUtil.getLocalIpAddress(OpenVipActivity.this));
        params.put("typeid", "0");
        RetrofitClient.getInstance().getApiService().getWeiXinPay(params)
                .enqueue(new Callback<WeixinPayBean>() {
                    @Override
                    public void onResponse(Call<WeixinPayBean> call, Response<WeixinPayBean> response) {
                        WeixinPayBean body = response.body();
                        if (body != null && !TextUtils.isEmpty(body.getSign())) {
                            PayReq request = new PayReq();
                            request.appId = body.getAppId();

                            request.partnerId = body.getPartnerId();

                            request.prepayId = body.getPrepayId();

                            request.packageValue = body.getPackageValue();

                            request.nonceStr = body.getNonceStr();

                            request.timeStamp = body.getTimeStamp();

                            request.sign = body.getSign();

                            CcqApp.getWxApi().sendReq(request);
                        }
                    }

                    @Override
                    public void onFailure(Call<WeixinPayBean> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }


    private void sendPayInfo() {

        JSONObject signParams = new JSONObject();
        try {
            signParams.put("appid", Constants.WX_APP_ID);
            signParams.put("mch_id", Constants.WX_MCH_ID);
            signParams.put("nonce_str", Utils.getRandomString(30));
            signParams.put("body", "铲车圈-会员充值");
            signParams.put("out_trade_no", String.valueOf(System.currentTimeMillis()));
            signParams.put("total_fee", "0.01");
            signParams.put("spbill_create_ip", SystemUtil.getLocalIpAddress(OpenVipActivity.this));
            signParams.put("notify_url", Constants.WX_PAY_CALLBACK_URL);
            signParams.put("trade_type", "APP");

            signParams.put("sign", Utils.createSign(signParams));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        parameterMap.put("appid", Constants.WX_APP_ID);
        parameterMap.put("mch_id", Constants.WX_MCH_ID);
        parameterMap.put("nonce_str", PayCommonUtil.getRandomString(32));
        parameterMap.put("spbill_create_ip", SystemUtil.getLocalIpAddress(this));
        parameterMap.put("out_trade_no", "ccq" + System.currentTimeMillis());
        parameterMap.put("total_fee", "100");
        parameterMap.put("body", "ccq_vip_recharge");
        parameterMap.put("trade_type", "APP");
        parameterMap.put("notify_url", Constants.WX_PAY_CALLBACK_URL);
        String sign = PayCommonUtil.createSign(parameterMap);
        parameterMap.put("sign", sign);
        String requestXML = PayCommonUtil.getRequestXml(parameterMap);
        System.out.println(requestXML);
        new sendYZMTask(requestXML, parameterMap).execute();

    }


    class sendYZMTask extends AsyncTask<String, Integer, String> {

        private String requestXML;
        private Map<String, Object> params;

        public sendYZMTask(String requestXML, SortedMap<String, Object> parameterMap) {
            this.requestXML = requestXML;
            this.params = parameterMap;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String result = PayCommonUtil.httpsRequest(
                        "https://api.mch.weixin.qq.com/pay/unifiedorder", "POST",
                        requestXML);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("---------------", result);
            Map<String, String> map = null;
            try {
                map = PayCommonUtil.doXMLParse(result);

                PayReq request = new PayReq();
                TreeMap<String, Object> treeMap = new TreeMap<>();

                treeMap.put("appid", Constants.WX_APP_ID);
                treeMap.put("partnerid", Constants.WX_MCH_ID);
                treeMap.put("prepayid", map.get("prepay_id"));
                treeMap.put("noncestr", PayCommonUtil.getRandomString(32));
                treeMap.put("package", "Sign=WXPay");
                long l = System.currentTimeMillis();
                treeMap.put("timeStamp", String.valueOf(l / 1000));
                String sign = PayCommonUtil.createSign(treeMap);

                request.appId = Constants.WX_APP_ID;

                request.partnerId = Constants.WX_MCH_ID;

                request.prepayId = map.get("prepay_id");

                request.packageValue = "Sign=WXPay";

                request.nonceStr = String.valueOf(treeMap.get("noncestr"));

                request.timeStamp = String.valueOf(treeMap.get("timeStamp"));

                request.sign = sign;

                CcqApp.getWxApi().sendReq(request);
            } catch (JDOMException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private void setTabSelect(TextView tv, boolean isSelect) {
        if (isSelect) {
            tv.setTextColor(getResources().getColor(R.color.white));
            tv.setBackground(getResources().getDrawable(R.color.colorPrimary));
        } else {
            tv.setTextColor(getResources().getColor(R.color.primary_text));
            tv.setBackground(getResources().getDrawable(R.color.white));
        }
    }


    private void getMonthPayInfo(String month) {

        Call call = RetrofitClient.getInstance().getApiService().getVipMoney(month);
        HttpClient.getInstance(OpenVipActivity.this).sendRequest(call, new ProgressCallBack(OpenVipActivity.this, true, "正在查询...") {
            @Override
            protected void onSuccess(Object response) {
                super.onSuccess(response);
                if (response != null) {
                    Map<String, Object> map = (Map<String, Object>) response;
                    if ("0.0".equals(map.get("code").toString())) {
                        tvMoneyDiscount.setText(map.get("v1").toString() + "折");
                        total_fee = map.get("message").toString();
                        tvPayMoney.setText(total_fee + "元");
                    }
                }
            }

            @Override
            protected void onFailure(Object response) {
                super.onFailure(response);

            }

            @Override
            protected void onError(Throwable t) {
                super.onError(t);

            }
        });


    }

}
