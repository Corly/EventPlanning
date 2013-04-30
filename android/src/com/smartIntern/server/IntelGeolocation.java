package com.smartIntern.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public abstract class IntelGeolocation
{
	public static String parseXML(String XML)
	{
		int token_startposition = XML.indexOf("<token>") + "<token>".length();
		int token_endposition = XML.indexOf("</token>");
		return XML.substring(token_startposition,token_endposition);
	}
	
	public static String GetAccessToken()
	{
		try
		{
			HttpClient httpclient = new DefaultHttpClient();
			
			String parameters = "client_id={client_id}&client_secret={client_secret}&grant_type=client_credentials&scope=location:basic&language1=fr&language2=en";
		    String linkRequest = "https://api.intel.com:8081/oauth20/token";
		    
			
		    parameters = parameters.replace("{client_id}", "4247c4dce5a5b0c58e29b2a830f8c768");
		    parameters = parameters.replace("{client_secret}", "de779a6a10dd3443");
		    
		    HttpResponse response = httpclient.execute(new HttpPost(linkRequest +  "?" + parameters));
		    StatusLine statusLine = response.getStatusLine();
		    
		    if(statusLine.getStatusCode() == HttpStatus.SC_OK)
		    {
		        ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        String responseString = out.toString();	
		        Log.d("Test", responseString);
		        //add the token to shared information
		        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(null);
		        SharedPreferences.Editor editor = mPreferences.edit();
		        editor.putString("token", parseXML(responseString));
		        editor.commit();
		        
		        return parseXML(responseString);		        
		    }
		    
		}
		catch (IOException e) 
		{
	        Log.e("Test", e.toString());
	        return "";
	    }
		return "";   	    
	}
}
