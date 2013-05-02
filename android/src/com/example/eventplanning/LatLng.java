package com.example.eventplanning;

public class LatLng {
	public String name;
	public Double lat;
	public Double lng;
	public LatLng(Double lat , Double lng)
	{
		this.lat = lat;
		this.lng = lng;
	}
	
	public LatLng()
	{
		this.lat = 0.0;
		this.lng = 0.0;
	}
}
