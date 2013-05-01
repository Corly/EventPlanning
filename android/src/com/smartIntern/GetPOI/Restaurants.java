package com.smartIntern.GetPOI;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.example.eventplanning.IntelGeolocation;
import com.example.eventplanning.R;
import com.example.eventplanning.UrlCreator;
import com.smartIntern.server.ServerResponse;

public class Restaurants extends Activity
{
	private ArrayList<RestaurantItem> mItems;
	final Context cnt = this;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.restaurants);

		ListView mListView = (ListView) findViewById(android.R.id.list);
		mListView.setEmptyView(findViewById(android.R.id.empty)); 
		mItems = new ArrayList<RestaurantItem>();
		
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
	
	private void GetPOIS()
	{
		UrlCreator crt = new UrlCreator(cnt);
		crt.setRequierment("poi");
		crt.addArgument("access_token", IntelGeolocation.GetAccessToken());
		crt.addArgument("lat", "44.43250");
		crt.addArgument("lng", "26.10389");
		crt.addArgument("category", "POI_RESTAURANTS");// asta o sa il dam ca parametru la intent
		crt.addArgument("radius", "2000");
		crt.addArgument("num_results", "50");
		ServerResponse resp = null;
		try
		{
			resp = crt.execute();
		} catch (ClientProtocolException e1)
		{
			Toast.makeText(cnt , "Server response error" , Toast.LENGTH_SHORT).show();
			return;
		} catch (IOException e1)
		{
			Toast.makeText(cnt , "I/O error" , Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (resp == null)
		{
			Toast.makeText(cnt , "Server response error" , Toast.LENGTH_SHORT).show();
			return;
		}
		
		ListView mListView = (ListView) findViewById(android.R.id.list);
		if (resp.getStatus() == false) {
			Toast.makeText(cnt, resp.getError(), Toast.LENGTH_SHORT).show();
		} else {
			try {
				JSONArray arr = resp.getArrayData();
				for (int i = 0; i < arr.length(); i++) {
					RestaurantItem mes = new RestaurantItem();
					mes.parseContent(arr.getJSONObject(i));
					mItems.add(mes); // closest on top
				}
			} catch (JSONException e) {
				Toast.makeText(cnt, "Server response format error.",
						Toast.LENGTH_SHORT).show();
				return;
			}
		}
		mListView.post(new Runnable()
		{
			public void run()
			{
				ListView mListView = (ListView) findViewById(android.R.id.list);
				mListView.setAdapter(new RestaurantAdapter(cnt, mItems));
			}
		});
	}
	
	

}
