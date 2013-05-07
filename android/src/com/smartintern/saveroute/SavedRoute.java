package com.smartintern.saveroute;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventplanning.R;

public class SavedRoute extends Activity{
	
	private ListView mListView;
	private Context cnt = this;
	private Button clearAll;
	
	OnTouchListener touchListener = new OnTouchListener()
	{
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1)
		{
			switch (arg1.getAction())
			{
				case MotionEvent.ACTION_DOWN:
				{
					arg0.setBackgroundResource(R.drawable.clearbuttonclick);
					return false;
				}
				case MotionEvent.ACTION_UP:
				{
					arg0.setBackgroundResource(R.drawable.clearbutton);
					return false;
				}
			}
			return false;
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.saved_routes);
		clearAll = (Button)findViewById(R.id.clear_all_routes);
		clearAll.setOnTouchListener(touchListener);
		TextView tv = (TextView) findViewById(R.id.txt_saved);
		tv.setText("Saved routes:");
		
		Toast.makeText(getApplicationContext() , "Hint: Long click to delete a destination" , Toast.LENGTH_SHORT).show();
		
		mListView = (ListView) findViewById(android.R.id.list);
		mListView.setEmptyView(findViewById(android.R.id.empty));
		
		ArrayList<String> mItems = new ArrayList<String>();
		for (int i=0;i<SavedRouteName.getInstance().savedRouteName.size();i++){
			mItems.add(SavedRouteName.getInstance().savedRouteName.get(i));
		}
		
		SavedRouteAdapter adapter = new SavedRouteAdapter(getApplicationContext(), mItems);
		mListView.setAdapter(adapter);
		
		final SavedRouteAdapter adap = adapter;
		
		mListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
			{
				Bundle data = new Bundle();
				data.putInt("whatRoute", arg2);
				Intent i = new Intent(cnt, EnterRoute.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtras(data);
				cnt.startActivity(i);				
			}			
		});
		
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, 
					int pos, long id){
				
				final int position = pos;
				AlertDialog.Builder dialog = new AlertDialog.Builder(cnt);
				final AlertDialog.Builder auxDialog = dialog; 
				dialog.setMessage("Are you sure you want to delete " +"\""+ adap.mItems.get(position) + "\""+ " from the saved routes?");
				dialog.setNegativeButton("Delete", new OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{					
						adap.mItems.remove(position);
						adap.notifyDataSetChanged();
						SavedRouteVector.getInstance().savedRoute.remove(position);
						SavedRouteName.getInstance().savedRouteName.remove(position);
						
						if ( SavedRouteVector.getInstance().savedRoute.isEmpty()){
							Toast.makeText(getApplicationContext() , "All saved routes were deleted!" , Toast.LENGTH_SHORT).show();
							finish();
						}
						else {
							Toast.makeText(getApplicationContext() , "Deleted!" , Toast.LENGTH_SHORT).show();
						}
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
		
		Button clear = (Button)findViewById(R.id.clear_all_routes);
		
		clear.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(SavedRoute.this);
				final AlertDialog.Builder auxDialog = dialog; 
				dialog.setMessage("Are you sure you want to delete all routes?");
				dialog.setNegativeButton("Delete", new OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{					
						while ( !SavedRouteVector.getInstance().savedRoute.isEmpty()){
							SavedRouteVector.getInstance().savedRoute.remove(0);
							SavedRouteName.getInstance().savedRouteName.remove(0);
						}
						Toast.makeText(getApplicationContext() , "All deleted!" , Toast.LENGTH_SHORT).show();
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
	}

}
