package com.smartintern.FavoritedPoints;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
		item = new FavoritedPointsView(mContext, mItems.get(index), index);		
		return item;
	}
}
