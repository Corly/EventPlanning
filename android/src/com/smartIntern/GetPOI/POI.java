package com.smartIntern.GetPOI;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.example.eventplanning.IntelGeolocation;
import com.example.eventplanning.R;
import com.example.eventplanning.UrlCreator;
import com.smartIntern.server.ServerResponse;

public class POI extends Activity
{
	private ArrayList<POIItem> mItems;
	final Context cnt = this;
	private String category;
	private String radius;
	private Boolean results_show = true;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.restaurants);

		ListView mListView = (ListView) findViewById(android.R.id.list);
		mListView.setEmptyView(findViewById(android.R.id.empty)); 
		mItems = new ArrayList<POIItem>();
		
		Bundle extras = getIntent().getExtras();
		if (extras != null){
			category = extras.getString("category");
			radius = extras.getString("radius");
		}
		
		Runnable runnable = new Runnable() 
		{
	        public void run() 
	        {
	        	GetPOIS();
	        }    
	    };
	    Thread mythread = new Thread(runnable);
	    mythread.start();
	}
	
	public void MakeToast(final String message)
	{
		Handler toastHandler = new Handler();
		Runnable toastRunnable = new Runnable() 
		{
			public void run() 
			{
				Toast.makeText(cnt , message , Toast.LENGTH_SHORT).show();
			}
		};
		toastHandler.post(toastRunnable);
	}
	
	private void GetPOIS()
	{
		UrlCreator crt = new UrlCreator(cnt);
		crt.setRequierment("poi");
		crt.addArgument("access_token", IntelGeolocation.GetAccessToken());
		crt.addArgument("lat", "44.43250");
		crt.addArgument("lng", "26.10389");
		crt.addArgument("category", GetCategory(category));// asta o sa il dam ca parametru la intent
		crt.addArgument("radius", radius);
		crt.addArgument("num_results", "50");
		ServerResponse resp = null;
		try
		{
			resp = crt.execute();
		} catch (ClientProtocolException e1)
		{
			MakeToast("Server response error");
			return;
		} catch (IOException e1)
		{
			MakeToast("I/O error");
			return;
		}
		
		if (resp == null)
		{
			MakeToast("Server response error");
			return;
		}
		
		ListView mListView = (ListView) findViewById(android.R.id.list);
		if (resp.getStatus() == false) {
			Toast.makeText(cnt, resp.getError(), Toast.LENGTH_SHORT).show();
		} else {
			try {
				JSONArray arr = resp.getArrayData();
				if ( arr.length() == 0){
					IntelGeolocation.MakeToast("No results were found!", cnt);
					results_show = false;
				}
				for (int i = 0; i < arr.length(); i++) {
					POIItem mes = new POIItem();
					mes.parseContent(arr.getJSONObject(i));
					mItems.add(mes); // closest on top
				}
			} catch (JSONException e) {
				MakeToast("Server response format error");
				return;
			}
		}
		mListView.post(new Runnable()
		{
			public void run()
			{
				ListView mListView = (ListView) findViewById(android.R.id.list);
				mListView.setAdapter(new POIAdapter(cnt, mItems));
			}
		});
		
		   if ( !results_show) {
		    	finish();
		    }
	}
	
	private String GetCategory(String category){
		
		if ( category.startsWith("Restaurant"))
			return "POI_RESTAURANTS";
		else
		if ( category.startsWith("Hotel"))
			return "POI_HOTELS";
		else
		if ( category.startsWith("Gas"))
			return "POI_GAS_STATIONS";
		else
		if ( category.startsWith("Shopping"))
			return "POI_SHOPPING";
		else
		if ( category.startsWith("Transport"))
			return "POI_TRANSPORTATION";
		else
		if ( category.startsWith("Parking"))
			return "POI_PARKING";
		else
		if ( category.startsWith("Entertain"))
			return "POI_ENTERTAINMENT";
		else
		if ( category.startsWith("Emergency"))
			return "POI_EMERGENCY";
		else
		if ( category.startsWith("Tourism"))
			return "POI_TOURISM";
		
		return "";
	}
	

}
