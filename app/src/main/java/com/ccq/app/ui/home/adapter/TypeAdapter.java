package com.ccq.app.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccq.app.R;
import com.ccq.app.entity.TypeBean;

import java.util.List;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/6/3 16:08
 * 描述：首页，选择型号的适配器
 * 版本：
 *
 **************************************************/

public class TypeAdapter extends BaseFilterAdapter<TypeBean.NumberListBean> {

    public TypeAdapter(List<TypeBean.NumberListBean> list) {
        super(list);
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).isSub()) {
            return 2;//子类目
        } else {
            return 0;//大类目
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (getItemViewType(i)){
            case 0:
                view = inflater.inflate(R.layout.item_city, viewGroup, false);
                TextView tvName = view.findViewById(R.id.item_city_text);
                tvName.setText(list.get(i).getName());
                break;
            case 2:
                view = inflater.inflate(R.layout.item_secondary_text, viewGroup, false);
                TextView tvSecondName = view.findViewById(R.id.item_tv_info);
                tvSecondName.setText(list.get(i).getName());
                break;
        }
        return view;
    }
}
