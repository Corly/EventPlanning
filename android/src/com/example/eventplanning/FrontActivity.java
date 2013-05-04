package com.example.eventplanning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.smartintern.saveroute.SavedRoute;
import com.smartintern.saveroute.SavedRouteName;

public class FrontActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.front);
		
		Button btnNormal = (Button)findViewById(R.id.btn_find);
		Button btnCity = (Button)findViewById(R.id.btn_city);
		
		btnNormal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplication(), MainActivity.class);
				startActivity(i);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) 
	    {
	        case R.id.show_current:
	        {
	        	if (GlobalVector.getInstance().routeList.isEmpty())
	        		Toast.makeText(getApplicationContext() , "Your list of current destinations is empty" , Toast.LENGTH_SHORT).show();
	        	else {
	        		Intent i = new Intent(getApplicationContext(), CurrentDestinations.class);
	        		startActivity(i);
	        	}
	        	break;
	        }
	        
	        case R.id.make_route:
	        {
	        	if (GlobalVector.getInstance().routeList.isEmpty())
	        		Toast.makeText(getApplicationContext() , "Your list of current destinations is empty" , Toast.LENGTH_SHORT).show();
	        	else 
	        	{
	        		Intent i = new Intent(getApplicationContext(), RouteActivity.class);
	        		startActivity(i);
	        	}
	        	break;
	        }
	        
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
	        		
	        default:
	            return super.onOptionsItemSelected(item);
	    }
		return false;
	}
}
