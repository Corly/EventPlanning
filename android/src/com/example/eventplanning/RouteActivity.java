package com.example.eventplanning;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.smartIntern.server.ServerResponse;

public class RouteActivity extends Activity
{

	Context cnt;
	ListBox list;	
	TextView distanceTEXT;
	TextView timeTEXT;
	
	public void GetRoute()
	{
		if (!IntelGeolocation.isNetworkAvailable(cnt))
		{
			IntelGeolocation.MakeToast("No internet connection!", cnt);
			return;
		}
		UrlCreator creator = new UrlCreator(this);
		creator.setRequierment("route");
		creator.addArgument("access_token", IntelGeolocation.GetAccessToken());
		creator.addArgument("origin_lat", "44.43250");
		creator.addArgument("origin_lng", "26.10389");
		creator.addArgument("destination_lat","44.435814");
		creator.addArgument("destination_lng", "26.09449");
		creator.addArgument("route_algorithm", "FASTEST");
		try
		{
			final ServerResponse resp;
			resp = creator.execute();
			if (resp == null) return;
			String str = resp.getData().toString();
			int poz1 = str.indexOf("totalDistanceInMeters") + "totalDistanceInMeters".length() + 2;
			
			String distance = "Distance to destination : ";			
			for (int i = poz1;str.charAt(i) != ',';i++)
			{
				distance += str.charAt(i);
			}
			distance += " meters";
			
			final String dist = distance;
			distanceTEXT.post(new Runnable(){public void run(){distanceTEXT.setText(dist);}});
			
			int poz2 = str.indexOf("totalTimeSeconds") + "totalTimeSeconds".length() + 2;
			distance = "Time to destination : ";			
			for (int i = poz2;str.charAt(i) != ',';i++)
			{
				distance += str.charAt(i);
			}
			distance += " seconds";
			
			final String time = distance;
			timeTEXT.post(new Runnable(){public void run(){timeTEXT.setText(time);}});
			
			
			final JSONArray obj = resp.getArrayData();
			for (int i =0;i<obj.length();i++)
			{
				final String text = obj.getString(i);
				list.post(new Runnable(){public void run(){list.InsertItem(text);}});
			}
			
		} catch (ClientProtocolException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);
		list = (ListBox) findViewById(R.id.listBox1);
		distanceTEXT = (TextView) findViewById(R.id.RouteTextView1);
		timeTEXT = (TextView) findViewById(R.id.RouteTextView2);
		
		cnt = this;
		Runnable runnable = new Runnable() 
		{
	        public void run() 
	        {
	        	GetRoute();
	        }    
	    };
	    Thread mythread = new Thread(runnable);
	    mythread.start();	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.route, menu);
		return true;
	}

}
