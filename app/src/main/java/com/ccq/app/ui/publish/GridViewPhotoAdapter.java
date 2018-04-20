package com.ccq.app.ui.publish;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ccq.app.R;


/** 
 * 图片显示时gridView适配器
 * @author mly
 *
 */
public class GridViewPhotoAdapter extends BaseAdapter {


	private Context context;
	private LayoutInflater inflater;
	private List<String> paths;
    private int size;
	private int width;
	public GridViewPhotoAdapter(Context context, List<String> paths,int width) {
		this.context = context;
		this.paths = paths;
		this.size = paths.size();
		inflater = LayoutInflater.from(context);
		this.width = width;
	}


	@Override
	public int getCount() {
		return  size==3?paths.size():(paths.size()+1);
	}
	@Override
	public Object getItem(int position) {
		return paths.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.gridview_photo_item, null);
			viewHolder.simpleDraweeView = (ImageView) convertView.findViewById(R.id.gridviewimg);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width/2,width/2);
		viewHolder.simpleDraweeView.setLayoutParams(params);
		if (getCount()>size&&position==size)
		{
			viewHolder.simpleDraweeView.setBackgroundResource(R.drawable.add_photo);
		}else {
			viewHolder.simpleDraweeView.setImageURI(Uri.parse("file://" + paths.get(position)));
		}

		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});
		return convertView;
	}

	class ViewHolder {
		ImageView simpleDraweeView;
	}
}
