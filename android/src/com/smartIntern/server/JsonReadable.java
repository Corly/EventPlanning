package com.smartIntern.server;

import org.json.JSONException;
import org.json.JSONObject;

public interface JsonReadable {
	public void readFromJson(JSONObject jsonObject) throws JSONException;
}
