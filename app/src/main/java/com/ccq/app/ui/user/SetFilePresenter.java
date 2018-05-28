package com.ccq.app.ui.user;

import android.widget.Toast;

import com.ccq.app.base.BasePresenter;
import com.ccq.app.base.CcqApp;
import com.ccq.app.entity.BaseBean;
import com.ccq.app.entity.BindResultBaen;
import com.ccq.app.entity.UserBean;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.ToastUtils;
import com.ccq.app.weidget.Toasty;

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
                .enqueue(new Callback<BindResultBaen>() {
                    @Override
                    public void onResponse(Call<BindResultBaen> call, Response<BindResultBaen> response) {
                        if (response.body()!=null){
                            if (response.body().getCode() == 0){
                                ToastUtils.show(CcqApp.getAppContext(),"绑定成功！");
                                //获取user信息
                                getUser(response.body().getUserid());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BindResultBaen> call, Throwable t) {
                        ToastUtils.show(CcqApp.getAppContext(),t.getMessage());
                        mView.bindFailure();
                    }
                });
    }

    void getUser(String userid){
        apiService.getUser(userid).enqueue(new Callback<UserBean>() {
            @Override
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                if (response.body()!=null){
                    AppCache.setUserBean(response.body());
                    Toasty.success(mView.get(),"恭喜！绑定成功", Toast.LENGTH_SHORT).show();
                    mView.bindSuccess();
                }
            }

            @Override
            public void onFailure(Call<UserBean> call, Throwable t) {
                Toasty.error(mView.get(),t.getMessage()+"\n抱歉！出问题了，请稍后再试", Toast.LENGTH_SHORT).show();
                mView.bindFailure();
            }
        });
    }
}
