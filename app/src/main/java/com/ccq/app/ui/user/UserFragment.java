package com.ccq.app.ui.user;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ccq.app.R;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.utils.Constants;
import com.ccq.app.utils.ToastUtils;
import com.ccq.app.utils.Utils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.http.PUT;

/****************************************
 * 功能说明:  我的
 *
 * Author: Created by bayin on 2018/3/26.
 ****************************************/

public class UserFragment extends BaseFragment implements IWXAPIEventHandler {

    @BindView(R.id.user_iv_header)
    ImageView ivHeader;

    @OnClick(R.id.user_iv_header)
    public void login() {
        startActivity(new Intent(get(), LoginActivity.class));
    }

    @Override
    protected int inflateContentView() {
        return R.layout.fragment_user;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    public void initData() {

    }

    @Subscribe
    public void onReceiveLoginSuccess(int eventId) {
        if (eventId == Constants.WX_LOGIN_SUCCESS) {
            ToastUtils.show(get(), "登录成功！设置页面数据！");
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        int result = 0;

        Toast.makeText(getHostActivity(), "baseresp.getType = " + baseResp.getType(), Toast.LENGTH_SHORT).show();

        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_success;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = R.string.errcode_unsupported;
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }

        Toast.makeText(getHostActivity(), result, Toast.LENGTH_LONG).show();
    }
}
