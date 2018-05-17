package com.ccq.app.ui.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.Utils;

/**
 * creator by mly ,  2018/5/14
 */
public class SetWechatQRActivity extends BaseActivity {
    @Override
    protected int inflateContentView() {
        return R.layout.activity_wechat_qr_image;
    }

    @Override
    protected void initView() {
        super.initView();
        setToolBarTitle("微信二维码");
        setBackIconVisible(true);
        if(AppCache.getUserBean().isBusiness() || AppCache.getUserBean().isMember()){

        }else{
            showComfirmDialog();
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    private void showComfirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SetWechatQRActivity.this);
        builder.setTitle("操作提示");
        builder.setMessage("此功能需要'会员'或'认证经销商'可用");
        builder.setCancelable(true);
        builder.setPositiveButton("开通会员", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(SetWechatQRActivity.this,OpenVipActivity.class));
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }
}
