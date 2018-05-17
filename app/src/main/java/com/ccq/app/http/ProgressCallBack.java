package com.ccq.app.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.ccq.app.base.CcqApp;
import com.ccq.app.entity.BaseBean;
import com.ccq.app.utils.ToastUtils;

import retrofit2.Call;


/**************************************************
 *
 * 作者：巴银
 * 时间：2018/5/6 15:05
 * 描述：
 * 版本：
 *
 **************************************************/

public abstract class ProgressCallBack<T> {

    private boolean isShowProgress = true;
    private Context mContext;
    private ProgressDialog mProgress;
    private T response;
    private OnProgressCancle mCancelListener;
    private Call mCall;
    private String showMessage;

    public ProgressCallBack(Context context, boolean isShowProgress) {
        this.mContext = context;
        this.isShowProgress = isShowProgress;
        if (mProgress == null) {
            initProgress();
        }
    }

    public ProgressCallBack(Context context, boolean isShowProgress,String showMessage) {
        this.mContext = context;
        this.isShowProgress = isShowProgress;
        this.showMessage = showMessage;
        if (mProgress == null) {
            initProgress();
        }
    }

    public void onStart() {
        if (isProgress()) {
            showProgress();
        }
    }

    protected void onSuccess(T response) {
        dismissProgress();
    }

    protected void onFailure(T response) {
        if (response != null &&  response instanceof BaseBean) {
            ToastUtils.show(CcqApp.getAppContext(), ((BaseBean) response).getMessage());
        }
        dismissProgress();
    }

    protected void onError(Throwable t){
        dismissProgress();
        ToastUtils.show(CcqApp.getAppContext(),t.getMessage());
    }

    protected boolean isProgress() {
        return isShowProgress;
    }


    private void initProgress() {
        if (mContext != null) {
            mProgress = new ProgressDialog(mContext);
            if(TextUtils.isEmpty(showMessage)) showMessage = "拼命加载中...";
            mProgress.setMessage(showMessage);
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.setCancelable(true);
            mProgress.setCanceledOnTouchOutside(false);
            mProgress.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (mCall != null && mCall.isExecuted()) {
                        mCall.cancel();
                    }
                }
            });
        }
    }

    private void showProgress() {
        if (mProgress == null) initProgress();
        if (!mProgress.isShowing()) {
            mProgress.show();
        }
    }

    private void dismissProgress() {
        if (mProgress != null && mProgress.isShowing()) mProgress.dismiss();
    }

    public void setRetrofitCall(Call call) {
        this.mCall = call;
    }

    public Call getRrtorfitCall() {
        return mCall;
    }

    public void setCancelListener(OnProgressCancle l) {
        this.mCancelListener = l;
    }

    public interface OnProgressCancle {
        void onCancel();
    }
}
