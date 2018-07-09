package com.ccq.app.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccq.app.R;
import com.ccq.app.entity.TypeBean;
import com.ccq.app.http.HomeCarParams;

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
        selectedWord = HomeCarParams.getInstance().getCarTypeName();
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
        String name = list.get(i).getName();
        switch (getItemViewType(i)){
            case 0:
                view = inflater.inflate(R.layout.item_city, viewGroup, false);
                TextView tvName = view.findViewById(R.id.item_city_text);
                if (selectedWord!=null && selectedWord.equals(name)){
                    tvName.setSelected(true);
                }else {
                    tvName.setSelected(false);
                }
                tvName.setText(name);
                break;
            case 2:
                view = inflater.inflate(R.layout.item_secondary_text, viewGroup, false);
                TextView tvSecondName = view.findViewById(R.id.item_tv_info);
                if (selectedWord!=null && selectedWord.equals(name)){
                    tvSecondName.setSelected(true);
                }else {
                    tvSecondName.setSelected(false);
                }
                tvSecondName.setText(name);
                break;
        }
        return view;
    }
}
