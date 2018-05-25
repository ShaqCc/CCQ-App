package com.ccq.app.ui.user;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.http.HttpClient;
import com.ccq.app.http.ProgressCallBack;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.utils.Constants;
import com.ccq.app.utils.PayCommonUtil;
import com.ccq.app.utils.SystemUtil;
import com.ccq.app.utils.Utils;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.jdom.JDOMException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import jiguang.chat.utils.ToastUtil;
import retrofit2.Call;

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
            "</html>" ;


    private IWXAPI api;


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
        api  = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
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
                setTabSelect(tvMonthTwelve,true);
                setTabSelect(tvMonthSix,false);
                setTabSelect(tvMonthThree,false);
                setTabSelect(tvMonthOne,false);
                break;
            case R.id.tv_month_six:
                getMonthPayInfo("6");
                month = "6";
                setTabSelect(tvMonthTwelve,false);
                setTabSelect(tvMonthSix,true);
                setTabSelect(tvMonthThree,false);
                setTabSelect(tvMonthOne,false);
                break;
            case R.id.tv_month_three:
                getMonthPayInfo("3");
                month = "3";
                setTabSelect(tvMonthTwelve,false);
                setTabSelect(tvMonthSix,false);
                setTabSelect(tvMonthThree,true);
                setTabSelect(tvMonthOne,false);
                break;
            case R.id.tv_month_one:
                getMonthPayInfo("1");
                month = "1";
                setTabSelect(tvMonthTwelve,false);
                setTabSelect(tvMonthSix,false);
                setTabSelect(tvMonthThree,false);
                setTabSelect(tvMonthOne,true);
                break;
            case R.id.btn_pay:
                checkIsSupportPay();
                break;
        }
    }

    private void checkIsSupportPay() {
        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if(!isPaySupported){
            ToastUtil.shortToast(OpenVipActivity.this,"版本过低，不支持微信支付");
        }else{
//            sendWeiXinPay();
            sendPayInfo();
        }
    }

//    private void sendWeiXinPay(){
//        HashMap carMap = new HashMap<>();
//        carMap.put("userid", AppCache.getUserBean().getUserid());
//        carMap.put("openid", Constants.WX_APP_ID);
//        carMap.put("month", month);
//        carMap.put("ip", SystemUtil.getLocalIpAddress(OpenVipActivity.this));
//        carMap.put("typeid", "0");
//
//        HttpClient.getInstance(OpenVipActivity.this).sendRequest(RetrofitClient.getInstance().getApiService().getWeiXinPay(carMap), new ProgressCallBack<Map<String,String>>(OpenVipActivity.this,true,"正在查询...") {
//
//            @Override
//            protected void onSuccess(Map<String, String> response) {
//                super.onSuccess(response);
//                if(response!=null){
//
//                }
//            }
//
//            @Override
//            protected void onError(Throwable t) {
//                super.onError(t);
//            }
//        });
//
//    }

    private void sendPayInfo() {
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";// 微信统一下单接口

        JSONObject signParams = new JSONObject();
        try {
            signParams.put("appid", Constants.WX_APP_ID);
            signParams.put("mch_id", Constants.WX_MCH_ID);
            signParams.put("nonce_str", Utils.getRandomString(30));
            signParams.put("body", "铲车圈-会员充值");
            signParams.put("out_trade_no", String.valueOf(System.currentTimeMillis()));
            signParams.put("total_fee","0.01");
            signParams.put("spbill_create_ip", SystemUtil.getLocalIpAddress(OpenVipActivity.this));
            signParams.put("notify_url",Constants.WX_PAY_CALLBACK_URL);
            signParams.put("trade_type", "APP");

            signParams.put("sign",Utils.createSign(signParams) );

        } catch (JSONException e) {
            e.printStackTrace();
        }

        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        parameterMap.put("appid", Constants.WX_APP_ID);
        parameterMap.put("mch_id", Constants.WX_MCH_ID);
        parameterMap.put("nonce_str", PayCommonUtil.getRandomString(32));
        parameterMap.put("body", "铲车圈-会员充值");
        parameterMap.put("out_trade_no", String.valueOf(System.currentTimeMillis()));
        parameterMap.put("fee_type", "CNY");

//        BigDecimal total = totalAmount.multiply(new BigDecimal(100));
//        java.text.DecimalFormat df=new java.text.DecimalFormat("0");
        parameterMap.put("total_fee", "0.01");
        parameterMap.put("spbill_create_ip", SystemUtil.getLocalIpAddress(OpenVipActivity.this));
        parameterMap.put("notify_url", Constants.WX_PAY_CALLBACK_URL);
        parameterMap.put("trade_type", "APP");
        String sign = PayCommonUtil.createSign("UTF-8", parameterMap);
        parameterMap.put("sign", sign);
        String requestXML = PayCommonUtil.getRequestXml(parameterMap);
        System.out.println(requestXML);

        Toast.makeText(this, "正常调起支付", Toast.LENGTH_SHORT).show();

        new sendYZMTask(requestXML).execute();

//        Map<String, String> map = null;
//        try {
//            map = PayCommonUtil.doXMLParse(result);
//        } catch (JDOMException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"),requestXML);
//        Request request = new Request.Builder()
//                .post(body)
//                .url(url).build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(okhttp3.Call call, IOException e) {
//            }
//
//            @Override
//            public void onResponse(okhttp3.Call call, Response response) throws IOException {
//                Log.e("---------------",response.body().toString());
//                Object obj = response.body();
//            }
//        });

     }


        class sendYZMTask extends AsyncTask<String, Integer, String> {

            private String requestXML;
            public sendYZMTask(String requestXML) {
                this.requestXML = requestXML;
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
                Log.e("---------------",result);
                Map<String, String> map = null;
                try {
                    map = PayCommonUtil.doXMLParse(result);

                } catch (JDOMException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }



    private void setTabSelect(TextView tv,boolean isSelect){
        if(isSelect){
            tv.setTextColor(getResources().getColor(R.color.white));
            tv.setBackground(getResources().getDrawable(R.color.colorPrimary));
        }else{
            tv.setTextColor(getResources().getColor(R.color.primary_text));
            tv.setBackground(getResources().getDrawable(R.color.white));
        }
    }


    private void getMonthPayInfo(String month){

        Call call = RetrofitClient.getInstance().getApiService().getVipMoney(month);
        HttpClient.getInstance(OpenVipActivity.this).sendRequest(call, new ProgressCallBack(OpenVipActivity.this,true,"正在查询...") {
            @Override
            protected void onSuccess(Object response) {
                super.onSuccess(response);
                if (response!=null){
                    Map<String,Object> map = (Map<String, Object>) response;
                    if("0.0".equals(map.get("code").toString())){
                        tvMoneyDiscount.setText(map.get("v1").toString() +"折");
                        tvPayMoney.setText(map.get("message").toString() +"元");
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
