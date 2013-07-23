package com.tikal.share;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class AboutPrefFragment extends PreferenceFragment {
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.layout.about);
    }

}
