package com.example.eventplanning;

import java.io.InputStream;
import java.net.URL;

import org.json.JSONArray;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.smartIntern.server.ServerResponse;
import com.smartintern.FavoritedPoints.LatitudeLongitude;
import com.smartintern.saveroute.SavedRouteName;
import com.smartintern.saveroute.SavedRouteVector;

public class RouteActivity extends Activity
{

	private Context cnt;
	private ListBox list;	
	private TextView distanceTEXT;
	private TextView timeTEXT;
	private Button saveRoute;
	private Button showSomething;
	private ProgressBar spinner;
	private Bitmap mapImageForSaving;
	private String stringRoute;

	private ImageView imageViewer;
	private boolean whichisshown = false;
	
	private void Show()
	{
		if (!whichisshown)
		{
			imageViewer.setVisibility(View.INVISIBLE);
			list.setVisibility(View.VISIBLE);
			Log.d("TEST","Nebun?");
			showSomething.setText("Show map");
		}
		else
		{
			list.setVisibility(View.INVISIBLE);
			imageViewer.setVisibility(View.VISIBLE);
			showSomething.setText("Show route");
		}
	}

	
	public void GetRoute(String token)
	{
		if (!IntelGeolocation.isNetworkAvailable(cnt))
		{
			IntelGeolocation.MakeToast("No internet connection!", cnt);
			finish();
			return;
		}
		UrlCreator creator = new UrlCreator(cnt);
		creator.setRequierment("route");
		creator.addArgument("access_token", token);
		int numberofpoints = GlobalVector.getInstance().routeList.size();
		LatitudeLongitude origin = new LatitudeLongitude(44.43250,26.10389);
		LatitudeLongitude dest = GlobalVector.getInstance().routeList.get(numberofpoints-1);
		
		creator.addArgument("origin_lat", origin.lat+"");
		creator.addArgument("origin_lng", origin.lng+"");
		creator.addArgument("destination_lat",dest.lat+"");
		creator.addArgument("destination_lng", dest.lng+"");
		
		String via_points = "";
		LatitudeLongitude point;
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
			stringRoute = "";
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
			stringRoute = stringRoute + distance + "&";
			
			final String dist = distance;
			distanceTEXT.post(new Runnable(){public void run(){distanceTEXT.setText(dist);}});
			
			int poz2 = str.indexOf("totalTimeSeconds") + "totalTimeSeconds".length() + 2;
			distance = "Time to destination : ";			
			for (int i = poz2;str.charAt(i) != ',';i++)
			{
				distance += str.charAt(i);
			}
			distance += " seconds";
			stringRoute = stringRoute + distance + "&";
			
			final String time = distance;
			timeTEXT.post(new Runnable(){public void run(){timeTEXT.setText(time);}});
			
			saveRoute.post(new Runnable() {
				@Override
				public void run() {
					saveRoute.setVisibility(0);
				}
			});
			
			
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
				final int ind = i;
				if ( route.startsWith("Arrive")){
					stringRoute = stringRoute + "You have reached the destination" +"&";
				}
				else {
					stringRoute = stringRoute + locationString +"&";
				}
				
				list.post(new Runnable(){
					public void run(){
						if ( route.startsWith("Arrive") && ind == obj.length() - 1){
							list.InsertItem("You have reached the destination");
						}
						else {
							list.InsertItem(route);
						}
					}
				});
			}
			
		} 
		catch (Exception er)
		{
			IntelGeolocation.MakeToast("Error creating route!", cnt);
			finish();
		}
	}
	
	public Bitmap GetStaticMapURL(String token)
	{
		if (!IntelGeolocation.isNetworkAvailable(cnt))
		{
			IntelGeolocation.MakeToast("No internet connection!", cnt);
			finish();
		}
		UrlCreator creator = new UrlCreator(cnt);
		creator.setRequierment("staticurl");
		creator.addArgument("access_token", token);
		creator.addArgument("size", "480x480");
		creator.addArgument("center", "44.43250,26.10389");
		creator.addArgument("zoom", "14");
		creator.addArgument("dpi","300");
		
		String routeArg = "";
		LatitudeLongitude point;
		routeArg += "from:"+44.43250 + ","+26.10389+"%7C";
		int size = GlobalVector.getInstance().routeList.size();
		point = GlobalVector.getInstance().routeList.get(size-1);
		routeArg += "to:"+point.lat+","+point.lng;
		
		if (size >= 2) 
		{
			routeArg+= "%7Cvia:";
			for (int i = 0;i<size - 2;i++)
			{
				point = GlobalVector.getInstance().routeList.get(i);
				routeArg += (point.lat + "," + point.lng + ",");
			}			
			point = GlobalVector.getInstance().routeList.get(size-2);
			routeArg+= (point.lat + "," + point.lng);
		}
		
		routeArg+="%7Calgorithm:FASTEST";
		creator.addArgument("route", routeArg);	
		try
		{
			String data = creator.executeWithGetHTTP();
			int start = "{'data':{'url':".length() +1 ;
			data = data.substring(start, data.length()-3);
			Log.d("TEST",data);
			InputStream in = new URL(data).openConnection().getInputStream();
			final Bitmap imageMAP = BitmapFactory.decodeStream(in);
			imageViewer.post(new Runnable(){public void run(){imageViewer.setImageBitmap(imageMAP);}});
			return imageMAP;
		}
		catch (Exception er)
		{
			IntelGeolocation.MakeToast("Error fetching map data!", cnt);
			return null;
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
		saveRoute = (Button) findViewById(R.id.save_route);
		spinner = (ProgressBar)findViewById(R.id.route_progress);
		imageViewer = (ImageView)findViewById(R.id.imageView1);
		showSomething = (Button) findViewById(R.id.show_current);
		Show();
		cnt = this;
		Runnable runnable = new Runnable() 
		{
	        public void run() 
	        {
	        	String token = IntelGeolocation.GetAccessToken();
	        	GetRoute(token);
	        	mapImageForSaving = GetStaticMapURL(token);	
	        	spinner.post(new Runnable(){public void run(){spinner.setVisibility(View.GONE);}});
	        	showSomething.post(new Runnable(){public void run(){showSomething.setVisibility(View.VISIBLE);}});
	        }    
	    };
	    Thread mythread = new Thread(runnable);
	    mythread.start();
	    
	    saveRoute.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (stringRoute != null){
					AlertDialog.Builder dialog = new AlertDialog.Builder(RouteActivity.this);
					final AlertDialog.Builder auxDialog = dialog;
					dialog.setMessage("Choose a name for saving:");
					final EditText input = new EditText(RouteActivity.this);
					dialog.setView(input);
					dialog.setNegativeButton("Save", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface arg0, int arg1)
						{					
							SavedRouteVector.getInstance().savedRoute.add(stringRoute);
							SavedRouteVector.getInstance().savedImage.add(mapImageForSaving);
							SavedRouteName.getInstance().savedRouteName.add(input.getText().toString()); 
							Toast.makeText(getApplicationContext() , "Route saved" , Toast.LENGTH_SHORT).show();
						}				
					});
					dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface arg0, int arg1)
						{					
							AlertDialog d = auxDialog.show();
							d.dismiss();
						}				
					});
					dialog.show();
				}
				else {
					Toast.makeText(getApplicationContext() , "No route to save" , Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	public void Show_Click(View v)
	{
		whichisshown = !whichisshown;
		Show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}

}
