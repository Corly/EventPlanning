package com.smartIntern.FavoritedPoints;

import java.util.ArrayList;

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
