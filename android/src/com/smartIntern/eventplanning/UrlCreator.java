package com.smartIntern.eventplanning;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import android.content.Context;

import com.smartIntern.server.ApiHandler;
import com.smartIntern.server.ServerResponse;

public class UrlCreator
{
	ArrayList<String> parameters = new ArrayList<String>();
	private String baseURL = "https://api.intel.com:8081/location/v2/";
	private String requierment;
	private Context context;
	UrlCreator(Context context)
	{
		this.context = context;
	}
	
	public void setRequierment(String str)
	{
		requierment = str;
	}
	
	public void addArgument(String parameter , String value)
	{
		parameters.add(parameter + "=" + value);
	}
	
	public ServerResponse execute() throws ClientProtocolException, IOException
	{
		String finalURL = baseURL + requierment + "?";
		int size = parameters.size();
		for (int i  = 0;i<size -1;i++)
			finalURL += parameters.get(i) + "&";
		finalURL += parameters.get(size-1);		
		return ApiHandler.getArray(finalURL, context);
	}

}
