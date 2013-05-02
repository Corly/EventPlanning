package com.example.eventplanning;

import java.util.ArrayList;

public final class GlobalVector {
	private static final GlobalVector instance = new GlobalVector();
	
	public ArrayList<LatLng> routeList = new ArrayList<LatLng>();
	
	public static GlobalVector getInstance() {
		return instance;
	}
	
	private GlobalVector(){
	}
}
