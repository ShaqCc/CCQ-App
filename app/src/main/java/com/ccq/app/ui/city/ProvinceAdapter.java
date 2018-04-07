package com.ccq.app.ui.city;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccq.app.R;
import com.ccq.app.entity.Province;
import com.ccq.app.utils.OnListItemClickListener;

import java.util.List;

/****************************************
 * 功能说明  
 *
 * Author Created by bayin on 2018/3/29.
 ****************************************/

public class ProvinceAdapter extends RecyclerView.Adapter<BaseHolder> {

    private List<Province.CityBean> dataList;
    private Context mContext;
    private OnListItemClickListener mListener;
    private ViewGroup mRv;

    public ProvinceAdapter(List<Province.CityBean> dataList) {
        this.dataList = dataList;
    }

    public void setCityItemSelected(boolean enable, int index) {
        if (dataList.size() > index) {
            dataList.get(index).setSelected(enable);
            notifyDataSetChanged();
        }
    }

    public void refresh(List<Province.CityBean> dataList) {
        this.dataList = dataList;
    }


    public void setOnItemClickListener(OnListItemClickListener l) {
        this.mListener = l;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        if (null == mRv) {
            mRv = parent;
        }
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_city, parent, false);
        return new BaseHolder(mContext, inflate);
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        holder.setText(R.id.item_city_text, dataList.get(position).getName());
        holder.getView(R.id.item_city_text).setSelected(dataList.get(position).getSelected());
        setListener(position, holder);
    }

    protected void setListener(final int position, final BaseHolder viewHolder) {
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(mRv, v, dataList.get(position), position);
                }
            }
        });


        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListener != null) {
                    int position = getPosition(viewHolder);
                    return mListener.onItemLongClick(mRv, v, dataList.get(position), position);
                }
                return false;
            }
        });
    }

    protected int getPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition();
    }

    @Override
    public int getItemCount() {
        if (dataList != null) return dataList.size();
        return 0;
    }
}
