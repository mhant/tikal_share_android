package com.tikal.share;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class PlayerActivity extends YouTubeBaseActivity implements OnInitializedListener {

	public static final String DEVELOPER_KEY = "YOUR_DEVELOPER_KEY_GOES_HERE";
	private MHPlaybackEventListener playerEventListener;
	private MHPlayerStateChangeListener playerStateChangeListener;
	private YouTubePlayer playa; //hollla
	private boolean playerLoaded;
	private int previousReqSeek;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);

		playerLoaded = false;
		previousReqSeek = 0;

		Button seekButton = (Button)findViewById(R.id.seek);
		seekButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startVideoAt(50000);

			}
		});

		YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
		youTubeView.initialize(DEVELOPER_KEY, this);  

		playerEventListener = new MHPlaybackEventListener();
		playerStateChangeListener = new MHPlayerStateChangeListener();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.player, menu);
		return true;
	}


	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1) {


	}

	@Override
	protected void onPause() {
		super.onPause();
		//might need to do some cleanup here
	}


	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
			boolean wasRestored) {
		if (!wasRestored) {
			playa = player;

			playa.cueVideo("b7pjiYr-Bpo");
			playa.setPlaybackEventListener(playerEventListener);
			playa.setPlayerStateChangeListener(playerStateChangeListener);
		}
	}

	private void startVideoAt(int milis){
		if(playerLoaded){
			previousReqSeek = 0;
			playa.seekToMillis(milis);
			playa.play();
		}
		else{
			previousReqSeek = milis;
		}
	}

	private class MHPlaybackEventListener implements PlaybackEventListener {

		@Override
		public void onBuffering(boolean arg0) {
			Log.d("PlayerStateChanged", "onBuffering");

		}

		@Override
		public void onPaused() {
			//call save here or in onStopped, seems that when one is called the other is
			Log.d("PlayerStateChanged", "onPaused");

		}

		@Override
		public void onPlaying() {
			Log.d("PlayerStateChanged", "onPlaying");

		}

		@Override
		public void onSeekTo(int arg0) {
			Log.d("PlayerStateChanged", "onSeekTo");

		}

		@Override
		public void onStopped() {
			Log.d("PlayerStateChanged", "onStopped");

		}
	}

	private class MHPlayerStateChangeListener implements PlayerStateChangeListener  {

		@Override
		public void onAdStarted() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onError(ErrorReason arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLoaded(String arg0) {
			playerLoaded = true;
			//in case previously requested seek seek to that point
			if(previousReqSeek > 0){
				startVideoAt(previousReqSeek);
			}
		}

		@Override
		public void onLoading() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onVideoEnded() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onVideoStarted() {
			// TODO Auto-generated method stub

		}

	}
}

