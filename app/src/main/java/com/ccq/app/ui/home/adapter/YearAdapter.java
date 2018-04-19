package com.ccq.app.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccq.app.R;
import com.ccq.app.entity.BrandBean;
import com.ccq.app.entity.YearLimitBean;

import java.util.List;

/**
 * 作者： 巴银
 * 日期： 2018/4/17.
 * 描述：
 */
public class YearAdapter extends BaseFilterAdapter<YearLimitBean> {

    public YearAdapter(List<YearLimitBean> list) {
        super(list);
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_city, viewGroup, false);
        TextView text = view.findViewById(R.id.item_city_text);
        text.setText(list.get(i).getName());
        return view;
    }
}
