package com.ccq.app.ui.user;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.http.HttpClient;
import com.ccq.app.http.ProgressCallBack;
import com.ccq.app.http.RetrofitClient;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    String xml = "<html>1、发布信息多号分享，多个微信大号同时分享信息<br>" +
            "2、添加自己二维码水印，从分享号直接聊天<br>" +
            "3、刷新在售车辆，实现车辆置顶 <br>" +
            "4、发布求购车辆信息<br>" +
            "5、抗禁言，抗举报<br>" +
            "6、更多会员特权，物超所值" +
            "</html>" ;

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
                setTabSelect(tvMonthTwelve,true);
                setTabSelect(tvMonthSix,false);
                setTabSelect(tvMonthThree,false);
                setTabSelect(tvMonthOne,false);
                break;
            case R.id.tv_month_six:
                getMonthPayInfo("6");
                setTabSelect(tvMonthTwelve,false);
                setTabSelect(tvMonthSix,true);
                setTabSelect(tvMonthThree,false);
                setTabSelect(tvMonthOne,false);
                break;
            case R.id.tv_month_three:
                getMonthPayInfo("3");
                setTabSelect(tvMonthTwelve,false);
                setTabSelect(tvMonthSix,false);
                setTabSelect(tvMonthThree,true);
                setTabSelect(tvMonthOne,false);
                break;
            case R.id.tv_month_one:
                getMonthPayInfo("1");
                setTabSelect(tvMonthTwelve,false);
                setTabSelect(tvMonthSix,false);
                setTabSelect(tvMonthThree,false);
                setTabSelect(tvMonthOne,true);
                break;
            case R.id.btn_pay:
                break;
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
