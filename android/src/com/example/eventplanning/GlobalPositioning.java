package com.example.eventplanning;
import android.app.Activity;
import android.content.Context;
import android.location.*;
import android.os.Bundle;
import android.util.Log;

public class GlobalPositioning extends Activity implements LocationListener
{
	private LocationManager manager;
	
	GlobalPositioning()
	{
		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Log.e("GPS", "Aloha");
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, this); 
	}

	@Override
	public void onLocationChanged(Location arg0)
	{
		String lat = String.valueOf(arg0.getLatitude());
        String lon = String.valueOf(arg0.getLongitude());
        Log.e("GPS", "location changed: lat="+lat+", lon="+lon);		
	}

	@Override
	public void onProviderDisabled(String arg0)
	{
		
	}

	@Override
	public void onProviderEnabled(String arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2)
	{
		// TODO Auto-generated method stub
		
	}
	

}
