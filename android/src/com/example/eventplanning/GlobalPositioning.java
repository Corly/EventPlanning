package com.example.eventplanning;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

@SuppressLint("Registered")
public class GlobalPositioning extends Service implements LocationListener
{
	private LocationManager manager;
	private Context context;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 metrii
    private static final long MIN_TIME_BW_UPDATES = 1000  * 1; // 1 minut
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean locationCanBeObtained = false;
    
    private Location lastKnownLocation;
	
	GlobalPositioning(Context cnt)
	{
		context = cnt;
		manager = (LocationManager)context.getSystemService(LOCATION_SERVICE);
		isGPSEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		isNetworkEnabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		locationCanBeObtained = isGPSEnabled || isNetworkEnabled;
		this.getLocation();
	}
	
	private void ShowSettingsAlert()
	{
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS & Network settings");
        alertDialog.setMessage("GPS and Networking are not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(intent);
            }
        });
 
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
        
        alertDialog.show();
	}
	
	public Location getLocation()
	{
		
		isGPSEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		isNetworkEnabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		Log.d("GPS",isGPSEnabled + " " + isNetworkEnabled);
		locationCanBeObtained = isGPSEnabled || isNetworkEnabled;
		
		if (!locationCanBeObtained) 
		{
			ShowSettingsAlert();
			return null;
		}		
		if (isNetworkEnabled) 
		{
			if (lastKnownLocation == null)
			{
				manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this); 
			}
			if (manager != null)
			{
				lastKnownLocation = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
			Log.d("GPS","Got Location from Networking");
		}else if (isGPSEnabled) 
		{
			if (lastKnownLocation == null)
			{
				manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this); 
			}
			if (manager != null)
			{
				lastKnownLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}
			Log.d("GPS","Got Location from GPS");
		}
		
		return lastKnownLocation;
	}

	public Location getLastKnownLocation()
	{
		return lastKnownLocation;
	}
	
	@Override
	public void onLocationChanged(Location t)
	{
		lastKnownLocation = t;
	}

	@Override
	public void onProviderDisabled(String arg0)
	{
	}

	@Override
	public void onProviderEnabled(String arg0)
	{
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2)
	{
		
	}

	@Override
	public IBinder onBind(Intent arg0)
	{
		
		return null;
	}
	

}
