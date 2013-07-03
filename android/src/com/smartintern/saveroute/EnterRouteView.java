package com.smartIntern.saveroute;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eventplanning.R;

public class EnterRouteView extends LinearLayout{

	String mItem;
	Context mContext;

	public EnterRouteView(Context context, String item, int index) {
		super(context);
		mContext = context;
		mItem = item;

		LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.simple_enter_routes_list, this, true);

		TextView name = (TextView) findViewById(R.id.list_enter_routes_name);
		name.setText(item);

	}
}
