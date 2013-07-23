package com.tikal.share;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class AppPreferenceFragment  extends PreferenceActivity{

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	
	/**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.header_preferences, target);
    }
}
