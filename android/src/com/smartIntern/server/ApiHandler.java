package com.smartIntern.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class ApiHandler 
{
	private static final String BASE_URL = "https://api.intel.com:8081/";	
	private static final String URL_PARAM_ACCESS_TOKEN = "{access_token}";
	private static final String URL_PARAM_POI_CATEGORY = "{category}";
	private static final String URL_PARAM_LATITUDE = "{latitude}";
	private static final String URL_PARAM_LONGITUDE = "{longitude}";
	private static final String URL_PARAM_RADIUS = "{radius}";

	/**
	 * Gets a general HTTP string.
	 * 
	 * @param req
	 *            The url to get it from.
	 * @return A String containing the data.
	 * @throws OAuthCommunicationException
	 * @throws OAuthExpectationFailedException
	 * @throws OAuthMessageSignerException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String getHTTP(String url, Context context) throws ClientProtocolException, IOException 
	{

		StringBuilder responseBuilder = new StringBuilder();

		/*SharedPreferences mAppInfo = PreferenceManager
				.getDefaultSharedPreferences(context);
		
		final String token = mAppInfo.getString("token", null);*/

		/** Send request */
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
<<<<<<< HEAD
		
		//not sure if casting will result to something that we want
		
		HttpResponse response = httpclient.execute(request);
=======
		Log.d("CCCCCCCCCCCCCCC",request.toString());
		//not sure if casting will result to something that we want
		HttpResponse response = httpclient.execute(request);
		//Log.d("DDDDDDDDDDDDDDDDDDDDDD",response.toString());
>>>>>>> Nervi -> infinit
		InputStream data = response.getEntity().getContent();
		//Log.d("MMMMMMMMMMMDAAAAAAAAA", data.toString());
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(data));
		String responeLine;
		while ((responeLine = bufferedReader.readLine()) != null) 
		{
			responseBuilder.append(responeLine);
		}
<<<<<<< HEAD
=======
		
		//Log.d("BBBBBBBBBBBBBBBBBBBBBBB",responseBuilder.toString());

>>>>>>> Nervi -> infinit
		return responseBuilder.toString();
	}


	/**
	 * Generic HTTP GET data request
	 * 
	 * @param request
	 * @return the server's reply @see ServerResponse
	 */
	public static ServerResponse get(String req, Context context) 
	{
		JSONObject jObject;
		String info;
		ServerResponse result = new ServerResponse();
<<<<<<< HEAD

		try 
		{
			info = getHTTP(req, context);
		} catch (Exception e1) 
		{
=======
		
		try {
			info = getHTTP(req, context);
		} catch (Exception e1) {
			Log.d("CUMBAIAAAAAAAA","SHALALALA");
>>>>>>> Nervi -> infinit
			result.setStatus(false);
			result.setError("Server communication error.");
			return result;
		}

<<<<<<< HEAD
		try 
		{
=======
		Log.d("CUMBAIAAAAAAAA","SHALALALA");
		try {
>>>>>>> Nervi -> infinit
			jObject = new JSONObject(info);
			result.setData(jObject);
		result.setStatus(true);
		} catch (JSONException e) 
		{
			result.setStatus(false);
			result.setError("Server response format error.");
			return result;
		}

		return result;
	}
	
	/**
	 * Generic HTTP GET data request
	 * 
	 * @param req The request
	 * @return The server's reply @see ServerResponse
	 */
	public static ServerResponse getArray(String req, Context context) {
<<<<<<< HEAD
=======
	/*	//JSONArray jObject;
		//String info;
		ServerResponse result = new ServerResponse();

		try 
		{
			result = get(req, context);
		} catch (Exception e1) {
			result.setStatus(false);
			result.setError("Server communication error.");
			return result;
		}
		
		Log.d("mama matii de result", result.getData().toString());
		
		try 
		{
			JSONObject m = new JSONObject(result.getData().getString("data"));
			JSONArray array = m.getJSONArray("items");
			result.setArrayData(array);
		} catch (JSONException e) 
		{
			result.setStatus(false);
			result.setError("Server response format error.");
			return result;
		}

		return result;*/
		JSONArray jArr;
		JSONObject jObj;
		String info;
>>>>>>> Nervi -> infinit
		ServerResponse result = new ServerResponse();

		try 
		{
			result = get(req, context);
		} catch (Exception e1) {
			Log.d("AAAAAAAAAAAAAAAAAAAAAAAAAAaa","dsa");
			result.setStatus(false);
			result.setError("Server communication error.");
			return result;
		}
		
<<<<<<< HEAD
		try 
		{
			JSONObject m = new JSONObject(result.getData().getString("data"));
			JSONArray array = m.getJSONArray("items");
			result.setArrayData(array);
		} catch (JSONException e) 
		{
=======
		Log.d("info", info);

		try {
			jObj = new JSONObject(info);
			jArr = new JSONArray(jObj.getString("items"));
			result.setArrayData(jArr);
			result.setStatus(true);
		} catch (JSONException e) {
>>>>>>> Nervi -> infinit
			result.setStatus(false);
			result.setError("Server response format error.");
			return result;
		}

		return result;
	}
	
	/**
	 * Sends a POST with the specified data.
	 * 
	 * @param host
	 *            The host to send the POST to
	 * @param data
	 *            The data to send.
	 * @return The server's response to the POST.
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static HttpResponse postHttp(String host, List<NameValuePair> data, Context context) throws ClientProtocolException, IOException 
	{
		String url = host;
		HttpPost httpost = new HttpPost(url);
		HttpResponse res = null;
		DefaultHttpClient mHttpClient = new DefaultHttpClient();

		/** Send post */
		httpost.setEntity(new UrlEncodedFormEntity(data));
		res = mHttpClient.execute(httpost);

		return res;
	}
	
	public static ServerResponse sendPost(String host,
			List<NameValuePair> data, Context context) 
	{
		HttpResponse httpRes;
		ServerResponse res = new ServerResponse();

		/** Send post */
		try {
			httpRes = postHttp(host, data, context);
		} catch (Exception e) {
			res.setStatus(false);
			res.setError("Server communication error.");
			return res;
		}

		/** Read post result */
		HttpEntity entity = httpRes.getEntity();
		if (entity != null) {
			InputStream instream;
			try {
				instream = entity.getContent();
				String result = convertStreamToString(instream);
				instream.close();
				JSONObject server = new JSONObject(result);
				res.setStatus(server.getBoolean("success"));
				if (res.getStatus() == false)
					res.setError(server.getString("error"));
			} catch (Exception e) {
				res.setStatus(false);
				res.setError("Invalid server response.");
			}
		}
		
		return res;
	}
	
	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return sb.toString();
	}

}
