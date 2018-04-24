package com.ccq.app.ui.publish;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccq.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择列表选择适配器
 * 
 * @author mly
 * 
 */
public class CheckListAdapter extends BaseAdapter {

	LayoutInflater inflater;
	private List<String> list = new ArrayList<String>();
	private Context context;
	private ListItemClickListener listItemClickListener;

	public void setSelectItem(int selectItem) {
		this.selectItem = selectItem;
	}

	private int selectItem  = -1;



	public void setListItemClickListener(ListItemClickListener listItemClickListener) {
		this.listItemClickListener = listItemClickListener;
	}

	public CheckListAdapter(Context context, List<String> list) {
		this.list = list;
		this.context = context;
 	}


	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.item_check_list, null);
		LinearLayout selectLayout = (LinearLayout) convertView.findViewById(R.id.item_select_layout);
		TextView date = (TextView) convertView.findViewById(R.id.date);
		CheckBox checkDate = (CheckBox) convertView.findViewById(R.id.checkBoxDate);

		date.setGravity(Gravity.CENTER_HORIZONTAL);
		date.setText(list.get(position));
		if(selectItem  == position){
			checkDate.setChecked(true);
		}

		selectLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
 			listItemClickListener.onListItemClickListener(position);
			}
		});

		return convertView;
	}

	public interface  ListItemClickListener {
		public void  onListItemClickListener(int position);
	}
}
