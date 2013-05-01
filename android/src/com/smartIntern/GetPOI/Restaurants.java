package com.smartIntern.GetPOI;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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

		// Get Restaurants
		mItems = new ArrayList<RestaurantItem>();
		ServerResponse resp = ApiHandler.getArray("https://api.intel.com:8081/location/v2/poi?access_token=c743c833cd60b81dfc2da391bf78ac5e&lat=44.43250&lng=26.10389&radius=2000&category=poi_restaurants&num_results=50",
				this);
		//Log.d("resp", resp.toString());
		Log.d("tag","1");
		if (resp.getStatus() == false) {
			Toast.makeText(this, resp.getError(), Toast.LENGTH_SHORT).show();
		} else {
			try {
				JSONArray arr = resp.getArrayData();
				for (int i = 0; i < arr.length(); i++) {
					RestaurantItem mes = new RestaurantItem();
					mes.parseContent(arr.getJSONObject(i));
					mItems.add(mes); // closest on top
				}
			} catch (JSONException e) {
				Toast.makeText(this, "Server response format error.",
						Toast.LENGTH_SHORT).show();
			}
		}

		mListView.setAdapter(new RestaurantAdapter(this, mItems));
	}

}
