package com.ccq.app.ui.reprot;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.entity.Car;
import com.ccq.app.weidget.Toasty;

import butterknife.BindView;
import butterknife.OnClick;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/6/24 17:12
 * 描述：举报
 * 版本：
 *
 **************************************************/

public class ReportCarActivity extends BaseActivity<ReportPresenter> implements IReportView {

    @BindView(R.id.rg_reason)
    RadioGroup rgReason;
    @BindView(R.id.et_reason)
    EditText etReason;

    @OnClick({R.id.reprot_tv_cancel, R.id.report_tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reprot_tv_cancel:
                finish();
                break;
            case R.id.report_tv_submit:
                String content = etReason.getText().toString();
                mPresenter.report(carBean, cateID, typeID, content);
                break;
        }
    }

    private Car carBean;
    private String typeID = "1";
    private String cateID = "0";

    @Override
    protected int inflateContentView() {
        return R.layout.activity_reprot_car_layout;
    }

    /**
     * cateid = 0    铲车（车辆信息）
     * cateid = 1    经销商
     * cateid = 2    求购信息
     * <p>
     * typeid = 1 与话题无关
     * typeid = 2 虚假信息
     * typeid = 3 广告
     * typeid = 4 重复发布
     * else  其它
     */
    @Override
    protected void initView() {
        super.initView();
        carBean = (Car) getIntent().getSerializableExtra("car");
        setToolBarTitle("信息举报");
        setBackIconVisible(true);
        if (carBean == null) {
            finish();
        }
        rgReason.check(R.id.reason_useless);
        rgReason.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.reason_useless:
                        typeID = "1";
                        break;
                    case R.id.reason_fate:
                        typeID = "2";
                        break;
                    case R.id.reason_ads:
                        typeID = "3";
                        break;
                    case R.id.reason_again:
                        typeID = "4";
                        break;
                    case R.id.reason_else:
                        typeID = "111";
                        break;
                }
            }
        });
    }

    @Override
    protected ReportPresenter createPresenter() {
        return new ReportPresenter(this);
    }

    @Override
    public Activity get() {
        return this;
    }

    @Override
    public void reportSuccess() {
        Toasty.success(get(), "感谢您的反馈，我们会尽快处理!", Toast.LENGTH_SHORT).show();
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 500);
    }

    @Override
    public void failure(String msg) {
        if (msg != null)
            Toasty.success(get(), msg, Toast.LENGTH_SHORT).show();
    }
}
