package com.ccq.app.ui.message;

import android.os.Bundle;
import android.view.View;

import com.ccq.app.R;

import jiguang.chat.activity.ChatActivity;
import jiguang.chat.utils.ToastUtil;

public class SingleChatActivity extends ChatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChatAdapter.setItemClickViewListener(ItenViewClick);
    }

    View.OnClickListener ItenViewClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.jmui_avatar_iv:
                    ToastUtil.shortToast(SingleChatActivity.this, "头像点击事件");
                    break;
            }
        }
    };

}
