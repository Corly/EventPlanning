package com.smartintern.saveroute;

import java.util.ArrayList;

public final class SavedRouteVector {
	private static final SavedRouteVector instance = new SavedRouteVector();
	
	public ArrayList<String> savedRoute = new ArrayList<String>();
	
	public static SavedRouteVector getInstance() {
		return instance;
	}
	
	private SavedRouteVector()
	{
	}
}

