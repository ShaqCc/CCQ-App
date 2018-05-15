package jiguang.chat.listener;

import android.view.View;

import jiguang.chat.utils.ToastUtil;

/**
 * 聊天界面头像点击事件
 */
public class HeaderIconClick implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        ToastUtil.shortToast(v.getContext(),"头像点击事件");
    }
}
