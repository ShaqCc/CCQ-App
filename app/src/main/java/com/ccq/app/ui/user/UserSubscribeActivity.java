package com.ccq.app.ui.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ccq.app.R;
import com.ccq.app.base.CcqApp;
import com.ccq.app.entity.SubscribeUser;
import com.ccq.app.entity.UserBean;
import com.ccq.app.http.ApiService;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.ui.MainActivity;
import com.ccq.app.ui.publish.DoubleListRightAdapter;
import com.ccq.app.ui.user.adapter.SubcribeListAdapter;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.ToastUtils;
import com.google.gson.jpush.JsonObject;
import com.google.gson.jpush.JsonParser;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ClassName: BrandModelSelectActivity
 * @author: MLY
 * @date: 2018年2月18日
 */
public class UserSubscribeActivity extends Activity {

    public ApiService apiService;

    @BindView(R.id.toobar_title)
    TextView toobarTitle;
    @BindView(R.id.lv_subscribe_list)
    ListView lvSubscribeList;
    @BindView(R.id.ic_finish)
    ImageView icFinish;

    private SubscribeUser subscribeUser;
    private int type = 0;

    SubcribeListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_listview_layout);
        ButterKnife.bind(this);

        type = getIntent().getIntExtra("type", 0);

        switch (type) {
            case 0:
                toobarTitle.setText("我关注了谁");
                break;
            case 1:
                toobarTitle.setText("谁关注了我");
                break;
        }

        getData();
    }

    private void setAdapter() {
        adapter = new SubcribeListAdapter(this, subscribeUser.getData(), new SubcribeListAdapter.onItemOptInterface() {
            @Override
            public void onSubscribeOptClick(int position) {
                showSelectDialog(position);
            }
        },type);
        lvSubscribeList.setAdapter(adapter);

        lvSubscribeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //TODO  用户资料页面
                List<SubscribeUser.SubUsr> mList = subscribeUser.getData();
                String userid  = mList.get(position).getUserid();
                Intent intent =  new Intent(UserSubscribeActivity.this ,UserInfoActivity.class);
                intent.putExtra("id",userid);
                startActivity(intent);
            }
        });

    }

    private  void showSelectDialog(int selectPosition){
        final SubscribeUser.SubUsr user =  subscribeUser.getData().get(selectPosition);
        String notice = user.getIstemplate()==0 ? "开启提醒" : "关闭提醒" ;
        final String[] items = { notice,"取消关注"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(UserSubscribeActivity.this);
//        listDialog.setTitle("我是一个列表Dialog");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        changeNotice(user,user.getIstemplate()==0? 1:0);
                        break;
                    case 1:
                        changeSubscribe(user);
                        break;
                }
            }
        });
        listDialog.show();
    }

    private void changeNotice(SubscribeUser.SubUsr user , int state){
        apiService = RetrofitClient.getInstance().getApiService();
        HashMap map = new HashMap<>();
        map.put("userid", AppCache.getUserBean().getUserid());
        map.put("subuser", user.getSub_userid());
        map.put("status", state);
        apiService.setUserSubRemind(map).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Object obj = response.body();
                if (obj != null) {
                    JsonObject returnData = new JsonParser().parse(obj.toString()).getAsJsonObject();
                    String mesg = returnData.get("message").getAsString();
                    getData();
                    ToastUtils.show(UserSubscribeActivity.this, mesg);
                 }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

    }

    private void changeSubscribe(SubscribeUser.SubUsr user){
        apiService = RetrofitClient.getInstance().getApiService();
        HashMap map = new HashMap<>();
        map.put("userid", AppCache.getUserBean().getUserid());
        map.put("subuser", user.getSub_userid());
        apiService.setUserSubRemove(map).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Object obj = response.body();
                if (obj != null) {
                    JsonObject returnData = new JsonParser().parse(obj.toString()).getAsJsonObject();
                    String mesg = returnData.get("message").getAsString();
                    getData();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void  getUserSaleInfo(){
        if(subscribeUser!=null){
            List<SubscribeUser.SubUsr> mList = subscribeUser.getData();
            if(mList!=null && mList.size()>0){
                for (SubscribeUser.SubUsr user : mList){
                    getUser(user);
                }
            }

            setAdapter();

        }
    }

    private void getData() {
        if (AppCache.getUserBean()!=null){
            subscribeUser = new SubscribeUser();

            apiService = RetrofitClient.getInstance().getApiService();
            HashMap userMap = new HashMap<>();
            userMap.put("userid", AppCache.getUserBean().getUserid());
//        carMap.put("page", "1");
//        carMap.put("size", "20");

            if (type == 0) {
                apiService.getUserSubscribe(userMap).enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        subscribeUser = (SubscribeUser) response.body();
                        getUserSaleInfo();
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
            } else {
                apiService.getUserFans(userMap).enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        subscribeUser = (SubscribeUser) response.body();
                        getUserSaleInfo();
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
            }
        }

    }

    void getUser(final SubscribeUser.SubUsr user){
        String userid = "";
        switch (type){
            case 0:
                userid = user.getSub_userid();
                break;
            case 1:
                userid = user.getUserid();
                break;
        }

        apiService.getUser(userid).enqueue(new Callback<UserBean>() {
            @Override
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                if (response.body()!=null){
                    UserBean bean = response.body();
                    user.setOnSaleCount(bean.getZaishou_count());
                    user.setSaleOutCount(bean.getYishou_count());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<UserBean> call, Throwable t) {

             }
        });
    }

    @OnClick(R.id.ic_finish)
    public void onViewClicked() {
        finish();
    }
}
