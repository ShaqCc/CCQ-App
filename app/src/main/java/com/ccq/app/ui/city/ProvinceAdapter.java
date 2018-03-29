package com.ccq.app.ui.city;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccq.app.R;
import com.ccq.app.entity.Province;

import java.util.List;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2018/3/29.
 ****************************************/

public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.MyHolder> {

    private List<Province> dataList;

    public ProvinceAdapter(List<Province> dataList) {
        this.dataList = dataList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_province_text, null, false);
        return new MyHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.mTvProvince.setText(dataList.get(position).getCity().get(0).getName());
    }

    @Override
    public int getItemCount() {
        if (dataList!=null) return dataList.size();
        return 0;
    }

    class MyHolder extends RecyclerView.ViewHolder{

        private final TextView mTvProvince;

        public MyHolder(View itemView) {
            super(itemView);
            mTvProvince = itemView.findViewById(R.id.item_province_text);
        }
    }
}
