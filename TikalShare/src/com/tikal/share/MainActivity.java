package com.tikal.share;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.cacheyoutubedata.PreferencesDataCacheStore;
import com.example.cacheyoutubedata.YouTubeDataCacher;
import com.tikal.share.youtube.LookupChannel;
import com.tikal.share.youtube.YoutubeData;
import com.tikal.share.youtube.YoutubePlaylist;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	private PlayListPagerAdapter mPlaylistPagerAdapter;
	private ViewPager mViewPager;
	private String userName;
	private boolean downloadThumbnail;

	private YouTubeDataCacher myYTDC = null;
	private PreferencesDataCacheStore myPDCS = null;
	// private static String myCacheID = "my_youtube_cache";

	private int playListCount = 3;
	public static final String DATA_UPDATE = "data_update";
	private IntentFilter filter = new IntentFilter(DATA_UPDATE);
	
	private MenuItem menuSpinner;

	private YoutubeAsyncTask youtubeAsyncTask = new YoutubeAsyncTask();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create cache data store
		myPDCS = new PreferencesDataCacheStore(this);
		myYTDC = new YouTubeDataCacher(myPDCS);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//data will be loaded after menu has been initialized
	}

	class YoutubeAsyncTask extends AsyncTask<Void, Void, YoutubeData> {
		private AtomicBoolean gettingData = new AtomicBoolean(false);

		public void doExecute() {
			if (!gettingData.get()) {
				gettingData.set(true);
				Toast.makeText(getApplicationContext(), "Refreshing data in background", Toast.LENGTH_LONG).show();
				execute();
			} else {
				Toast.makeText(getApplicationContext(), "Already getting data", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Start Animation
			menuSpinner.setVisible(true);
			Animation rotation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
			rotation.setRepeatCount(Animation.INFINITE);
			View animateView = menuSpinner.getActionView();
			animateView.findViewById(R.id.loadingSpinner).startAnimation(rotation);
			animateView.setVisibility(View.VISIBLE);
		}

		protected void onPostExecute(YoutubeData result) {
			// Stop Animation
			menuSpinner.setVisible(false);
			View animateView = menuSpinner.getActionView();
			animateView.findViewById(R.id.loadingSpinner).clearAnimation();
			animateView.setVisibility(View.GONE);
			// Parse the data
			updateGUIWithData(result);
			gettingData.set(false);
		}

		@Override
		protected YoutubeData doInBackground(Void... params) {
			LookupChannel lookup = new LookupChannel(false);
			
			YoutubeData youtubeData = lookup.getYoutubeData(userName, downloadThumbnail);
			myYTDC.saveToFile(youtubeData, MainActivity.this);
			// myYTDC.cacheThis(myCacheID, youtubeData);
			return youtubeData;
		}

	}

	private void updateGUIWithData(YoutubeData result) {
		if (result != null) {
			playListCount = result.getPlayList().size();
			onUpdateRecieve(getActionBar(), result.getPlayList());
			addActionbarTabs(getActionBar());
		}
	}

	/**
	 * @param actionBar
	 * @param result
	 */
	private void onUpdateRecieve(final ActionBar actionBar, List<YoutubePlaylist> result) {
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.

		mPlaylistPagerAdapter = new PlayListPagerAdapter(getSupportFragmentManager(), result);

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mPlaylistPagerAdapter);
		mPlaylistPagerAdapter.swapItems(result);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
	}

	/**
	 * @param actionBar
	 */
	private void addActionbarTabs(final ActionBar actionBar) {
		actionBar.removeAllTabs();

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mPlaylistPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab().setText(mPlaylistPagerAdapter.getPageTitle(i)).setTabListener(this));
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(datareceiver, filter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(datareceiver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menuSpinner = menu.findItem( R.id.menu_spinner);
		menuSpinner.setVisible(false);
		//calling data sync after options menu is created to deal 
		//with loading indications
		YoutubeData youtubeData = myYTDC.loadFromFile(MainActivity.this);
		updateGUIWithData(youtubeData);
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		userName = "androiddev101";// enter channel name here
		downloadThumbnail = sharedPreferences.getBoolean("downloadThumbnail", true);
		
		boolean update = true;
		if (youtubeData != null) {
			String temp = sharedPreferences.getString("cacheTimeout", "24");
			int cacheTimeout = Integer.parseInt(temp);

			Date now = new Date();
			update = now.getTime() - youtubeData.getUpdated().getTime() > cacheTimeout * 60 * 60 * 1000; 
		}
		if (update) {
			youtubeAsyncTask.doExecute();
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.action_refresh:
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
			userName = "androiddev101";// enter channel name here
			downloadThumbnail = sharedPreferences.getBoolean("downloadThumbnail", true);
			youtubeAsyncTask.doExecute();
			// Add tab action
			/*
			 * PLAYLIST_COUNT = 4; mViewPager.setAdapter(null);
			 * mPlaylistPagerAdapter = new
			 * PlayListPagerAdapter(getSupportFragmentManager());
			 * mViewPager.setAdapter(mPlaylistPagerAdapter); //
			 * mPlaylistPagerAdapter.notifyDataSetChanged();
			 * mViewPager.invalidate(); addActionbarTabs(getSupportActionBar());
			 */
			break;
		case R.id.action_settings:
			startActivity(new Intent(this, AppPreferenceFragment.class));
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class PlayListPagerAdapter extends FragmentPagerAdapter {

		private List<YoutubePlaylist> list;

		public PlayListPagerAdapter(FragmentManager fm, List<YoutubePlaylist> list) {
			super(fm);
			this.list = list;
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new MyListFragment(list.get(position));
			return fragment;
		}

		public void swapItems(List<YoutubePlaylist> items) {
			this.list = items;
			notifyDataSetChanged();
		}

		@Override
		public long getItemId(int position) {

			return super.getItemId(position);

			/*
			 * switch (position) { case 0: case 1: case 2: return
			 * super.getItemId(position); case 3: return 4; }
			 */
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public int getCount() {
			return playListCount;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return list.get(position).getTitle();
		}
	}

	private BroadcastReceiver datareceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			Toast.makeText(getApplicationContext(), "received", Toast.LENGTH_SHORT);
			// onUpdateRecieve(getSupportActionBar());
			addActionbarTabs(getActionBar());

		}
	};

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

}
