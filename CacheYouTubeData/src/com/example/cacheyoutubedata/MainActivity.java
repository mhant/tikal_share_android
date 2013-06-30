package com.example.cacheyoutubedata;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	private YouTubeDataCacher myYTDC = null;
	private PreferencesDataCacheStore myPDCS = null;
	private static String myCacheID = "my_youtube_cache";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		myPDCS = new PreferencesDataCacheStore(this);
		myYTDC = new YouTubeDataCacher(myPDCS);
		
		myYTDC.cacheThis(myCacheID, new String[] {"abc", "jjj"});
		
		Object r = myYTDC.unchacheThis(myCacheID);
		Log.d("MAIN", r.getClass().getName());
		
		r = myYTDC.unchacheThis("lkjsdlfks");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
