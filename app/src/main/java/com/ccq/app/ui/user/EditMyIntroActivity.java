package com.ccq.app.ui.user;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.http.ApiService;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.Constants;
import com.ccq.app.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMyIntroActivity extends Activity {

    @BindView(R.id.ic_finish)
    ImageView icFinish;
    @BindView(R.id.toobar_title)
    TextView toobarTitle;
    @BindView(R.id.tv_intro_content)
    EditText tvIntroContent;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_intro);
        ButterKnife.bind(this);
        toobarTitle.setText("简介编辑");

    }

    @OnClick({R.id.ic_finish, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ic_finish:
                finish();
                break;
            case R.id.btn_submit:
                if(TextUtils.isEmpty(tvIntroContent.getText())){
                    ToastUtils.show(this,"请输入内容");
                    return;
                }

                String content = tvIntroContent.getText().toString();
                if(content.length()<7 || content.length()>500){
                    ToastUtils.show(this,tvIntroContent.getHint().toString());
                    return;
                }

                RetrofitClient.getInstance().getApiService().editUserIntro(AppCache.getUserBean().getUserid(),content).enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Map<String,Object> map = (Map<String, Object>) response.body();
                        if(0.0== (Double)map.get("code")) {
                            finish();
                            EventBus.getDefault().post(Constants.REFRESH_EVENT);
                        }
                        ToastUtils.show(EditMyIntroActivity.this, (String) map.get("message"));
                     }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });

                break;
        }
    }




}
