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
 * Author: Created by bayin on 2018/3/30.
 ****************************************/

public class CityNameAdapter extends RecyclerView.Adapter<CityNameAdapter.CityHolder> {
    List<Province.CityBean> citys;

    public CityNameAdapter(List<Province.CityBean> citys) {
        this.citys = citys;
    }

    @Override
    public CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new CityHolder(inflate);
    }

    @Override
    public void onBindViewHolder(CityHolder holder, final int position) {
        holder.mCityName.setText(citys.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (l != null) l.onCityClicked(citys.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (citys != null) return citys.size();
        return 0;
    }

    static class CityHolder extends RecyclerView.ViewHolder {

        private final TextView mCityName;

        public CityHolder(View itemView) {
            super(itemView);
            mCityName = itemView.findViewById(R.id.item_city_text);
        }
    }

    OnCityClickListener l;

    public void setOnCityClickListener(OnCityClickListener listener) {
        l = listener;
    }

    interface OnCityClickListener {
        void onCityClicked(Province.CityBean city);
    }
}
