package com.ccq.app.ui.reprot;

import com.ccq.app.base.BasePresenter;
import com.ccq.app.entity.Car;
import com.ccq.app.utils.AppCache;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/6/24 17:44
 * 描述：
 * 版本：
 *
 **************************************************/

public class ReportPresenter extends BasePresenter<IReportView> {

    public ReportPresenter(IReportView view) {
        super(view);
    }

    public void report(Car carBean, String cateId,String typeID,String content) {
        apiService.reportCar(AppCache.getUserBean().getUserid(),String.valueOf(carBean.getId()),cateId,content,typeID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        mView.reportSuccess();
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        mView.failure(t.getMessage());
                    }
                });
    }
}
