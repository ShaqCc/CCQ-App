package com.ccq.app.utils;

import android.app.Activity;
import android.support.v7.widget.MenuPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.ccq.app.R;

/**
 * 作者： 巴银
 * 日期： 2018/4/17.
 * 描述：
 */
public class DialogUtils {

    private static PopupWindow popupWindow;
    private static ListView listview;

    public static void showListPopWindow(Activity activity, View view, final BaseAdapter adapter,
                                         final OnPopItemClickListener lis){
        if (popupWindow==null) initPop(activity);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (lis!=null){
                    lis.OnPopItemClick(adapter.getItem(i));
                }
                popupWindow.dismiss();
            }
        });
        popupWindow.showAsDropDown(view);
    }

    public interface OnPopItemClickListener<T>{
        void OnPopItemClick(T t);
    }

    private static void initPop(Activity activity) {
        popupWindow = new PopupWindow(activity);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(SystemUtil.getScreenHeight(activity)/2);
        View contentView = LayoutInflater.from(activity).inflate(R.layout.pop_list_layout, null, false);
        popupWindow.setContentView(contentView);
        listview = contentView.findViewById(R.id.pop_listview);
    }
}
