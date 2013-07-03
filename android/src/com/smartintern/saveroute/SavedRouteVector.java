package com.smartIntern.saveroute;

import java.util.ArrayList;

import android.graphics.Bitmap;

public final class SavedRouteVector {
	private static final SavedRouteVector instance = new SavedRouteVector();
	
	public ArrayList<String> savedRoute = new ArrayList<String>();
	public ArrayList<Bitmap> savedImage = new ArrayList<Bitmap>();
	
	public static SavedRouteVector getInstance() {
		return instance;
	}
	
	private SavedRouteVector()
	{
	}
}

