package com.example.eventplanning;

import com.smartIntern.GetPOI.POIItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CurrentDestinationsView extends LinearLayout{
	String mItem;
	Context mContext;

	public CurrentDestinationsView(Context context, String item) {
		super(context);
		mContext = context;
		mItem = item;

		LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.simple_current_destinations_list, this, true);

		TextView restaurant_name = (TextView) findViewById(R.id.current_dest_poi_name);
		restaurant_name.setText(item);

	}
}
