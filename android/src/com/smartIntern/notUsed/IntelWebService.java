package com.smartIntern.notUsed;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.smartIntern.notUsed.HttpConnectionUtil.HttpResponse;

import android.os.AsyncTask;
import android.util.Log;

public class IntelWebService {
	private static final String CLIENT_ID = "4247c4dce5a5b0c58e29b2a830f8c768";
	private static final String SECRET_KEY = "de779a6a10dd3443";
	
	// URL params
	private static final String URL_PARAM_ACCESS_TOKEN = "{access_token}";
	private static final String URL_PARAM_POI_CATEGORY = "{category}";
	private static final String URL_PARAM_LATITUDE = "{latitude}";
	private static final String URL_PARAM_LONGITUDE = "{longitude}";
	private static final String URL_PARAM_RADIUS = "{radius}";
	private static final String CODE = "htIM!IAAAABLmriSeKtRnufcwGFvSNtbJA2qDyemk9ZmHmrmKCsu0QQEAAAGhWmlJ9Z444v-jt_x7OUSgcKk4SCma2LzqUvusKGnr4MYowSDODEKeIjELESEFRBtRYuKW9BdFkGB-YWScMuTtEpKVXMxfBh43aPduAjvaBfAYE1Fiuv8_Xtj7WmetUcNLgKyOfbH8Jwb6zAYamhaNeUZxEJceN16t19P5OTzefwdgi9692kuJwH0Tx-iHTWXpBfYvQCmZt-0mo834FFIoJi0ArQAInLeBIQ_5po9IZwC8Y56N1ndnediz0thUF0PHgKdyh3GjM-wFBz-ovObP2MnbE6nU6yYnREcYIZUYyV13V73Vc8YeWsjpqXOetPba1N3N1De10RFfpUaQXYqkqIrIDghi-hG8SKTOAiIhMbLZtjmyAAANhWsK47YHhD9kJIf8nxwutpiRAvXF0_7CUnMwUuwHfvpDCYAyKqoeEA";
	
	private static final String BASE_URL = "https://api.intel.com:8081/";
	private static final String OAUTH20_URL = BASE_URL + 
	"oauth20/token?client_id=" + CLIENT_ID + 
	"&client_secret=" + SECRET_KEY + "&grant_type=client_credentials&scope=location:basic&language1=en&language2=fr";
	private static final String USERAUTH = "https://api.intel.com:8081/identityui/v1/auth?client_id=" + CLIENT_ID +
				"&client_secret=" + SECRET_KEY + "&redirect_uri=urn:intel:identity:oauth:oob:async&grant_type=authorization_code"
				+ "&code=" + CODE;
	
	private static final String POI_CATEGORIES_URL = BASE_URL + 
	"location/v2/poi/categories?" +
	"access_token=" + URL_PARAM_ACCESS_TOKEN;
	
	private static final String POIS_BY_CATEGORIE_URL = BASE_URL + 
	"location/v2/poi?" +
	"access_token=" + URL_PARAM_ACCESS_TOKEN +
	"&category=" + URL_PARAM_POI_CATEGORY +
	"&lat=" + URL_PARAM_LATITUDE +
	"&lng=" + URL_PARAM_LONGITUDE +
	"&radius=" + URL_PARAM_RADIUS +
	"&alt=json";
	
	private static final String REVERSE_GEOCODE_URL = BASE_URL +
	"location/v2/reversegeocode?" +
	"access_token=" + URL_PARAM_ACCESS_TOKEN +
	"&lat=" + URL_PARAM_LATITUDE +
	"&lng=" + URL_PARAM_LONGITUDE +
	"&alt=json";
	
	private static IntelWebService instance = new IntelWebService();
	
	private UserAuth userAuth;
	private AuthData authData;
	private HashMap<String, String> defaultHeaderParams;
	private DecimalFormat geoCoordinatesDecimalFormat;
	
	public IntelWebService() {
		defaultHeaderParams = new HashMap<String, String>();
		defaultHeaderParams.put("Accept", "application/json");
		
		geoCoordinatesDecimalFormat = new DecimalFormat("#.######");
	}
	
	public static IntelWebService getInstance() {
		return instance;
	}

	public void getOauth20Token(final OnResponseListener<AuthData> responseListener) {
		AsyncTask<String, Void, AuthData> getTokenTask = new AsyncTask<String, Void, AuthData>(){
			@Override
			protected AuthData doInBackground(String... params) {
				try {
					com.smartIntern.notUsed.HttpConnectionUtil.HttpResponse response = HttpConnectionUtil.httpPost(params[0], defaultHeaderParams, null);
					if (response.getResponseCode() == HttpURLConnection.HTTP_OK) {
						JSONObject accessTokenObject = new JSONObject(response.getResponseData()).getJSONObject("OAuth20").getJSONObject("access_token");
						AuthData authData = new AuthData();
						authData.readFromJson(accessTokenObject);
						return authData;
					} else {
						// TODO - handle error
						return null;
					}
				} catch (JSONException e) {
					// TODO - handle error
					Log.w(IntelWebService.class.getSimpleName(), e);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.w(IntelWebService.class.getSimpleName(), e);
				}
				return null;
			}

			@Override
			protected void onPostExecute(AuthData result) {
				if (result != null) {
					authData = result;
					responseListener.onSuccess(result);
				} else {
					responseListener.onError();
				}
				super.onPostExecute(result);
			}
		};
		getTokenTask.execute(OAUTH20_URL);
	}
	
	public void getAccessToken(final OnResponseListener<UserAuth> responseListener) {
		//AsyncTask makes something behind the main thread
		//The following initialization creates a constructor for UserAuth
		AsyncTask<String, Void, UserAuth> getTokenTask = new AsyncTask<String, Void, UserAuth>(){
			@Override
			protected UserAuth doInBackground(String... params) {
				try {
					HttpResponse response = HttpConnectionUtil.httpPost(params[0], defaultHeaderParams, null);
					if (response.getResponseCode() == HttpURLConnection.HTTP_OK) {
						JSONObject accessTokenObject = new JSONObject(response.getResponseData()).getJSONObject("UserAuth").getJSONObject("access_token");
						UserAuth userAuth = new UserAuth();
						userAuth.readFromJson(accessTokenObject);
						return userAuth;
					} else {
						// TODO - handle error
						return null;
					}
				} catch (JSONException e) {
					// TODO - handle error
					Log.w(IntelWebService.class.getSimpleName(), e);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.w(IntelWebService.class.getSimpleName(), e);
				}
				return null;
			}

			//I have no idea what this do
			@Override
			protected void onPostExecute(UserAuth result) {
				if (result != null) {
					userAuth = result;
					responseListener.onSuccess(result);
				} else {
					responseListener.onError();
				}
				super.onPostExecute(result);
			}
		};
		//Magic works! :D 
		//No seriously, this makes the authentication I guess
		getTokenTask.execute(USERAUTH);
	}
	
	public void getPOICategories(final OnResponseListener<List<POICategory>> responseListener) {
		AsyncTask<String, Void, List<POICategory>> getTokenTask = new AsyncTask<String, Void, List<POICategory>>(){
			@Override
			protected List<POICategory> doInBackground(String... params) {
				try {
					HttpResponse response = HttpConnectionUtil.httpGet(params[0], defaultHeaderParams);
					if (response.getResponseCode() == HttpURLConnection.HTTP_OK) {
						List<POICategory> categories = new ArrayList<POICategory>();
						JSONArray categoriesJsonArray = new JSONObject(response.getResponseData()).getJSONObject("data").getJSONArray("items");
						for (int i = 0; i < categoriesJsonArray.length(); i++) {
							POICategory poiCategory = new POICategory();
							poiCategory.readFromJson(categoriesJsonArray.getJSONObject(i));
							categories.add(poiCategory);
						}
						return categories;
					} else {
						// TODO - handle error
						return null;
					}
				} catch (JSONException e) {
					// TODO - handle error
					Log.w(IntelWebService.class.getSimpleName(), e);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.w(IntelWebService.class.getSimpleName(), e);
				}
				return null;
			}

			@Override
			protected void onPostExecute(List<POICategory> result) {
				if (result != null) {
					responseListener.onSuccess(result);
				} else {
					responseListener.onError();
				}
				super.onPostExecute(result);
			}
		};
		getTokenTask.execute(POI_CATEGORIES_URL.replace(URL_PARAM_ACCESS_TOKEN, authData.getToken()));
	}
	
	public void getNearByPOIs(String category, double latitude, double longitude, String radius,
			final OnResponseListener<List<POILocation>> responseListener) {
		AsyncTask<String, Void, List<POILocation>> getTokenTask = new AsyncTask<String, Void, List<POILocation>>(){
			@Override
			protected List<POILocation> doInBackground(String... params) {
				try {
					HttpResponse response = HttpConnectionUtil.httpGet(params[0], defaultHeaderParams);
					if (response.getResponseCode() == HttpURLConnection.HTTP_OK) {
						List<POILocation> locations = new ArrayList<POILocation>();
						JSONArray locationsJsonArray = new JSONObject(response.getResponseData()).getJSONObject("data").getJSONArray("items");
						for (int i = 0; i < locationsJsonArray.length(); i++) {
							POILocation location = new POILocation();
							location.readFromJson(locationsJsonArray.getJSONObject(i));
							locations.add(location);
						}
						return locations;
					} else 
					{
						return null;
					}
				} catch (JSONException e) {
					// TODO - handle error
					Log.w(IntelWebService.class.getSimpleName(), e);
				} catch (IOException e) {
					
					Log.w(IntelWebService.class.getSimpleName(), e);
				}
				return null;
			}

			@Override
			protected void onPostExecute(List<POILocation> result) {
				if (result != null) {
					responseListener.onSuccess(result);
				} else {
					responseListener.onError();
				}
				super.onPostExecute(result);
			}
		};
		getTokenTask.execute(POIS_BY_CATEGORIE_URL
				.replace(URL_PARAM_ACCESS_TOKEN, authData.getToken())
				.replace(URL_PARAM_POI_CATEGORY, category)
				.replace(URL_PARAM_LATITUDE, geoCoordinatesDecimalFormat.format(latitude))
				.replace(URL_PARAM_LONGITUDE, geoCoordinatesDecimalFormat.format(longitude))
				.replace(URL_PARAM_RADIUS, radius)
				);
	}
	
	public static interface OnResponseListener<ResponseType> {
		public void onSuccess(ResponseType response);
		public void onError();
	}	
}
