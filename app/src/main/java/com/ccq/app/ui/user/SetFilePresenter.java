package com.ccq.app.ui.user;

import com.ccq.app.base.BasePresenter;
import com.ccq.app.entity.BaseBean;
import com.ccq.app.utils.ToastUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/4/22 22:46
 * 描述：
 * 版本：
 *
 **************************************************/

public class SetFilePresenter extends BasePresenter<IsetFileView> {
    public SetFilePresenter(IsetFileView view) {
        super(view);
    }

    void sendCheckCode(String phone){
        apiService.sendMobileCode(phone).enqueue(new Callback<BaseBean>() {
            @Override
            public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                mView.sendCodeSuccess(response.body());
            }

            @Override
            public void onFailure(Call<BaseBean> call, Throwable t) {
                ToastUtils.show(mView.get(),t.getMessage());
            }
        });
    }


    /*unionid         微信的unionid
    mobile          手机号码
    code            手机验证码
    headimg         微信获取的头像
    longitude        位置
    latitude          位置
    nickname*/
    public void bindUserInfo(String unionid, String mobile, String code, String headimg, String longtitude,
                             String latitude, String nickName) {
        apiService.bindUserInfo(unionid, mobile, code, headimg, longtitude, latitude, nickName)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        mView.onReceiveBindResult();
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        mView.onReceiveBindResult();
                    }
                });
    }
}
