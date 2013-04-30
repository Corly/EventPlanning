package com.smartIntern.notUsed;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthData implements JsonReadable {

	private String token;
	private String refreshToken;
	private String tokenType;
	private long expiresIn;
	
	public String getToken() {
		return token;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	@Override
	public void readFromJson(JSONObject jsonObject) throws JSONException {
		token = jsonObject.optString("token");
		refreshToken = jsonObject.optString("refresh_token");
		tokenType = jsonObject.optString("token_type");
		expiresIn = jsonObject.optLong("expires_in", 0);
	}
	
}
