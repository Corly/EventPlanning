package com.example.eventplanning;

import java.util.ArrayList;

import com.smartintern.FavoritedPoints.FavoritedPoints;
import com.smartintern.FavoritedPoints.FavoritedPointsVector;
import com.smartintern.FavoritedPoints.LatitudeLongitude;
import com.smartintern.saveroute.SavedRoute;
import com.smartintern.saveroute.SavedRouteName;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
				dialog.setMessage("Are you sure you want to delete " + "\"" + adap.mItems.get(position)+"\"" + " from the current route?");
				dialog.setNegativeButton("Delete", new OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{					
						if ( !(position == 0 || position == adap.mItems.size()) ){
							if (adap.mItems.get(position+1).contentEquals(adap.mItems.get(position-1))){
								adap.mItems.remove(position+1);
								adap.mItems.remove(position);
								GlobalVector.getInstance().routeList.remove(position+1);
								GlobalVector.getInstance().routeList.remove(position);
								adap.notifyDataSetChanged();
								if ( GlobalVector.getInstance().routeList.isEmpty()){
									Toast.makeText(getApplicationContext() , "All destinations were deleted!" , Toast.LENGTH_SHORT).show();
									finish();
								}
								else
								IntelGeolocation.MakeToast("Deleted!", cnt);
							}
							else{
								adap.mItems.remove(position);
								adap.notifyDataSetChanged();
								GlobalVector.getInstance().routeList.remove(position);
								if ( GlobalVector.getInstance().routeList.isEmpty()){
									Toast.makeText(getApplicationContext() , "All destinations were deleted!" , Toast.LENGTH_SHORT).show();
									finish();
								}
								else
								IntelGeolocation.MakeToast("Deleted!", cnt);
							}
						}
						else{
							adap.mItems.remove(position);
							adap.notifyDataSetChanged();
							GlobalVector.getInstance().routeList.remove(position);
							if ( GlobalVector.getInstance().routeList.isEmpty()){
								Toast.makeText(getApplicationContext() , "All destinations were deleted!" , Toast.LENGTH_SHORT).show();
								finish();
							}
							else
								IntelGeolocation.MakeToast("Deleted!", cnt);
						}
					}				
				});
				dialog.setPositiveButton("Cancel", new OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{					
						AlertDialog d = auxDialog.create();
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
				dialog.setMessage("Are you sure you want to delete all destinations?");
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.current_destination, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) 
	    {   
    
	        case R.id.menu_save_point:
	        {
	        	GlobalPositioning gp = new GlobalPositioning(CurrentDestinations.this);
	    		final double lat = gp.getLocation().getLatitude();
	    		final double lng = gp.getLocation().getLongitude();
	        	AlertDialog.Builder dialog = new AlertDialog.Builder(CurrentDestinations.this);
				final AlertDialog.Builder auxDialog = dialog;
				dialog.setMessage("Choose a name for saving:");
				final EditText input = new EditText(CurrentDestinations.this);
				dialog.setView(input);
				dialog.setNegativeButton("Save", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{					
						LatitudeLongitude ll = new LatitudeLongitude();
						ll.lat = lat;
						ll.lng = lng;
						ll.name = input.getText().toString(); 
						
						FavoritedPointsVector.getInstance().favPoints.add(ll);
						Toast.makeText(getApplicationContext() , "Point saved" , Toast.LENGTH_SHORT).show();
					}				
				});
				dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{					
						AlertDialog d = auxDialog.show();
						d.dismiss();
					}				
				});
				dialog.show();
				break;
	        }
	        	
	        default:
	            return super.onOptionsItemSelected(item);
	    }
		return false;
	}
	
}
