package com.smartIntern.eventplanning;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListBox extends ListView
{
	ArrayList<String> elements;
	Context context;
	ArrayAdapter<String> myarrayAdapter;
	
	public ListBox(Context context , AttributeSet str)
	{
		super(context ,str);
		this.context = context;
		elements = new ArrayList<String>();	
		myarrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, elements);
		this.setAdapter(myarrayAdapter);
	}
	
	public void InsertItem(String item)
	{
		elements.add(item);
		myarrayAdapter.notifyDataSetChanged();
	}
	
	public void RemoveItem(int position)
	{
		elements.remove(position);
		myarrayAdapter.notifyDataSetChanged();
	}
	
	public void Clear()
	{
		elements.clear();
		myarrayAdapter.notifyDataSetChanged();
	}
	
	
}
