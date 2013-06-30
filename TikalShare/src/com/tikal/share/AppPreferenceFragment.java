package com.tikal.share;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class AppPreferenceFragment  extends PreferenceActivity{


	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.layout.pref);
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String string = sharedPreferences.getString("userName", "");
        
        
    }

}
