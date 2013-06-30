
package com.tikal.share;

import java.util.List;
import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.CursorJoiner.Result;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.tikal.share.youtube.LookupChannel;
import com.tikal.share.youtube.YoutubePlaylist;

public class MainActivity extends SherlockFragmentActivity implements ActionBar.TabListener {

	PlayListPagerAdapter mPlaylistPagerAdapter;
	ViewPager mViewPager;

	private int PLAYLIST_COUNT = 3;
	public static final String DATA_UPDATE = "data_update";
	private IntentFilter filter = new IntentFilter(DATA_UPDATE);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// actionBar.

		new myAsyncTask().execute();
	}

	class myAsyncTask extends AsyncTask<Void, Void, List<YoutubePlaylist>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Start Animation
		}

		protected void onPostExecute(List<YoutubePlaylist> result) {
			// Stop Animation
			Toast.makeText(getApplicationContext(), "blal", 1).show();
			// Parse the data
			PLAYLIST_COUNT = result.size();
			onUpdateRecieve(getSupportActionBar(), result);
			addActionbarTabs(getSupportActionBar());
		}

		@Override
		protected List<YoutubePlaylist> doInBackground(Void... params) {
			LookupChannel lookup = new LookupChannel(false);
			List<YoutubePlaylist> list = lookup.getFullListByUser("androiddev101");
			return list;
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
			actionBar.addTab(
					actionBar.newTab()
							.setText(mPlaylistPagerAdapter.getPageTitle(i))
							.setTabListener(this));
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
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

			case R.id.action_refresh:
				// Add tab action
				/*
				 * PLAYLIST_COUNT = 4; mViewPager.setAdapter(null); mPlaylistPagerAdapter = new
				 * PlayListPagerAdapter(getSupportFragmentManager()); mViewPager.setAdapter(mPlaylistPagerAdapter); //
				 * mPlaylistPagerAdapter.notifyDataSetChanged(); mViewPager.invalidate();
				 * addActionbarTabs(getSupportActionBar());
				 */
				break;
			case R.id.action_settings:
				sendBroadcast(new Intent(DATA_UPDATE));
				Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the sections/tabs/pages.
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
			Fragment fragment = new ListFragment(list.get(position));
			return fragment;
		}

		@Override
		public long getItemId(int position) {

			return super.getItemId(position);

			/*
			 * switch (position) { case 0: case 1: case 2: return super.getItemId(position); case 3: return 4; }
			 */
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public int getCount() {
			return PLAYLIST_COUNT;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return list.get(position).getTitle();
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);
			TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

	@Override
	public void onTabSelected(Tab tab, android.support.v4.app.FragmentTransaction ft) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, android.support.v4.app.FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(Tab tab, android.support.v4.app.FragmentTransaction ft) {
	}

	private BroadcastReceiver datareceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			Toast.makeText(getApplicationContext(), "received", Toast.LENGTH_SHORT);
			// onUpdateRecieve(getSupportActionBar());
			addActionbarTabs(getSupportActionBar());

		}
	};

}
