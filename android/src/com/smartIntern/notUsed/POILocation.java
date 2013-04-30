package com.smartIntern.notUsed;

import org.json.JSONException;
import org.json.JSONObject;

public class POILocation implements JsonReadable {

	private String id;
	private String categoryId;
	private String name;
	private double latitude;
	private double longitude;
	private String city;
	private String streetAddress;
	private String website;
	private String phone;
	private String email;
	
	public String getId() {
		return id;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public String getName() {
		return name;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getCity() {
		return city;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public String getWebsite() {
		return website;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public void readFromJson(JSONObject jsonObject) throws JSONException {
		id = jsonObject.optString("id");
		categoryId = jsonObject.optString("category");
		name = jsonObject.optString("name");
		latitude = jsonObject.optDouble("latitude", 0.0);
		longitude = jsonObject.optDouble("longitude", 0.0);
		city = jsonObject.optString("city");
		streetAddress = jsonObject.optString("streetAddress");
		website = jsonObject.optString("website");
		phone = jsonObject.optString("phone");
		email = jsonObject.optString("email");
	}
	
}
