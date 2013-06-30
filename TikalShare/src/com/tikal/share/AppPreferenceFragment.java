package com.tikal.share;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.widget.EditText;

public class AppPreferenceFragment  extends PreferenceActivity implements OnPreferenceChangeListener{


	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.layout.pref);
        
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String string = sharedPreferences.getString("userName", "");
        
        
    }

	@Override
	public boolean onPreferenceChange(Preference arg0, Object arg1) {
        EditText editTextPreference = (EditText)findViewById(R.id.userNameX);
        editTextPreference.setText("ttt");

		return false;
	}
}
