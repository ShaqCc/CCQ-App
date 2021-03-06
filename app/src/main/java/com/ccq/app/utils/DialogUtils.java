package com.ccq.app.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.MenuPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
    private static int popWindowOffset;

    public static void setPopWindowOffset(int offset) {
        popWindowOffset = offset;
    }


    public static void showListPopWindow(Activity activity, View view, final BaseAdapter adapter,
                                         final OnPopItemClickListener lis, PopupWindow.OnDismissListener dismissListener) {
        if (popupWindow == null) initPop(activity);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (lis != null) {
                    lis.OnPopItemClick(adapter.getItem(i), i);
                }
                closePop();
            }
        });
        popupWindow.showAsDropDown(view);
        //背景变灰
//        WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
//        attributes.alpha = 0.3f;
//        activity.getWindow().setAttributes(attributes);
        popupWindow.setOnDismissListener(dismissListener);
    }

    public static void closePop() {
        if (popupWindow != null) popupWindow.dismiss();
    }

    public interface OnPopItemClickListener<T> {
        void OnPopItemClick(T t, int position);
    }

    private static void initPop(Activity activity) {
        popupWindow = new PopupWindow(activity);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#90000000")));
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        int height = SystemUtil.getScreenHeight(activity) - popWindowOffset;
        popupWindow.setHeight(height);
        View contentView = LayoutInflater.from(activity).inflate(R.layout.pop_list_layout, null, false);
        popupWindow.setContentView(contentView);
        listview = contentView.findViewById(R.id.pop_listview);
        contentView.findViewById(R.id.pop_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closePop();
            }
        });
//        ViewGroup.LayoutParams layoutParams = listview.getLayoutParams();
//        listview.setLayoutParams(layoutParams);

        // 按下android回退物理键 PopipWindow消失解决
        contentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    closePop();
                    return true;
                }
                return false;
            }
        });
    }
}
