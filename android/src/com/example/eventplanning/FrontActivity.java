package com.example.eventplanning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class FrontActivity extends Activity
{

	OnTouchListener touchListener = new OnTouchListener()
	{

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1)
		{
			switch (arg1.getAction())
			{
				case MotionEvent.ACTION_DOWN:
				{
					arg0.setBackgroundResource(R.drawable.button_click);
					return false;
				}
				case MotionEvent.ACTION_UP:
				{
					arg0.setBackgroundResource(R.drawable.button);
					return false;
				}
			}
			return false;
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.front);
		
		Button btnNormal = (Button)findViewById(R.id.btn_find);
		Button btnCity = (Button)findViewById(R.id.btn_city);
		
		btnNormal.setOnTouchListener(touchListener);
		btnCity.setOnTouchListener(touchListener);
		
		btnNormal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplication(), MainActivity.class);
				startActivity(i);
			}
		});
	}
}
