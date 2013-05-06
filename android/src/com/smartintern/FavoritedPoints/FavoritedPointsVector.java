package com.smartintern.FavoritedPoints;

import java.util.ArrayList;

import com.smartintern.saveroute.SavedRouteVector;

public final class FavoritedPointsVector {
	private static final FavoritedPointsVector instance = new FavoritedPointsVector();
	
	public ArrayList<LatitudeLongitude> favPoints = new ArrayList<LatitudeLongitude>();
	
	public static FavoritedPointsVector getInstance() {
		return instance;
	}
	
	private FavoritedPointsVector()
	{
	}
}
