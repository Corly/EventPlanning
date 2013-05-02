package com.example.eventplanning;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.smartIntern.GetPOI.POIAdapter;

public class CurrentDestinations extends Activity {
	
	private ListView mListView;
	private Context cnt = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.current_destinations);
		
		mListView = (ListView) findViewById(android.R.id.list);
		mListView.setEmptyView(findViewById(android.R.id.empty));
		
		ArrayList<String> mItems = new ArrayList<String>();
		
		for (int i=0;i<GlobalVector.getInstance().routeList.size();i++){
			mItems.add(GlobalVector.getInstance().routeList.get(i).name);
		}
		mListView.setAdapter(new CurrentDestinationsAdapter(cnt, mItems));
	}
	
}
