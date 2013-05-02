package com.example.eventplanning;


import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.smartIntern.GetPOI.POI;


public class MainActivity extends Activity 
{
	
	private Button btn;
	private GlobalPositioning GP;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn = (Button)findViewById(R.id.button1);
		btn.setClickable(false);
		
		SearchMyLocation();
	}
	
	void SearchMyLocation()
	{
		Location location;
		while(true)
		{
			GP = new GlobalPositioning(this);
			location = GP.getLocation();
			if (location != null) break;
		}
		TextView t = (TextView)findViewById(R.id.RouteTextView1);		
		t.setText("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());
		btn.setClickable(true);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void Click(final View v) throws JSONException
	{
		Click2( );
	}
	
	public void Click2()
	{
		
		Location location = GP.getLocation();
		if (location == null) return;
		Intent i = new Intent(this, RouteActivity.class);
		startActivity(i);
	}
}
