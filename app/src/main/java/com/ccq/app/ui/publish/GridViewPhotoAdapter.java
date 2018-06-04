package com.ccq.app.ui.publish;

import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
	private boolean isFromLocal = true;// true 本地 | false 网络

	public void setDeleteItemClickListener(DeleteItemClickListener deleteItemClickListener) {
		this.deleteItemClickListener = deleteItemClickListener;
	}

	private DeleteItemClickListener  deleteItemClickListener;

	public GridViewPhotoAdapter(Context context, List<String> paths,int width,boolean isFromLocal) {
		this.context = context;
		this.paths = paths;
		this.size = paths.size();
		inflater = LayoutInflater.from(context);
		this.width = width;
		this.isFromLocal = isFromLocal;
	}


	@Override
	public int getCount() {
		return   (paths.size()+2);
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
			viewHolder.simpleDraweeView = convertView.findViewById(R.id.gridviewimg);
			viewHolder.deleteImageView = convertView.findViewById(R.id.delete_iv);
			viewHolder.packageTextView = convertView.findViewById(R.id.pakage_tv);
			viewHolder.leftTipTextView = convertView.findViewById(R.id.left_tips_textview);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.simpleDraweeView.getLayoutParams();
		int paramWidth = (int)(width * 1.4);
		params.width = paramWidth;
		params.height = paramWidth;
		viewHolder.simpleDraweeView.setLayoutParams(params);
		if (position==size) {
			viewHolder.simpleDraweeView.setBackgroundResource(R.drawable.icon_add_photo);
			viewHolder.deleteImageView.setVisibility(View.GONE);
			viewHolder.packageTextView.setVisibility(View.GONE);
		}else if(position == size +1){
			viewHolder.simpleDraweeView.setBackgroundResource(R.drawable.icon_add_video);
			viewHolder.deleteImageView.setVisibility(View.GONE);
			viewHolder.packageTextView.setVisibility(View.GONE);
		} else {
			String pathtemp = paths.get(position).toLowerCase();

			if(isFromLocal){
				if(pathtemp.contains(".jpg")||pathtemp.contains(".jpeg")||pathtemp.contains(".png")||pathtemp.contains(".bmp")){
					viewHolder.simpleDraweeView.setImageURI(Uri.parse("file://" + paths.get(position)));
				}else{
					MediaMetadataRetriever media = new MediaMetadataRetriever();
					media.setDataSource(paths.get(position));
					Bitmap bitmap = media.getFrameAtTime();
					viewHolder.simpleDraweeView.setImageBitmap(bitmap);
				}
			}else{
				if(pathtemp.contains(".jpg")||pathtemp.contains(".jpeg")||pathtemp.contains(".png")||pathtemp.contains(".bmp")){
					Glide.with(context)
							.load(paths.get(position) + "!300auto")
							.into(viewHolder.simpleDraweeView);
				}else{
//					MediaMetadataRetriever media = new MediaMetadataRetriever();
//					media.setDataSource(paths.get(position));
//					Bitmap bitmap = media.getFrameAtTime();
//					viewHolder.simpleDraweeView.setImageBitmap(bitmap);
				}
			}

			if(position == 0){
				viewHolder.leftTipTextView.setVisibility(View.VISIBLE);
				viewHolder.packageTextView.setBackground(context.getResources().getDrawable(R.color.colorPrimary));
			}else{
				viewHolder.leftTipTextView.setVisibility(View.GONE);
				viewHolder.packageTextView.setBackground(context.getResources().getDrawable(R.color.lightransparent));
			}

		}
		viewHolder.deleteImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteItemClickListener.onListItemClickListener(position);
			}
		});
		return convertView;
	}

	class ViewHolder {
		ImageView simpleDraweeView;
		ImageView deleteImageView;
		TextView packageTextView;
		TextView leftTipTextView;
	}

	public interface  DeleteItemClickListener {
		 void  onListItemClickListener(int position);
	}

}