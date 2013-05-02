package com.example.eventplanning;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.smartIntern.server.ServerResponse;

public class RouteActivity extends Activity
{

	private Context cnt;
	private ListBox list;	
	private TextView distanceTEXT;
	private TextView timeTEXT;
	
	public void GetRoute(String token)
	{
		if (!IntelGeolocation.isNetworkAvailable(cnt))
		{
			IntelGeolocation.MakeToast("No internet connection!", cnt);
			return;
		}
		UrlCreator creator = new UrlCreator(cnt);
		creator.setRequierment("route");
		creator.addArgument("access_token", token);
		int numberofpoints = GlobalVector.getInstance().routeList.size();
		LatLng origin = new LatLng(44.43250,26.10389);
		LatLng dest = GlobalVector.getInstance().routeList.get(numberofpoints-1);
		
		creator.addArgument("origin_lat", origin.lat+"");
		creator.addArgument("origin_lng", origin.lng+"");
		creator.addArgument("destination_lat",dest.lat+"");
		creator.addArgument("destination_lng", dest.lng+"");
		
		String via_points = "";
		LatLng point;
		for (int i = 0;i<numberofpoints - 2;i++)
		{
			point = GlobalVector.getInstance().routeList.get(i);
			via_points += (point.lat + "," + point.lng + ",");
		}
		if (numberofpoints >= 2)
		{
			point = GlobalVector.getInstance().routeList.get(numberofpoints-2);
			via_points +=  (point.lat + "," + point.lng);	
		}
		if (via_points != "" ) creator.addArgument("via_points_lat_lng",via_points);
		creator.addArgument("route_algorithm", "FASTEST");
		try
		{
			final ServerResponse resp;
			resp = creator.executeWithGetArray();
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
				int start = text.indexOf("description") +  "'description':{'text':".length();	
				String locationString = "";
				for (int k = start;text.charAt(k) != '"';k++)
				{
					locationString+= text.charAt(k);
				}
				final String route = locationString;
				list.post(new Runnable(){public void run(){list.InsertItem(route);}});
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
	
	public void GetStaticMapURL(String token)
	{
		if (!IntelGeolocation.isNetworkAvailable(cnt))
		{
			IntelGeolocation.MakeToast("No internet connection!", cnt);
			return;
		}
		UrlCreator creator = new UrlCreator(cnt);
		creator.setRequierment("staticurl");
		creator.addArgument("access_token", token);
		creator.addArgument("size", "480x480");
		creator.addArgument("center", "44.43250,26.10389");
		creator.addArgument("zoom", "14");
		creator.addArgument("route","from:44.43250,26.10389%7Cto:44.435215,26.106804%7Calgorithm:FASTEST");
		creator.addArgument("dpi","300");
		try
		{
			String data = creator.executeWithGetHTTP();
			int start = "{'data':{'url':".length() +1 ;
			data = data.substring(start, data.length()-3);
			Log.d("TEST",data);
			
		} catch (ClientProtocolException e)
		{
			e.printStackTrace();
		} catch (IOException e)
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
	        	String token = IntelGeolocation.GetAccessToken();
	        	GetRoute(token);
	        	//GetStaticMapURL(token);	        	
	        }    
	    };
	    Thread mythread = new Thread(runnable);
	    mythread.start();	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
