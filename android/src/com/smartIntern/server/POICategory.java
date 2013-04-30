package com.smartIntern.server;

import org.json.JSONException;
import org.json.JSONObject;

public class POICategory implements JsonReadable {

	private String id;
	private String label;
	private String labelPlural;
	private String description;
	
	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public String getLabelPlural() {
		return labelPlural;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return labelPlural;
	}

	@Override
	public void readFromJson(JSONObject jsonObject) throws JSONException {
		id = jsonObject.optString("id");
		label = jsonObject.optString("label");
		labelPlural = jsonObject.optString("labelPlural");
		description = jsonObject.optString("description");
	}
	
}
