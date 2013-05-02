package com.smartIntern.GetPOI;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class POIAdapter extends BaseAdapter{
	
	ArrayList<POIItem> mItems;
	OnClickListener mOnItemClick;
	Context mContext;
	//final Intent readMessage;

	public POIAdapter(Context context, ArrayList<POIItem> items) {
		mItems = new ArrayList<POIItem>();
		mContext = context;
		mItems = items;
		//readMessage = new Intent(mContext, ReadMessageReceived.class);
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

	public View getView(int index, View convertView, ViewGroup parent) {
		POIItemView item;

		item = new POIItemView(mContext, mItems.get(index));
		return item;
	}

}
