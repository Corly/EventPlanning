package com.example.eventplanning;


import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.smartIntern.server.ApiHandler;

import android.app.Activity;
import android.content.Context;
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
		try
		{
			Click2(this);
		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//SearchMyLocation();
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
	
	public void Click(View v) throws JSONException
	{
		Click2(v.getContext());
	}
	
	public void Click2(Context c) throws JSONException
	{
		Location location = GP.getLocation();
		if (location == null) return;
		TextView t = (TextView)findViewById(R.id.textView1);		
		t.setText("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());		
		
		UrlCreator creator = new UrlCreator(c);
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
			ServerResponse resp = creator.execute();
			for (int i = 0;i<resp.getArrayData().length();i++)
			{
				JSONObject local  = new JSONObject(resp.getArrayData().getString(i));
				StringBuilder t1 = new StringBuilder();
				t1.append("Name : " + local.getString("name")+"\n");
				t1.append("Latitude : " + local.getString("latitude")+"\n");
				t1.append("Longitude : " + local.getString("longitude")+"\n");
				t1.append("City : " + local.getString("city") + "\n");
				t1.append("Phone : " + local.getString("phone") + "\n");
				t1.append("Address : " + local.getString("streetAddress"));
				list.InsertItem(t1.toString());
			}
		} catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	
		//Location location = GP.getLocation();
		//if (location == null) return;
		//TextView t = (TextView)findViewById(R.id.textView1);		
		//t.setText("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());
//		IntelWebService.getInstance().getNearByPOIs("POI_ALL", location.getLatitude(), location.getLongitude(), "10000",responseListener);
		/*String token = com.smartIntern.server.IntelGeolocation.GetAccessToken();*/
		/*Intent i = new Intent(context, Restaurants.class);
		startActivity(i);
		Log.d("hello","hello");*/
	}

}
