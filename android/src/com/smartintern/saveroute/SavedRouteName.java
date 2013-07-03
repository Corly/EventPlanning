package com.smartIntern.saveroute;

import java.util.ArrayList;

public final class SavedRouteName {
	private static final SavedRouteName instance = new SavedRouteName();
	
	public ArrayList<String> savedRouteName = new ArrayList<String>();
	
	public static SavedRouteName getInstance() {
		return instance;
	}
	
	private SavedRouteName()
	{
	}
}
