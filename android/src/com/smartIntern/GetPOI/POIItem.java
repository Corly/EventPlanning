package com.smartIntern.GetPOI;

import org.json.JSONException;
import org.json.JSONObject;

public class POIItem {
	private String latitude;
	private String longitude;
	private String name;
	private String streetAddress;
	private String phone;
	private String email;
	private String city;

	public void parseContent(JSONObject obj) throws JSONException {
		latitude = obj.getString("latitude");
		longitude = obj.getString("longitude");
		name = obj.getString("name");
		streetAddress = obj.getString("streetAddress");
		phone = obj.getString("phone");
		email = obj.getString("email");
		city = obj.getString("city");
	}

	public String getCity() {
		return city;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public String getStreetAddress() {
		return streetAddress;
	}
}
