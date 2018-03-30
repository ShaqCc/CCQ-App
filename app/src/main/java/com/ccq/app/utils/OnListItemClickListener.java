package com.ccq.app.utils;

import android.view.View;
import android.view.ViewGroup;

/****************************************
 * 功能说明:  列表item的点击事件接口
 *
 * Author: Created by bayin on 2018/3/30.
 ****************************************/

public interface OnListItemClickListener<T> {
    void onItemClick(ViewGroup parent, View view, T t, int position);

    boolean onItemLongClick(ViewGroup parent, View view, T t, int position);
}
