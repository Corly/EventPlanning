package com.example.eventplanning;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

public class CurrentDestinations extends Activity {
	
	private ListView mListView;
	private Context cnt = this;
	static final int DELTA = 50;
	enum Direction {LEFT, RIGHT;};
	float historicX = Float.NaN, historicY = Float.NaN;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.current_destinations);
		
		IntelGeolocation.MakeToast("Hint: Long click to delete a destination", cnt);
		
		mListView = (ListView) findViewById(android.R.id.list);
		mListView.setEmptyView(findViewById(android.R.id.empty));
		
		ArrayList<String> mItems = new ArrayList<String>();
		
		for (int i=0;i<GlobalVector.getInstance().routeList.size();i++){
			mItems.add(GlobalVector.getInstance().routeList.get(i).name);
		}
		CurrentDestinationsAdapter adapter = new CurrentDestinationsAdapter(cnt, mItems);
		mListView.setAdapter(adapter);
		
		final CurrentDestinationsAdapter adap = adapter;
		
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, 
					int pos, long id){
				
				final int position = pos;
				AlertDialog.Builder dialog = new AlertDialog.Builder(cnt);
				final AlertDialog.Builder auxDialog = dialog; 
				dialog.setMessage("Are you sure you want to delete " + adap.mItems.get(position) + " from the current route?");
				dialog.setNegativeButton("Delete", new OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{					
						adap.mItems.remove(position);
						adap.notifyDataSetChanged();
						GlobalVector.getInstance().routeList.remove(position);
						IntelGeolocation.MakeToast("Deleted!", cnt);
					}				
				});
				dialog.setPositiveButton("Cancel", new OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{					
						AlertDialog d = auxDialog.show();
						d.dismiss();
					}				
				});
				dialog.show();
				return true;
			}
		});
		
		Button clear = (Button)findViewById(R.id.clear_all_destinations);
		Button showR = (Button)findViewById(R.id.show_route);
		
		clear.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(CurrentDestinations.this);
				final AlertDialog.Builder auxDialog = dialog; 
				dialog.setMessage("Are you sure you want to delete all destination?");
				dialog.setNegativeButton("Delete", new OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{					
						while ( !GlobalVector.getInstance().routeList.isEmpty())
							GlobalVector.getInstance().routeList.remove(0);
						IntelGeolocation.MakeToast("All deleted!", cnt);
						finish();
					}				
				});
				dialog.setPositiveButton("Cancel", new OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{					
						AlertDialog d = auxDialog.show();
						d.dismiss();
					}				
				});
				dialog.show();
				
			}
		});
		
		showR.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), RouteActivity.class);
        		startActivity(i);
			}
		});
	}
	
}
