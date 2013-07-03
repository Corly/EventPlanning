package com.smartIntern.FavoritedPoints;

public class LatitudeLongitude {
	public String name;
	public Double lat;
	public Double lng;
	public LatitudeLongitude(Double lat , Double lng)
	{
		this.lat = lat;
		this.lng = lng;
	}
	
	public LatitudeLongitude()
	{
		this.lat = 0.0;
		this.lng = 0.0;
	}

}
