package com.ccq.app.ui.home.adapter;

import android.widget.BaseAdapter;

import java.util.List;

/**
 * 作者： 巴银
 * 日期： 2018/4/17.
 * 描述：
 */
public abstract class BaseFilterAdapter<T> extends BaseAdapter{
    protected List<T> list;
    protected String selectedWord = "";
    public BaseFilterAdapter(List<T> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list!=null) return list.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

}
