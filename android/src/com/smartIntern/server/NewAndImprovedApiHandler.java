package com.smartIntern.server;

import java.io.ByteArrayInputStream;
import java.io.File;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class NewAndImprovedApiHandler {

	public static String parseXML(String XML)
	{
		int token_startposition = XML.indexOf("<token>") + "<token>".length();
		int token_endposition = XML.indexOf("</token>");
		return XML.substring(token_startposition,token_endposition);
	}
	
	public static String GetAccessToken(){
		String parameters = "client_id={client_id}&client_secret={client_secret}&grant_type=client_credentials&scope=location:basic&language1=fr&language2=en";
	    String linkRequest = "/oauth20/token";
	    
	    parameters = parameters.replace("{client_id}", "4247c4dce5a5b0c58e29b2a830f8c768");
	    parameters = parameters.replace("{client_secret}", "de779a6a10dd3443");
	    
	    NewAndImprovedApiHelper.get(linkRequest + '?' + parameters, null, new JsonHttpResponseHandler(){
	    	@Override
	    	public void onSuccess(String response){
	    		System.out.println( "Succes");
	    	}
	    	
	    	@Override
			public void onFailure(Throwable e, String response){
				System.out.println("Failure");
			}
	    });
	    return "default return";
	}
	
	public static void GetPoi(){
		RequestParams params = new RequestParams();
		params.put("access_token", "f61b6457e628f3610558b686c0606917");
		params.put("lat", "44.43250");
		params.put("lng", "26.10389");
		params.put("radius", "2000"); 
		params.put("category", "poi_restaurants"); 
		params.put("num_results", "50");
				
		Log.d("Helo", "hello");
		
		NewAndImprovedApiHelper.get(
				"/location/v2/poi",
				params, new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(String response){
						System.out.println( "Succes");
					}
					
					@Override
					public void onFailure(Throwable e, String response){
						System.out.println("Failure");
					}
				});
	}
}
