package com.ccq.app.ui.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ccq.app.R;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.entity.BaseBean;
import com.ccq.app.entity.Car;
import com.ccq.app.entity.SubscribeUser;
import com.ccq.app.http.ApiService;
import com.ccq.app.http.HttpClient;
import com.ccq.app.http.ProgressCallBack;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.ui.publish.BaseMapActivity;
import com.ccq.app.ui.publish.PublishFragment;
import com.ccq.app.ui.user.adapter.MyPublishListAdapter;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.ToastUtils;
import com.ccq.app.utils.ViewState;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/****************************************
 * 功能说明:  我的--首页 页签
 *****************************************/

public class TabHomeFragment extends BaseFragment {


    @BindView(R.id.my_home_recycleview)
    RecyclerView myHomeRecycleview;

    @BindView(R.id.empty_layout)
    View emptyView;
    Unbinder unbinder;
    private ApiService apiService;
    MyPublishListAdapter adapter;

    List<Car> carList = new ArrayList<>();

    Car editCar;

    @Override
    protected int inflateContentView() {
        return R.layout.fragment_my_home;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        emptyView.setVisibility(View.GONE);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(getActivity());
        //设置RecyclerView 布局
        myHomeRecycleview.setLayoutManager(layoutmanager);
        //设置Adapter
        adapter = new MyPublishListAdapter(getActivity(), carList, new MyPublishListAdapter.onItemManageInterface() {
            @Override
            public void onManageClick(int position) {
                editCar = carList.get(position);
                showSelectDialog();
            }

            @Override
            public void onMapShowClick(int position) {
                editCar = carList.get(position);
                getCarLocation();

            }
        });
        myHomeRecycleview.setAdapter(adapter);
    }

    private void getCarLocation() {
        apiService.getCarAddress(String.valueOf(editCar.getId())).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Map<String, Object> map = (Map<String, Object>) response.body();
                if (0.0 == (Double) map.get("code")) {
                    editCar.setDetailAddress((String) map.get("address"));
                    editCar.setLatitude(map.get("latitude").toString());
                    editCar.setLongitude(map.get("longitude").toString());

                    Intent i = new Intent(get(), BaseMapActivity.class);
                    i.putExtra("showMap", true);
                    i.putExtra("car", editCar);
                    get().startActivity(i);

                } else {
                    ToastUtils.show(get(), (String) map.get("message"));
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void showSelectDialog() {
        //type ：  0，在售, 1已售
        String sale = editCar.getType() == 0 ? "已售" : "在售";
        String[] items = {sale, "删除", "编辑", "刷新"};
        if (editCar.getType() == 1) {
            items = new String[]{sale, "删除"};
        }
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(get());
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        showComfirmDialog("确定要修改信息状态[在售／已售]吗？", which);
                        break;
                    case 1:
                        showComfirmDialog("确定要删除此条信息吗？", which);
                        break;
                    case 2:
                        editCar();
                        break;
                    case 3:
                        showComfirmDialog("确定要刷新此信息吗？会员及经销商每天只能刷新5次！", which);
                        break;
                }
            }
        });
        listDialog.show();
    }

    private void showComfirmDialog(String message, final int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(get());
        builder.setTitle("操作提示：");
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (type) {
                    case 0:
                        changeStatu();
                        break;
                    case 1:
                        removeCar();
                        break;
                    case 3:
                        if (AppCache.getUserBean().isBusiness() || AppCache.getUserBean().isMember()) {
                            refreshCar();
                        } else {
                            AlertDialog.Builder alertbuild = new AlertDialog.Builder(get());
                            alertbuild.setTitle("");
                            alertbuild.setCancelable(true);
                            alertbuild.setMessage("你没有权限刷新，开通会员后可刷新");
                            alertbuild.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alertbuild.show();
                        }
                        break;
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }

    /**
     * 修改信息状态
     */
    private void changeStatu() {
        apiService = RetrofitClient.getInstance().getApiService();
        apiService.setCarStatus(AppCache.getUserBean().getUserid(), String.valueOf(editCar.getId())).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Object obj = response.body();
                if (obj != null) {
                    com.google.gson.jpush.JsonObject returnData = new com.google.gson.jpush.JsonParser().parse(obj.toString()).getAsJsonObject();
                    String mesg = returnData.get("message").getAsString();
                    ToastUtils.show(get(), mesg);

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    /**
     * 删除车辆信息
     */
    private void removeCar() {
        apiService = RetrofitClient.getInstance().getApiService();
        apiService.deleteCar(AppCache.getUserBean().getUserid(), String.valueOf(editCar.getId())).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Object obj = response.body();
                if (obj != null) {
                    com.google.gson.jpush.JsonObject returnData = new com.google.gson.jpush.JsonParser().parse(obj.toString()).getAsJsonObject();
                    String mesg = returnData.get("message").getAsString();
                    ToastUtils.show(get(), mesg);

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    /**
     * 刷新车辆信息
     */
    private void refreshCar() {
        apiService = RetrofitClient.getInstance().getApiService();
        apiService.refreshCarInfo(AppCache.getUserBean().getUserid(), String.valueOf(editCar.getId())).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Object obj = response.body();
                if (obj != null) {
                    com.google.gson.jpush.JsonObject returnData = new com.google.gson.jpush.JsonParser().parse(obj.toString()).getAsJsonObject();
                    String mesg = returnData.get("message").getAsString();
                    ToastUtils.show(get(), mesg);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    /**
     * 编辑信息
     */
    private void editCar() {
        Intent i = new Intent(get(), EditPublishActivity.class);
        i.putExtra("bean", editCar);
        get().startActivity(i);
    }


    @Override
    public void initData() {
        if (AppCache.getUserBean() == null) {
            return;
        }
        apiService = RetrofitClient.getInstance().getApiService();
        HashMap carMap = new HashMap<>();
        carMap.put("userid", AppCache.getUserBean().getUserid());
        carMap.put("start", "0");
        carMap.put("limit", "20");

        HttpClient.getInstance(get()).sendRequest(apiService.getUserCarList(carMap), new ProgressCallBack<List<Car>>(get(), true, "正在查询...") {

            @Override
            protected void onSuccess(List<Car> response) {
                super.onSuccess(response);
                if (response != null) {
                    if (carList != null) carList.clear();
                    carList.addAll(response);
                    if (carList.size() < 1) {
                        emptyView.setVisibility(View.VISIBLE);
                        myHomeRecycleview.setVisibility(View.GONE);
//                        setViewState(ViewState.STATE_EMPTY);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        myHomeRecycleview.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                    myHomeRecycleview.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(Throwable t) {
                super.onError(t);
            }
        });

//        apiService.getUserCarList(carMap).enqueue(new Callback() {
//            @Override
//            public void onResponse(Call call, Response response) {
//                if (response!=null && response.body()!=null){
//                    if(carList!=null)carList.clear();
//                    carList.addAll( (List<Car>) response.body());
//                    if(carList.size()<1){
////                        setViewState(ViewState.STATE_EMPTY);
//                    }else{
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call call, Throwable t) {
//
//            }
//        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
