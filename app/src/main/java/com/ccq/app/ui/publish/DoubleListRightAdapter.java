package com.ccq.app.ui.publish;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ccq.app.R;
import com.ccq.app.entity.BrandModelBean;

import java.util.ArrayList;
import java.util.List;

public class DoubleListRightAdapter extends BaseAdapter {


    private Context context;
    private List<BrandModelBean> mList ;

    public DoubleListRightAdapter(Context context, List<BrandModelBean> list) {
        this.context = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_double_list_right_adapter, parent, false);
        TextView text = convertView.findViewById(R.id.item_text);
        text.setText(mList.get(position).getName());
        return convertView;
    }
}
