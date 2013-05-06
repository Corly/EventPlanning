package com.example.eventplanning;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smartintern.FavoritedPoints.FavoritedPoints;
import com.smartintern.FavoritedPoints.FavoritedPointsVector;
import com.smartintern.FavoritedPoints.LatitudeLongitude;
import com.smartintern.saveroute.SavedRoute;
import com.smartintern.saveroute.SavedRouteName;

public class FrontActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.front);
		
		Button btnNormal = (Button)findViewById(R.id.btn_find);
		Button btnFav = (Button)findViewById(R.id.btn_fav);
		
		btnNormal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplication(), MainActivity.class);
				//BASIC VALUES for testing. Change this when you finish!
				i.putExtra("lat", 44.43250);
				i.putExtra("lng", 26.10389);
				i.putExtra("title", "Route from current position");
				startActivity(i);
			}
		});
		
		btnFav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (FavoritedPointsVector.getInstance().favPoints.isEmpty()){
	        		Toast.makeText(getApplicationContext() , "You do not have any favorited points" , Toast.LENGTH_SHORT).show();
	        	}
	        	else {
	        		Intent i = new Intent(getApplicationContext(), FavoritedPoints.class);
	        		startActivity(i);
	        	}
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.front, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) 
	    {   
	        case R.id.menu_saved_routes:
	        {
	        	if (SavedRouteName.getInstance().savedRouteName.isEmpty())
	        		Toast.makeText(getApplicationContext() , "You do not have any saved routes" , Toast.LENGTH_SHORT).show();
	        	else {
	        		Intent i = new Intent(getApplicationContext(), SavedRoute.class);
	        		startActivity(i);
	        	}
	        	break;
	        }
	        
	        case R.id.menu_fav_points:
	        {
	        	if (FavoritedPointsVector.getInstance().favPoints.isEmpty()){
	        		Toast.makeText(getApplicationContext() , "You do not have any favorited points" , Toast.LENGTH_SHORT).show();
	        	}
	        	else {
	        		Intent i = new Intent(getApplicationContext(), FavoritedPoints.class);
	        		startActivity(i);
	        	}
	        	break;
	        }
	        
	        case R.id.menu_save_point:
	        {
	        	GlobalPositioning gp = new GlobalPositioning(FrontActivity.this);
	    		final double lat = gp.getLocation().getLatitude();
	    		final double lng = gp.getLocation().getLongitude();
	        	AlertDialog.Builder dialog = new AlertDialog.Builder(FrontActivity.this);
				final AlertDialog.Builder auxDialog = dialog;
				dialog.setMessage("Choose a name for saving:");
				final EditText input = new EditText(FrontActivity.this);
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
