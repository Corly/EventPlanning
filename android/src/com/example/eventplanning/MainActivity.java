package com.example.eventplanning;


import java.util.List;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.smartIntern.server.AuthData;
import com.smartIntern.server.IntelWebService;
import com.smartIntern.server.POILocation;
import com.smartIntern.server.IntelWebService.OnResponseListener;

public class MainActivity extends Activity 
{
	
	AuthData data;
	ListBox list;
	GlobalPositioning GP;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getAuthData();
		GP = new GlobalPositioning(this);
		GP.getLocation();
		list = (ListBox) findViewById(R.id.listBox1);
	}
	
	private void getAuthData()
	{
		IntelWebService.getInstance().getOauth20Token(new OnResponseListener<AuthData>() {
			   @Override
			   public void onSuccess(AuthData response) {
			        		data = response;	    
			   }

			   @Override
			   public void onError() {
			   }
			  });
	}
	
	OnResponseListener<List<POILocation>> responseListener = new OnResponseListener<List<POILocation>>()
	{
		@Override
		public void onSuccess(List<POILocation> response)
		{
			list.Clear();
			for (int i = 0;i<response.size();i++)
			{
				POILocation loc = response.get(i);
				list.InsertItem(loc.getName());
			}
		}

		@Override
		public void onError()
		{
			Log.d("TAG","Error??");			
		}		
	};	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void Click(View  v)
	{
		Location location = GP.getLocation();
		if (location == null) return;
		TextView t = (TextView)findViewById(R.id.textView1);
		
		t.setText("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());
		IntelWebService.getInstance().getNearByPOIs("POI_ALL", location.getLatitude(), location.getLongitude(), "10000",responseListener);
		
	}

}
