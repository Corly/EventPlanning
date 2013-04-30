package com.smartIntern.notUsed;

import org.json.JSONException;
import org.json.JSONObject;


public class UserAuth implements JsonReadable{
	private String token;
	private String refreshToken;
	private String tokenType;
	private String scope;
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
	
	public String getScope() {
		return scope;
	}

	@Override
	public void readFromJson(JSONObject jsonObject) throws JSONException {
		token = jsonObject.optString("token");
		refreshToken = jsonObject.optString("refresh_token");
		tokenType = jsonObject.optString("token_type");
		expiresIn = jsonObject.optLong("expires_in", 0);
		scope = jsonObject.optString("scope");
	}
	
}
