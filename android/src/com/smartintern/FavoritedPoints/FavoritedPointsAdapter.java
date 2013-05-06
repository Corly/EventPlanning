package com.smartintern.FavoritedPoints;

import java.util.ArrayList;

import com.example.eventplanning.MainActivity;
import com.smartintern.saveroute.EnterRoute;
import com.smartintern.saveroute.SavedRouteView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;

public class FavoritedPointsAdapter extends BaseAdapter{
	ArrayList<String> mItems;
	OnClickListener mOnItemClick;
	Context mContext;

	public FavoritedPointsAdapter(Context context, ArrayList<String> items) {
		mItems = new ArrayList<String>();
		mContext = context;
		mItems = items;
	}

	public int getCount() {
		return mItems.size();
	}

	public Object getItem(int index) {
		return mItems.get(index);
	}

	public long getItemId(int index) {
		return index;
	}
	
	public void remove(int index){
		mItems.remove(mItems.get(index));
	}

	public View getView(int index, View convertView, ViewGroup parent) {
		FavoritedPointsView item;
		final int ind = index;

		item = new FavoritedPointsView(mContext, mItems.get(index), index);
		item.setLongClickable(true);
		
		item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("title", "Route from "+ "\"" + FavoritedPointsVector.getInstance().favPoints.get(ind).name+"\"" + " point");
				i.putExtra("lat", FavoritedPointsVector.getInstance().favPoints.get(ind).lat);
				i.putExtra("lng", FavoritedPointsVector.getInstance().favPoints.get(ind).lng);
				mContext.startActivity(i);	
			}
		});
		
		return item;
	}
}
