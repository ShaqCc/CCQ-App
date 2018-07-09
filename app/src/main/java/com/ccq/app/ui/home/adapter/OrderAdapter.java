package com.ccq.app.ui.home.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccq.app.R;
import com.ccq.app.http.HomeCarParams;

import java.util.List;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/4/19 23:19
 * 描述：排序适配器
 * 版本：
 *
 **************************************************/

public class OrderAdapter extends BaseFilterAdapter<String> {
    public OrderAdapter(List<String> list) {
        super(list);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_city, viewGroup, false);
        TextView text = view.findViewById(R.id.item_city_text);
        String name = list.get(i);
        text.setText(name);
        String orderName = HomeCarParams.getInstance().getOrderName();
        if (orderName.equals(name)) {
            text.setSelected(true);
        } else {
            text.setSelected(false);
        }
        return view;
    }
}
