package com.ccq.app.ui.user.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ccq.app.R;
import com.ccq.app.entity.BrandBean;
import com.ccq.app.entity.SubscribeUser;

import java.util.List;

public class SubcribeListAdapter extends BaseAdapter {

    private Context context;
    private List<SubscribeUser.SubUsr> mList;

    private onItemOptInterface onClickItemOptListener;

    private int type;

    public SubcribeListAdapter(Context context, List<SubscribeUser.SubUsr> list,onItemOptInterface onClickListener,int type ) {
        this.context = context;
        this.mList = list;
        this.onClickItemOptListener = onClickListener;
        this.type = type;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_subscribe_list, parent, false);
            viewHolder.userNameTv =  convertView.findViewById(R.id.item_subscribe_name);
            viewHolder.userPublishCountTv =   convertView.findViewById(R.id.item_publish_count);
            viewHolder.userHeaderImage = convertView.findViewById(R.id.item_user_header_image);
            viewHolder.userOptImg = convertView.findViewById(R.id.item_opt);
            viewHolder.userNoticeImg = convertView.findViewById(R.id.item_notice);
            viewHolder.userIcon = convertView.findViewById(R.id.item_icon);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SubscribeUser.SubUsr user  = mList.get(position);
        if(user!=null){
            viewHolder.userNameTv.setText(user.getNickname());
            Glide.with(parent.getContext())
                    .load(user.getHeadimgurl())
                    .into(viewHolder.userHeaderImage);

            if(type == 0){
                if(user.getIstemplate()== 1){
                    viewHolder.userNoticeImg.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_notice));
                }else{
                    viewHolder.userNoticeImg.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_notiice_no));
                }
            }else {
                viewHolder.userNoticeImg.setVisibility(View.GONE);
            }

            viewHolder.userPublishCountTv.setText("在售："+user.getOnSaleCount()+"个，已售："+user.getSaleOutCount()+"个");
            viewHolder.userIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickItemOptListener.onSubscribeOptClick(position);
                }
            });
        }

        return convertView;
    }

    class ViewHolder{
        ImageView userHeaderImage;
        ImageView userNoticeImg;
        ImageView userOptImg;
        TextView userNameTv;
        TextView userPublishCountTv;
        RelativeLayout userIcon;
    }

    public interface onItemOptInterface{
        public void onSubscribeOptClick(int position);
    }


}
