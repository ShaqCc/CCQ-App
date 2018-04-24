package com.ccq.app.weidget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
 import android.widget.ListView;

import com.ccq.app.R;

public class ListDialog extends Dialog {

	private View view;
	private ListView dateListView;

	public ListDialog(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Window window = getWindow();
		window.setBackgroundDrawableResource(R.color.white);
		view = inflater.inflate(R.layout.list_dialog, null);
		dateListView = (ListView) view.findViewById(R.id.dateListView);
 	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public ListView getDateListView() {
		return dateListView;
	}


}
