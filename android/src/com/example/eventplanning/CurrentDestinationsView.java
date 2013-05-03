package com.example.eventplanning;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CurrentDestinationsView extends LinearLayout{
	String mItem;
	Context mContext;

	public CurrentDestinationsView(Context context, String item, int index) {
		super(context);
		mContext = context;
		mItem = item;
		final int ind = index;

		LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.simple_current_destinations_list, this, true);

		TextView restaurant_name = (TextView) findViewById(R.id.current_dest_poi_name);
		restaurant_name.setText(item);
		
		/*ImageButton delete = (ImageButton) findViewById(R.id.delete_destination);
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GlobalVector.getInstance().routeList.remove(ind);
				ListView mListView = (ListView) findViewById(android.R.id.list);
				CurrentDestinationsAdapter adapter = (CurrentDestinationsAdapter) mListView.getAdapter();
				adapter.
				adapter.notifyDataSetChanged();
				mListView.setEmptyView(findViewById(android.R.id.empty));
				
				ArrayList<String> mItems = new ArrayList<String>();
				
				for (int i=0;i<GlobalVector.getInstance().routeList.size();i++){
					mItems.add(GlobalVector.getInstance().routeList.get(i).name);
				}
				mListView.setAdapter(new CurrentDestinationsAdapter(mContext, mItems));	
			}
		});*/

	}
}
