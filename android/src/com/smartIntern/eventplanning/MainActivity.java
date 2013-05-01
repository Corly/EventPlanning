package com.smartIntern.eventplanning;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.smartIntern.R;
import com.smartIntern.GetPOI.Restaurants;
import com.smartIntern.server.NewAndImprovedApiHandler;
import com.smartIntern.server.ServerResponse;

import com.loopj.android.http.*;


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
		//btn.setClickable(false);
		list = (ListBox) findViewById(R.id.listBox1);

		
		Log.d("Checking", NewAndImprovedApiHandler.GetAccessToken());
		NewAndImprovedApiHandler.GetPoi();
		/*AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.google.com", new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Log.d("Succes!", response);
		    }
		});*/

		/*try {
			Log.d("Come on!", getHTTP("https://api.intel.com:8081/location/v2/poi?access_token=c9149580824a32c0f5894a5bdee15cb9&lat=44.43250&lng=26.10389&radius=2000&category=poi_restaurants&num_results=50", context));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*try
		{
			//Click2(this);
		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//SearchMyLocation();*/
		//Log.d("Test",IntelGeolocation.GetAccessToken());
	}
	
	public static String getHTTP(String url, Context context) throws ClientProtocolException, IOException 
	{

		StringBuilder responseBuilder = new StringBuilder();

		/*SharedPreferences mAppInfo = PreferenceManager
				.getDefaultSharedPreferences(context);
		
		final String token = mAppInfo.getString("token", null);*/

		/** Send request */
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);

		
		//not sure if casting will result to something that we want
		
		try {
			Log.d("DEBUG", "ajunge aici");
			Log.d("Reguest show", request.toString());
			HttpResponse response = httpclient.execute(request);
			Log.d("DEBUG", "ajunge aici");
			Log.i("Dragos", response.getStatusLine().toString());
			Log.d("DEBUG", "ajunge aici");
			HttpEntity entity = response.getEntity();
			Log.d("DEBUG", "ajunge aici");
			if (entity != null){
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);
				instream.close();
				return result;
			}
		} catch (Exception e){};
		
		return "ma-taaa";
	}
	
	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return sb.toString();
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
