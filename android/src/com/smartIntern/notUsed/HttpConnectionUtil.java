package com.smartIntern.notUsed;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.util.CharArrayBuffer;

import android.util.Log;

public class HttpConnectionUtil {
	
	private static final int DEFAULT_CHAR_BUFFER_SIZE = 1024 * 2;
	
	public static HttpResponse httpGet(String url) throws IOException {
		return httpGet(url, null);
	}

	public static HttpResponse httpGet(String url, HashMap<String, String> headerParams) throws IOException {
		Log.d(HttpConnectionUtil.class.getSimpleName(), "httpGet: " + url);
		HttpURLConnection connection = null;
		try {
			URL connUrl = new URL(url);
			connection = (HttpURLConnection)connUrl.openConnection();
			if (headerParams != null) {
				for (String name : headerParams.keySet()) {
					connection.setRequestProperty(name, headerParams.get(name));
				}
			}
			int statusCode = connection.getResponseCode();
			String responseData = readStreamData(connection.getInputStream());			
			HttpResponse response = new HttpResponse();
			response.responseCode = statusCode;
			response.responseData = responseData;
			return response;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	
	public static HttpResponse httpPost(String url, List<NameValuePair> postParams) throws IOException {
		return httpPost(url, null, postParams);
	}
	
	public static HttpResponse httpPost(String url, HashMap<String, String> headerParams, List<NameValuePair> postParams) throws IOException {
		Log.d(HttpConnectionUtil.class.getSimpleName(), "httpPost: " + url);
		HttpURLConnection connection = null;
		try {
			URL connUrl = new URL(url);
			connection = (HttpURLConnection)connUrl.openConnection();
			
			if (headerParams != null) {
				for (String name : headerParams.keySet()) {
					connection.setRequestProperty(name, headerParams.get(name));
				}
			}
			
			// setup http POST, by default settings are for GET request
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			
			if (headerParams != null) {
				for (String name : headerParams.keySet()) {
					connection.setRequestProperty(name, headerParams.get(name));
				}
			}
			
		    if (postParams != null) {
		    	OutputStream os = connection.getOutputStream();
		    	UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParams);
		    	entity.writeTo(os);
		    	os.close();
		    }
			
			int statusCode = connection.getResponseCode();
			String responseData = readStreamData(connection.getInputStream());			
			HttpResponse response = new HttpResponse();
			response.responseCode = statusCode;
			response.responseData = responseData;
			return response;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	
	private static final String readStreamData(InputStream in) throws IOException {
		Reader reader = new InputStreamReader(in, "UTF-8");
		CharArrayBuffer buffer = new CharArrayBuffer(1024 * 10);
		char[] tmpCharArray = new char[DEFAULT_CHAR_BUFFER_SIZE];
		int n = 0;
		while (-1 != (n = reader.read(tmpCharArray))) {
			buffer.append(tmpCharArray, 0, n);
		}
		return buffer.toString();
	}
	
	public static class HttpResponse {
		
		private int responseCode;
		private String responseData;
		
		public int getResponseCode() {
			return responseCode;
		}
		public String getResponseData() {
			return responseData;
		}
	}
}
