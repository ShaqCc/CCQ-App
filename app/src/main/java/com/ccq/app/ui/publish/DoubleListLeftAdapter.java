package com.ccq.app.ui.publish;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ccq.app.R;
import com.ccq.app.entity.BrandBean;

import java.util.List;

public class DoubleListLeftAdapter extends BaseAdapter {


    private Context context;
    private List<BrandBean> mList;
    private int select = -1;

    public void setSelect(int select) {
        this.select = select;
    }

    public DoubleListLeftAdapter(Context context, List<BrandBean> list) {
        this.context = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return  mList==null?0:mList.size();
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
                .inflate(R.layout.activity_double_list_left_adapter, parent, false);
        TextView text = convertView.findViewById(R.id.item_text);
        text.setText(mList.get(position).getName());

        if(select == position){
            text.setTextColor(context.getResources().getColor(R.color.red));

        }
        return convertView;
    }
}
