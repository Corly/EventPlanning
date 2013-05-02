package com.smartIntern.GetPOI;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eventplanning.R;

public class POIItemView extends LinearLayout {
	POIItem mItem;
	Context mContext;

	public POIItemView(Context context, POIItem item) {
		super(context);
		mContext = context;
		mItem = item;

		LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.simple_restaurant_list, this, true);

		TextView subject = (TextView) findViewById(R.id.restaurant_name);
		subject.setText(item.getName());
	
	}
}
