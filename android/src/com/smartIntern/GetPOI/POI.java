package com.smartIntern.GetPOI;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventplanning.CurrentDestinations;
import com.example.eventplanning.GlobalVector;
import com.example.eventplanning.IntelGeolocation;
import com.example.eventplanning.R;
import com.example.eventplanning.RouteActivity;
import com.example.eventplanning.UrlCreator;
import com.smartIntern.server.ServerResponse;
import com.smartintern.FavoritedPoints.LatitudeLongitude;

public class POI extends Activity
{
	private ArrayList<POIItem> mItems;
	final Context cnt = this;
	private String category;
	private String radius;
	private String lat;
	private String lng;
	private Boolean results_show = true;
	private ListView mListView;
	
	
	private OnItemClickListener listener = new OnItemClickListener()
	{
		public void onItemClick(AdapterView<?> adapter, View view, int position, long id)
		{
			final int pos = position;
			AlertDialog.Builder dialog = new AlertDialog.Builder(cnt);
			dialog.setMessage(mItems.get(position).convertToString());
			dialog.setPositiveButton("Add to Route", new OnClickListener()
			{
				public void onClick(DialogInterface arg0, int arg1)
				{					
					
					LatitudeLongitude ll = new LatitudeLongitude();
					ll.name = mItems.get(pos).getName();
					ll.lat = Double.parseDouble(mItems.get(pos).getLatitude());
					ll.lng = Double.parseDouble(mItems.get(pos).getLongitude());
					GlobalVector.getInstance().routeList.add(ll);
					MakeToast("Done!");
				}				
			});
			dialog.setNegativeButton("Show Route", new OnClickListener()
			{
				public void onClick(DialogInterface arg0, int arg1)
				{					
					if (GlobalVector.getInstance().routeList.isEmpty()){
		        		//Toast.makeText(getApplicationContext() , "Your list of current destinations is empty" , Toast.LENGTH_SHORT).show();
						LatitudeLongitude ll = new LatitudeLongitude();
						ll.name = mItems.get(pos).getName();
						ll.lat = Double.parseDouble(mItems.get(pos).getLatitude());
						ll.lng = Double.parseDouble(mItems.get(pos).getLongitude());
						GlobalVector.getInstance().routeList.add(ll);
						Intent i = new Intent(getApplicationContext(), RouteActivity.class);
		        		startActivity(i);
					}
		        	else 
		        	{
		        		LatitudeLongitude ll = new LatitudeLongitude();
						ll.name = mItems.get(pos).getName();
						ll.lat = Double.parseDouble(mItems.get(pos).getLatitude());
						ll.lng = Double.parseDouble(mItems.get(pos).getLongitude());
						GlobalVector.getInstance().routeList.add(ll);
		        		Intent i = new Intent(getApplicationContext(), RouteActivity.class);
		        		startActivity(i);
		        	}
				}				
			});
			dialog.show();
		}		
	};

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.restaurants);
		
		mListView = (ListView) findViewById(android.R.id.list);
		mListView.setEmptyView(findViewById(android.R.id.empty)); 
		mListView.setOnItemClickListener(listener);
		mItems = new ArrayList<POIItem>();
		
		Bundle extras = getIntent().getExtras();
		if (extras != null){
			category = extras.getString("category");
			radius = extras.getString("radius");
			lat = extras.getString("lat");
			lng = extras.getString("lng");
		}
		
		TextView category_tag = (TextView)findViewById(R.id.poi_activitity_category_tag);
		category_tag.setText(category + ":");
		
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
		if (!IntelGeolocation.isNetworkAvailable(cnt))
		{
			IntelGeolocation.MakeToast("No internet connection!", cnt);
			finish();
			return;
		}
		UrlCreator crt = new UrlCreator(cnt);
		crt.setRequierment("poi");
		crt.addArgument("access_token", IntelGeolocation.GetAccessToken());
		crt.addArgument("lat", lat);
		crt.addArgument("lng", lng);
		crt.addArgument("category", GetCategory(category));// asta o sa il dam ca parametru la intent
		crt.addArgument("radius", radius);
		crt.addArgument("num_results", "50");
		ServerResponse resp = null;
		try
		{
			resp = crt.executeWithGetArray();
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
				mListView.setAdapter(new POIAdapter(cnt, mItems));
			}
		});
		
		final ProgressBar pb = (ProgressBar) findViewById(R.id.progress_bar);
		pb.post(new Runnable() 
		{
			public void run()
			{
				pb.setVisibility(4);
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
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
	        		
	        default:
	            return super.onOptionsItemSelected(item);
	    }
		return false;
	}

}
