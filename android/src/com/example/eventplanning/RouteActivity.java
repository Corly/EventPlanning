package com.example.eventplanning;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.smartIntern.FavoritedPoints.FavoritedPointsVector;
import com.smartIntern.FavoritedPoints.LatitudeLongitude;
import com.smartIntern.saveroute.SavedRouteName;
import com.smartIntern.saveroute.SavedRouteVector;
import com.smartIntern.server.ServerResponse;

public class RouteActivity extends Activity
{

	private Context cnt ;
	private ListBox list;	
	private TextView distanceTEXT;
	private TextView timeTEXT;
	private Button saveRoute;
	private Button showSomething;
	private ProgressBar spinner;
	private Bitmap mapImageForSaving;
	private String stringRoute;
	private String lat;
	private String lng;
	private String algoritmType;

	private ImageView imageViewer;
	private boolean whichisshown = false;
	private double eQuatorialEarthRadius = 6378.1370D;
	private double d2r = (Math.PI / 180D);
	
	OnTouchListener touchListener = new OnTouchListener()
	{

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1)
		{
			switch (arg1.getAction())
			{
				case MotionEvent.ACTION_DOWN:
				{
					arg0.setBackgroundResource(R.drawable.routebuttonclick);
					return false;
				}
				case MotionEvent.ACTION_UP:
				{
					arg0.setBackgroundResource(R.drawable.routebutton);
					return false;
				}
			}
			return false;
		}
	};
	
	private void Show()
	{
		if (!whichisshown)
		{
			imageViewer.setVisibility(View.INVISIBLE);
			list.setVisibility(View.VISIBLE);
			showSomething.setText("Show map");
		}
		else
		{
			list.setVisibility(View.INVISIBLE);
			imageViewer.setVisibility(View.VISIBLE);
			showSomething.setText("Show route");
		}
	}
	
	LatitudeLongitude getCenter(LatitudeLongitude origin , ArrayList<LatitudeLongitude> destinations)
	{
		LatitudeLongitude centerPoint = null;
		double minLat = origin.lat, maxLat = origin.lat, minLng = origin.lng, maxLng = origin.lng;	
		
		for (int i = 0;i<destinations.size();i++)
		{
			LatitudeLongitude point = destinations.get(i);
			minLat = Math.min(minLat, point.lat);
			maxLat = Math.max(maxLat, point.lat);
			minLng = Math.min(minLng, point.lng);
			maxLng = Math.max(maxLng, point.lng);
		}		
		centerPoint = new LatitudeLongitude((minLat+maxLat)/2 , (minLng + maxLng)/2);		
		return centerPoint;
		
	}
	
	private double HaversineInM(double lat1, double long1, double lat2, double long2)
	{
	    return 1000.0 * HaversineInKM(lat1, long1, lat2, long2);
	}
	
	private double HaversineInKM(double lat1, double long1, double lat2, double long2)
	{
	    double dlong = (long2 - long1) * d2r;
	    double dlat = (lat2 - lat1) * d2r;
	    double a = Math.pow(Math.sin(dlat / 2D), 2D) + Math.cos(lat1 * d2r) * Math.cos(lat2 * d2r) * Math.pow(Math.sin(dlong / 2D), 2D);
	    double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
	    double d = eQuatorialEarthRadius * c;

	    return d;
	}
	
	private double LargestDistanceFromCenter(LatitudeLongitude center , ArrayList<LatitudeLongitude> destinations)
	{
		double maxDistance = -1.0;
		int arraySize = destinations.size();
		for (int i = 0;i<arraySize;i++)
		{
			maxDistance = Math.max(maxDistance, HaversineInM(center.lat,center.lng,destinations.get(i).lat,destinations.get(i).lng));
		}
		return maxDistance;
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
		LatitudeLongitude origin = new LatitudeLongitude(Double.parseDouble(lat),Double.parseDouble(lng));
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
		creator.addArgument("route_algorithm", algoritmType);
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
				if ( route.startsWith("Arrive")){
					stringRoute = stringRoute + "You have reached the destination" +"&";
				}
				else {
					stringRoute = stringRoute + locationString +"&";
				}
				
				list.post(new Runnable(){
					public void run(){
						if ( route.startsWith("Arrive") ){
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
		LatitudeLongitude origin = new LatitudeLongitude(44.43250 , 26.10389);// trebuie modificat.
		LatitudeLongitude center = getCenter(origin,GlobalVector.getInstance().routeList);
		double distanceMeters = LargestDistanceFromCenter(center,GlobalVector.getInstance().routeList);
		Log.d("TEST",distanceMeters+"");
		UrlCreator creator = new UrlCreator(cnt);
		creator.setRequierment("staticurl");
		creator.addArgument("access_token", token);
		creator.addArgument("size", "600x600");
		creator.addArgument("center", center.lat + "," + center.lng);
		creator.addArgument("zoom", "16");
		creator.addArgument("dpi","300");
		
		String routeArg = "";
		LatitudeLongitude point;

		routeArg += "from:"+ lat + ","+ lng +"%7C";

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
		
		routeArg+="%7Calgorithm:" + algoritmType;
		creator.addArgument("route", routeArg);	
		
		String showArg = "";
		showArg += origin.lat+","+origin.lng+"%7C";
		for (int i = 0;i<size-1;i++)
		{
			point = GlobalVector.getInstance().routeList.get(i);
			showArg += point.lat+","+point.lng+"%7C";
		}
		point = GlobalVector.getInstance().routeList.get(size-1);
		showArg += point.lat+","+point.lng;
		creator.addArgument("show", showArg);
		
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

		cnt = this;
		Bundle extras = getIntent().getExtras();
		if (extras != null){
			lat = extras.getString("lat");
			lng = extras.getString("lng");
		}
		
		algoritmType = "n";
		list = (ListBox) findViewById(R.id.listBox1);
		distanceTEXT = (TextView) findViewById(R.id.RouteTextView1);
		timeTEXT = (TextView) findViewById(R.id.RouteTextView2);	
		saveRoute = (Button) findViewById(R.id.save_route);
		saveRoute.setOnTouchListener(touchListener);
		spinner = (ProgressBar)findViewById(R.id.route_progress);
		imageViewer = (ImageView)findViewById(R.id.imageView1);
		showSomething = (Button) findViewById(R.id.show_current);
		showSomething.setOnTouchListener(touchListener);
		Show();
		Runnable runnable = new Runnable() 
		{
	        public void run() 
	        {
	        	boolean HasShown = false;
	        	while (algoritmType.contentEquals("n"))
	        	{
	        		if (!HasShown)
	        		{
	        			HasShown = true;
	        			ShowDialog(cnt);	        			
	        		}
	        	}
	        	
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
							AlertDialog d = auxDialog.create();
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
	
	public void ShowDialog(final Context cnt)
	{
		Activity activity = (Activity)cnt;
		activity.runOnUiThread(new Runnable() 
		{
		    @Override
		    public void run() 
		    {
		    	AlertDialog.Builder dialog = new AlertDialog.Builder(cnt);
				final AlertDialog.Builder auxDialog = dialog;
				dialog.setMessage("Choose your way of travelling");
				dialog.setNegativeButton("By foot", new DialogInterface.OnClickListener()
				{
				public void onClick(DialogInterface arg0, int arg1)
				{					
					algoritmType = "PEDESTRIAN";
					AlertDialog d = auxDialog.create();	
					d.dismiss();
				}				
				});
				dialog.setPositiveButton("By car", new DialogInterface.OnClickListener()
				{
				public void onClick(DialogInterface arg0, int arg1)
					{					
						algoritmType = "FASTEST";
						AlertDialog d = auxDialog.create();
						d.dismiss();
					}				
				});
				dialog.show();
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
		getMenuInflater().inflate(R.menu.current_destination, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) 
	    {   
    
	        case R.id.menu_save_point:
	        {
	        	GlobalPositioning gp = new GlobalPositioning(RouteActivity.this);
	    		final double lat = gp.getLocation().getLatitude();
	    		final double lng = gp.getLocation().getLongitude();
	        	AlertDialog.Builder dialog = new AlertDialog.Builder(RouteActivity.this);
				final AlertDialog.Builder auxDialog = dialog;
				dialog.setMessage("Choose a name for saving:");
				final EditText input = new EditText(RouteActivity.this);
				dialog.setView(input);
				dialog.setNegativeButton("Save", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{					
						LatitudeLongitude ll = new LatitudeLongitude();
						ll.lat = lat;
						ll.lng = lng;
						ll.name = input.getText().toString(); 
						
						FavoritedPointsVector.getInstance().favPoints.add(ll);
						Toast.makeText(getApplicationContext() , "Point saved" , Toast.LENGTH_SHORT).show();
					}				
				});
				dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{					
						AlertDialog d = auxDialog.create();
						d.dismiss();
					}				
				});
				dialog.show();
				break;
	        }
	        	
	        default:
	            return super.onOptionsItemSelected(item);
	    }
		return false;
	}

}
