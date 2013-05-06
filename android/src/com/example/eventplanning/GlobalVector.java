package com.example.eventplanning;

import java.util.ArrayList;

import com.smartintern.FavoritedPoints.LatitudeLongitude;

public final class GlobalVector {
	private static final GlobalVector instance = new GlobalVector();
	
	public ArrayList<LatitudeLongitude> routeList = new ArrayList<LatitudeLongitude>();
	
	public static GlobalVector getInstance() {
		return instance;
	}
	
	private GlobalVector()
	{
	}
}
