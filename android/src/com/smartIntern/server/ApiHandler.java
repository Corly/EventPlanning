package com.smartIntern.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import android.net.Uri;
import android.util.Log;

public class ApiHandler 
{
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
	public static String getHTTP(Uri url, Context context) throws ClientProtocolException, IOException 
	{

		StringBuilder responseBuilder = new StringBuilder();

		/** Send request */
		DefaultHttpClient httpclient = new DefaultHttpClient();
		Log.d("TEST","0");
		HttpGet request = new HttpGet(url.toString());
		Log.d("TEST","1");
		HttpResponse response = httpclient.execute(request);
		Log.d("TEST","2");
		
		
		InputStream data = response.getEntity().getContent();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(data));
		String responeLine;
		while ((responeLine = bufferedReader.readLine()) != null) 
		{
			responseBuilder.append(responeLine);
		}
		return responseBuilder.toString();
	}
	/**
	 * Generic HTTP GET data request
	 * 
	 * @param request
	 * @return the server's reply @see ServerResponse
	 */
	public static ServerResponse get(Uri req, Context context) 
	{
		JSONObject jObject;
		String info;
		ServerResponse result = new ServerResponse();
		try 
		{
			info = getHTTP(req, context);
		} catch (Exception e1) 
		{
			result.setStatus(false);
			result.setError("Server communication error.");
			return result;
		}
		try 
		{
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
	public static ServerResponse getArray(Uri req, Context context) 
	{
		ServerResponse result = new ServerResponse();

		try 
		{
			result = get(req, context);
		} catch (Exception e1) 
		{
			result.setStatus(false);
			result.setError("Server communication error.");
			return result;
		}		

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
	
	public static ArrayList<String> ParseJSON(ServerResponse resp)
	{
		ArrayList<String> list = new ArrayList<String>();
		try
		{
			for (int i = 0;i<resp.getArrayData().length();i++)
			{
				JSONObject local  = new JSONObject(resp.getArrayData().getString(i));
				StringBuilder t1 = new StringBuilder();
				t1.append("Name : " + local.getString("name")+"\n");
				t1.append("Latitude : " + local.getString("latitude")+"\n");
				t1.append("Longitude : " + local.getString("longitude")+"\n");
				t1.append("City : " + local.getString("city") + "\n");
				t1.append("Phone : " + local.getString("phone") + "\n");
				t1.append("Address : " + local.getString("streetAddress"));
				list.add(t1.toString());				
			}
		}
		catch (Exception er)
		{
			
		}
		return list;
	}

}
