package com.example.eventplanning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FrontActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.front);
		
		Button btnNormal = (Button)findViewById(R.id.btn_find);
		Button btnCity = (Button)findViewById(R.id.btn_city);
		
		btnNormal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplication(), MainActivity.class);
				startActivity(i);
			}
		});
	}
}
