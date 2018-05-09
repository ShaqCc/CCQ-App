package com.ccq.app.http;

import android.content.Context;
import android.util.Log;

import com.ccq.app.base.CcqApp;
import com.ccq.app.entity.BaseBean;
import com.ccq.app.utils.ToastUtils;
import com.ccq.app.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/5/6 15:22
 * 描述：
 * 版本：
 *
 **************************************************/

public class HttpClient {

    private static HttpClient mInstance;

    private static Context mContext;

    private ProgressCallBack mCallBack;


    private HttpClient() {
    }

    public static HttpClient getInstance(Context context) {
        if (context == null) throw new NullPointerException("context不能传空！！！");
        mContext = context;
        synchronized (HttpClient.class) {
            if (mInstance == null) {
                mInstance = new HttpClient();
            }
        }
        return mInstance;
    }

    /**
     * 发送网络请求
     *
     * @param call
     * @param callBack
     * @param <T>
     */
    public <T extends BaseBean> void sendRequest(Call<T> call, final ProgressCallBack<T> callBack) {

        if (prepare(call)) return;

        callBack.setRetrofitCall(call);
        callBack.onStart();
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.code() == 0) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onFailure(response.body());
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callBack.onError(t);
            }
        });
    }


    public void download(Call<ResponseBody> call) {
        if (prepare(call)) return;

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(), Utils.getAvatarPath());

                } else {
                    ToastUtils.show(CcqApp.getAppContext(), response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ToastUtils.show(CcqApp.getAppContext(), t.getMessage());
            }});
    }
    private boolean writeResponseBodyToDisk(ResponseBody body,String filePath) {
        try {
            File futureStudioIconFile = new File(filePath);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("download", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }



    private boolean prepare(Call call) {
        if (call == null) {
            ToastUtils.show(CcqApp.getAppContext(), "Retrofit Call 是空！");
            return true;
        }

        if (call.isExecuted()) {
            ToastUtils.show(CcqApp.getAppContext(), "Retrofit Call 已经执行过一次了，再发送就报错了！");
            return true;
        }
        return false;
    }
}
