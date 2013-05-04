package com.smartintern.saveroute;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eventplanning.R;

public class EnterRoute extends Activity{
	
	private ListView mListView;
	private TextView distanceText;
	private TextView timeText;
	private Context cnt;
	private int poz;
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.saved_route_route);
		
		mListView = (ListView) findViewById(android.R.id.list);
		mListView.setEmptyView(findViewById(android.R.id.empty));

		distanceText = (TextView) findViewById(R.id.saved_route_distance);
		timeText = (TextView) findViewById(R.id.saved_route_time);	
		cnt = this;
		poz = 0;
		int i, j;
		int whatRoute = -1;
		
		Bundle extras = getIntent().getExtras();
		if (extras != null){
			whatRoute = extras.getInt("whatRoute");
		}
		else {
			return;
		}

		String content = SavedRouteVector.getInstance().savedRoute.get(whatRoute);
		ArrayList<String> mItems = new ArrayList<String>();
		String distance = "";			

		for (i = poz;content.charAt(i) != '&';i++)
		{
			distance += content.charAt(i);
		}
		distanceText.setText(distance);
		poz = i+1;
		distance = "";			
		for (i = poz;content.charAt(i) != '&';i++)
		{
			distance += content.charAt(i);
		}
		timeText.setText(distance);
		poz = i+1;
		String aux;
		while (i<content.length()){
			aux = "";
			for (j = i; content.charAt(j) != '&'; j++){
				aux += content.charAt(j);
			}
			i = j+1;
			mItems.add(aux);
		}
		
		EnterRouteAdapter adapter = new EnterRouteAdapter(getApplicationContext(), mItems);
		mListView.setAdapter(adapter);
	}
}
