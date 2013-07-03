package com.smartIntern.saveroute;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eventplanning.R;

public class EnterRoute extends Activity{
	
	private ListView mListView;
	private TextView distanceText;
	private TextView timeText;
	private TextView tagText;
	private Context cnt = this;
	private int poz;
	private ImageView imageViewer;
	private Button showSomething;
	private boolean whichisshown = false;
	
	OnTouchListener touchListener = new OnTouchListener()
	{
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1)
		{
			switch (arg1.getAction())
			{
				case MotionEvent.ACTION_DOWN:
				{
					arg0.setBackgroundResource(R.drawable.routebuttonclick);
					return false;
				}
				case MotionEvent.ACTION_UP:
				{
					arg0.setBackgroundResource(R.drawable.routebutton);
					return false;
				}
			}
			return false;
		}
	};
	
	private void Show()
	{
		if (!whichisshown)
		{
			imageViewer.setVisibility(View.INVISIBLE);
			mListView.setVisibility(View.VISIBLE);
			Log.d("TEST","Nebun?");
			showSomething.setText("Show map");
		}
		else
		{
			mListView.setVisibility(View.INVISIBLE);
			imageViewer.setVisibility(View.VISIBLE);
			showSomething.setText("Show route");
		}
	}
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.saved_route_route);
		
		mListView = (ListView) findViewById(android.R.id.list);
		mListView.setEmptyView(findViewById(android.R.id.empty));

		distanceText = (TextView) findViewById(R.id.saved_route_distance);
		timeText = (TextView) findViewById(R.id.saved_route_time);	
		tagText = (TextView) findViewById(R.id.saved_route_tag);
		imageViewer = (ImageView)findViewById(R.id.imageView1);
		showSomething = (Button) findViewById(R.id.show_current);
		showSomething.setOnTouchListener(touchListener);
		
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

		tagText.setText(SavedRouteName.getInstance().savedRouteName.get(whatRoute));
		imageViewer.setVisibility(View.INVISIBLE);
		imageViewer.setImageBitmap(SavedRouteVector.getInstance().savedImage.get(whatRoute));
		
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
		i=i+1;
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
	
	public void Show_Click(View v)
	{
		whichisshown = !whichisshown;
		Show();
	}
}
