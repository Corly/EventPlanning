package com.smartIntern.GetPOI;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.example.eventplanning.R;
import com.smartIntern.server.ApiHandler;
import com.smartIntern.server.ServerResponse;

public class Restaurants extends Activity
{
	private ArrayList<RestaurantItem> mItems;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.restaurants);

		ListView mListView = (ListView) findViewById(android.R.id.list);
		mListView.setEmptyView(findViewById(android.R.id.empty)); 
		final Context cnt = this;
	//nu final ca nu il mai poti modifica // true nuuuuuuu hai skype
		
		// Get Restaurants
		mItems = new ArrayList<RestaurantItem>();
		
		// muta tot codu de dupa mythread . start in run :D
		//whaat?
		
		
		Runnable runnable = new Runnable() 
		{
	        public void run() 
	        {
	        		ServerResponse resp = ApiHandler.getArray("https://api.intel.com:8081/location/v2/poi?access_token=db04f2ef4568fe36d727d4dea535483c&lat=44.43250&lng=26.10389&radius=2000&category=poi_restaurants&num_results=50",
							cnt);//pai LLOOOOL
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
					// nu poti accesa dintrun thread elemente din xml asa skype!
	        }    
	    };
	    Thread mythread = new Thread(runnable);
	    mythread.start();
		Log.d("tag","1");
		
		
	}

}
