package com.smartintern.saveroute;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SavedRouteAdapter extends BaseAdapter{
	ArrayList<String> mItems;
	OnClickListener mOnItemClick;
	Context mContext;

	public SavedRouteAdapter(Context context, ArrayList<String> items) {
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
		SavedRouteView item;
		final int ind = index;

		item = new SavedRouteView(mContext, mItems.get(index), index);
		
		item.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Bundle data = new Bundle();
				data.putInt("whatRoute", ind);
				Intent i = new Intent(mContext, EnterRoute.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtras(data);
				mContext.startActivity(i);
			}
		});
		
		return item;
	}
}
