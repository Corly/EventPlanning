package com.example.eventplanning;


import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.smartIntern.GetPOI.POI;


public class MainActivity extends Activity 
{
	
	private Button btn;
	private GlobalPositioning GP;
	private EditText etext;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn = (Button)findViewById(R.id.find);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				etext = (EditText) findViewById(R.id.edit_radius);
				if (etext.getText().toString().length() == 0){
					Toast.makeText(getApplicationContext() , "Please enter a radius!" , Toast.LENGTH_SHORT).show();
				}
				else {
					Location location = GP.getLocation();
					if (location == null) return;
					Spinner spinner = (Spinner)findViewById(R.id.spinner1);
					String category = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
					String radius = etext.getText().toString();
					Intent i = new Intent(getApplicationContext(), POI.class);
					i.putExtra("category", category );
					i.putExtra("radius", radius);
					startActivity(i);
					//ghdghdsjgjksdhj
				}
				
			}
		});
		
		SearchMyLocation();
	}
	
	void SearchMyLocation()
	{
		Location location;
		while(true)
		{
			GP = new GlobalPositioning(this);
			location = GP.getLocation();
			if (location != null) break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
