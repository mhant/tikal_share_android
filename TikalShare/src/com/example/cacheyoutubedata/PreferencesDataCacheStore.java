package com.example.cacheyoutubedata;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

public class PreferencesDataCacheStore implements AbstractDataCacheStore {
	public static final String PREFS_NAME = "PreferencesDataCacheStore_PrefsFile";
	
	private Activity myActivity;
	
	public PreferencesDataCacheStore(Activity myActivity){
		this.myActivity = myActivity;
	}
	
	@Override
	public void store(String cache_id, String json, String class_name) {
		Log.d("PDCS", "Storing: "+cache_id+":"+json);
		 
		 SharedPreferences settings = myActivity.getSharedPreferences(PREFS_NAME, 0);
	     SharedPreferences.Editor editor = settings.edit();
	     
	     editor.putString(cache_id+"_json", json);
	     editor.putString(cache_id+"_class", class_name);

	     editor.commit();
	     
	     Log.d("PDCS", "Stored.");
	}

	@Override
	public String[] retrieve(String cache_id) {
		// TODO Auto-generated method stub
		Log.d("PDCS", "Retrieving: "+cache_id);
		
		SharedPreferences settings = myActivity.getSharedPreferences(PREFS_NAME, 0);
	    String json = settings.getString(cache_id+"_json","");
	    String class_name = settings.getString(cache_id+"_class","");;
	    
	    Log.d("PDCS", "Retrieved.");
	    return new String[] {json, class_name};
	}

}
