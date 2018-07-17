package com.ccq.app.ui.publish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ccq.app.R;
import com.ccq.app.entity.BrandBean;
import com.ccq.app.entity.BrandModelBean;
import com.ccq.app.entity.TypeBean;
import com.ccq.app.http.ApiService;
import com.ccq.app.http.HomeCarParams;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.ui.home.adapter.TypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ClassName: BrandModelSelectActivity
 * @author: MLY
 * @date: 2018年2月18日
 */
public class BrandModelSelectActivity extends Activity {


    @BindView(R.id.lv_list_left)
    ListView lvListLeft;
    @BindView(R.id.lv_list_right)
    ListView lvListRight;
    public ApiService apiService;
    @BindView(R.id.ic_finish)
    ImageView icFinish;
    @BindView(R.id.toobar_title)
    TextView toobarTitle;

    private List<BrandBean> brandList;
    private List<BrandModelBean> modelList;
    private BrandBean selectBrand;
    private View selectBrandView;

    private DoubleListRightAdapter adapterRight;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_double_layout);
        ButterKnife.bind(this);

        toobarTitle.setText("品牌型号");
        icFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getData();
    }

    private void setAdapterLeft() {
        final DoubleListLeftAdapter adapterLeft = new DoubleListLeftAdapter(this, brandList);
        lvListLeft.setAdapter(adapterLeft);

        lvListLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectBrand = brandList.get(position);
                adapterLeft.setSelect(position);
                adapterLeft.notifyDataSetChanged();

                if (modelList != null) modelList.clear();
                getBrandModelData(selectBrand.getId());
            }
        });

    }

    private void setAdapterRight(final List<TypeBean.NumberListBean> dataList) {

//        adapterRight = new DoubleListRightAdapter(this, modelList);
//        lvListRight.setAdapter(adapterRight);
        TypeAdapter typeAdapter = new TypeAdapter(dataList);
        lvListRight.setAdapter(typeAdapter);

        lvListRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TypeBean.NumberListBean bean = dataList.get(position);
                Intent data = new Intent();
                data.putExtra("brand", selectBrand);
                data.putExtra("model", bean);
                setResult(RESULT_OK, data);
                BrandModelSelectActivity.this.finish();
            }
        });
    }

    private void getData() {
        brandList = new ArrayList<>();
        modelList = new ArrayList<>();

        apiService = RetrofitClient.getInstance().getApiService();
        apiService.getBrandList().enqueue(new Callback<List<BrandBean>>() {
            @Override
            public void onResponse(Call<List<BrandBean>> call, Response<List<BrandBean>> response) {
                if (response != null && response.body() != null) {
                    brandList = response.body();
                    setAdapterLeft();
                }
            }

            @Override
            public void onFailure(Call<List<BrandBean>> call, Throwable t) {

            }
        });


    }

    private void getBrandModelData(String brandid) {
//        apiService.getBrandModelList(brandid).enqueue(new Callback<List<BrandModelBean>>() {
//            @Override
//            public void onResponse(Call<List<BrandModelBean>> call, Response<List<BrandModelBean>> response) {
//                modelList = response.body();
//                setAdapterRight();
//            }
//
//            @Override
//            public void onFailure(Call<List<BrandModelBean>> call, Throwable t) {
//
//            }
//        });


        apiService.getCarTypeList(brandid).enqueue(new Callback<List<TypeBean>>() {
            @Override
            public void onResponse(Call<List<TypeBean>> call, Response<List<TypeBean>> response) {
                List<TypeBean> typeList = response.body();
                if (typeList !=null){
                    List<TypeBean.NumberListBean> dataList = new ArrayList<>();
                    TypeBean.NumberListBean bean;
                    for (int i = 0; i < typeList.size(); i++) {
                        TypeBean typeBean = typeList.get(i);
                        bean = new TypeBean.NumberListBean();
                        bean.setSub(false);
                        bean.setId(typeBean.getId());
                        bean.setName(typeBean.getName());
                        bean.setCode(typeBean.getCode());
                        dataList.add(bean);
                        //遍历子列表
                        List<TypeBean.NumberListBean> numberList = typeBean.getNumberList();
                        if (numberList!=null && numberList.size()>0) {
                            for (int j = 0; j < numberList.size(); j++) {
                                TypeBean.NumberListBean numberBean = numberList.get(j);
                                bean = new TypeBean.NumberListBean();
                                bean.setSub(true);
                                bean.setTid(typeBean.getId());
                                bean.setName(numberBean.getName());
                                bean.setCode(numberBean.getCode());
                                bean.setId(numberBean.getId());
                                bean.setBid(numberBean.getBid());
                                dataList.add(bean);
                            }
                        }
                    }
                    //设置右边的分类列表
                    setAdapterRight(dataList);
                }
            }

            @Override
            public void onFailure(Call<List<TypeBean>> call, Throwable t) {

            }
        });
    }

}
