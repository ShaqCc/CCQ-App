package com.ccq.app.ui.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ccq.app.R;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.base.BasePresenter;

import jiguang.chat.activity.fragment.ConversationListFragment;

/****************************************
 * 功能说明:  消息
 *
 * Author: Created by bayin on 2018/3/26.
 ****************************************/

public class MessageFragment extends ConversationListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View toolbra = mRootView.findViewById(R.id.rl_toolbra);
        toolbra.setVisibility(View.GONE);
        mConvListView.dismissSearchView();
        mConvListController.setUserNamePrefix("chanche@");
    }
}
