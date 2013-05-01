package com.example.eventplanning;


import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.smartIntern.GetPOI.Restaurants;
import com.smartIntern.server.ApiHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import com.smartIntern.server.ServerResponse;


public class MainActivity extends Activity 
{
	
	private Button btn;
	private GlobalPositioning GP;
	private ListBox list;
	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn = (Button)findViewById(R.id.button1);
		btn.setClickable(false);
		list = (ListBox) findViewById(R.id.listBox1);
		
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
		TextView t = (TextView)findViewById(R.id.textView1);		
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
		Runnable runnable = new Runnable() 
		{
	        public void run() 
	        {
	        		try
					{
						v.setClickable(false);
						Click2(v);
					} catch (JSONException e)
					{
						e.printStackTrace();
					}
	        }    
	    };
	    Thread mythread = new Thread(runnable);
	    mythread.start();
	}
	
	public void Click2(final View v) throws JSONException
	{
		Location location = GP.getLocation();
		if (location == null) return;
		UrlCreator creator = new UrlCreator(context);
		creator.setRequierment("poi");
		creator.addArgument("access_token", IntelGeolocation.GetAccessToken());
		creator.addArgument("lat", 44.43250+"");
		creator.addArgument("lng", 26.10389+"");
		creator.addArgument("radius","1000");
		creator.addArgument("category", "poi_gas_stations");
		creator.addArgument("num_results", "100");
		creator.addArgument("alt", "json");
		try
		{
			final ServerResponse resp = creator.execute();
			list.post(new Runnable(){ public void run() { list.SetContents(ApiHandler.ParseJSON(resp)); } });
			v.post(new Runnable(){public void run() { v.setClickable(true); }});
		} catch (ClientProtocolException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			
			e.printStackTrace();
		}
	}

}
