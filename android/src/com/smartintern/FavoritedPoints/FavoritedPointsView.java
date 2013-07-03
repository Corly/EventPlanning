package com.smartIntern.FavoritedPoints;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eventplanning.R;

public class FavoritedPointsView extends LinearLayout {
	String mItem;
	Context mContext;

	public FavoritedPointsView(Context context, String item, int index) {
		super(context);
		mContext = context;
		mItem = item;

		LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.simple_saved_routes_list, this, true);

		TextView name = (TextView) findViewById(R.id.list_saved_routes_name);
		name.setText(item);

	}
}
