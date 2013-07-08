/*
 * Copyright (c) 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.cloud.backend.android.tikal.share.sync;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.google.cloud.backend.android.CloudBackendActivity;
import com.google.cloud.backend.android.CloudCallbackHandler;
import com.google.cloud.backend.android.CloudEntity;
import com.google.cloud.backend.android.CloudQuery.Order;
import com.google.cloud.backend.android.CloudQuery.Scope;
import com.tikal.share.R;

/**
 * Sample Guestbook app with Mobile Backend Starter.
 *
 */
public class CloudSync extends CloudBackendActivity {

	public  static final String RESULT_KEY = "result";
	public  static final String RESULT_SUCCESS= "success";
	public  static final String RESULT_FAILURE = "failure";
	
	public static final String INTENT_COMMAND = "command";
	public static final int INTENT_COMMAND_SET = 0;
	public static final int INTENT_COMMAND_GET = 1;
	public static final String INTENT_OFFSET_MILIS = "offset_milis";
	public static final String INTENT_VIDEO_ID = "video_id";
	public static final String INTENT_CLIENT_ID = "client_id";

	// data formatter for formatting createdAt property
//	private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss ", Locale.US);

	private static final String BROADCAST_PROP_DURATION = "duration";

	private static final String BROADCAST_PROP_MESSAGE = "message";

	private String clientID;
	
	private String videoID;


	// UI components
	//  private TextView tvPosts;
	//  private EditText videoID;
	//  private EditText watchedAt;
	//  private EditText watchedUntil;
	//  private Button btSend;

	// a list of posts on the UI
	List<CloudEntity> posts = new LinkedList<CloudEntity>();

	// initialize UI
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sync);
		Intent intent = getIntent();
		clientID = intent.getExtras().getString(INTENT_CLIENT_ID);
		videoID = intent.getExtras().getString(INTENT_VIDEO_ID);;
		int commandID = intent.getIntExtra(INTENT_COMMAND,INTENT_COMMAND_GET );
		if(commandID == INTENT_COMMAND_GET){
			listAllPosts();
		}
		else{
			
			onSend(clientID,
					intent.getExtras().getString(INTENT_VIDEO_ID),
					intent.getExtras().getString(INTENT_OFFSET_MILIS));
		}
		//    setContentView(R.layout.activity_main);
		//    tvPosts = (TextView) findViewById(R.id.tvPosts);
		//    videoID = (EditText) findViewById(R.id.videoID);
		//    watchedAt = (EditText) findViewById(R.id.watchedAt);
		//    watchedUntil = (EditText) findViewById(R.id.watchedUntil);
		//    btSend = (Button) findViewById(R.id.btSend);
	}

	//  @Override
	//  protected void onPostCreate() {
	//    super.onPostCreate();
	//    listAllPosts();
	//  }


	// execute query "SELECT * FROM Guestbook ORDER BY _createdAt DESC LIMIT 50"
	// this query will be re-executed when matching entity is updated
	private void listAllPosts() {

		// create a response handler that will receive the query result or an error
		CloudCallbackHandler<List<CloudEntity>> handler = new CloudCallbackHandler<List<CloudEntity>>() {
			@Override
			public void onComplete(List<CloudEntity> results) {
				for (CloudEntity post : results) {
					String postMsg = (String) post.get("message");
					String[] vals = postMsg.split(";");
					String tempClientID = "";
					String tempVideoID = "";
					String tempWatchedUntil = "";
					for(String val : vals){
						String[] subVal = val.split(":");
						if(subVal[0].equals("userID")){
							tempClientID = subVal[1];
						}
						else if(subVal[0].equals("videoID")){
							tempVideoID = subVal[1];
						}
						else if(subVal[0].equals("watchedUntil")){
							tempWatchedUntil = subVal[1];
						}
					}
					if(tempClientID.equals(clientID) && tempVideoID.equals(videoID)){
						Intent resultIntent = new Intent();
						resultIntent.putExtra(RESULT_KEY, tempWatchedUntil);
						setResult(Activity.RESULT_CANCELED, resultIntent);
						finish();
						break;
					}
				}
				//in case no matches found
				Intent resultIntent = new Intent();
				resultIntent.putExtra(RESULT_KEY, RESULT_FAILURE);
				setResult(Activity.RESULT_CANCELED, resultIntent);
				finish();
				
				//        posts = results;
				//        updateGuestbookUI();
			}

			@Override
			public void onError(IOException exception) {
				handleEndpointException(exception);
			}
		};

		// execute the query with the handler
		getCloudBackend().listByKind("TikalShare", CloudEntity.PROP_CREATED_AT, Order.DESC, 50,
				Scope.FUTURE_AND_PAST, handler);
	}

	private void handleEndpointException(IOException e) {
		Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		Intent resultIntent = new Intent();
		resultIntent.putExtra(RESULT_KEY, RESULT_FAILURE);
		setResult(Activity.RESULT_CANCELED, resultIntent);
		finish();
		//    btSend.setEnabled(true);
	}

	// convert posts into string and update UI
	//  private void updateGuestbookUI() {
	//    final StringBuilder sb = new StringBuilder();
	//    for (CloudEntity post : posts) {
	//    	String postMsg = (String) post.get("message");
	//    	String[] vals = postMsg.split(";");
	//    	for(String val : vals){
	//    		String[] subVal = val.split(":");
	//    		sb.append(subVal[0] + "--" + subVal[1]);
	//    	}
	//    }
	//    tvPosts.setText(sb.toString());
	//  }

	// removing the domain name part from email address
	//  private String getCreatorName(CloudEntity b) {
	//    if (b.getCreatedBy() != null) {
	//      return " " + b.getCreatedBy().replaceFirst("@.*", "");
	//    } else {
	//      return "<anonymous>";
	//    }
	//  }

	// post a new message to server
	//  public void onSendButtonPressed(View view) {
	public void onSend(String clientID, String videoID , String watchedUntil){

		// create a CloudEntity with the new post
		CloudEntity newPost = new CloudEntity("TikalShare");
		String postString = "userID:" + clientID+
		";watchedUntil:" + watchedUntil + 
		";videoID:" + videoID;
		newPost.put("message", postString);

		// create a response handler that will receive the result or an error
		CloudCallbackHandler<CloudEntity> handler = new CloudCallbackHandler<CloudEntity>() {
			@Override
			public void onComplete(final CloudEntity result) {
				Intent resultIntent = new Intent();
				resultIntent.putExtra(RESULT_KEY, RESULT_SUCCESS);
				setResult(Activity.RESULT_CANCELED, resultIntent);
				finish();
				//        posts.add(0, result);
				//        updateGuestbookUI();
				//        watchedAt.setText("");
				//        watchedUntil.setText("");
				//        videoID.setText("");
				//        btSend.setEnabled(true);
			}

			@Override
			public void onError(final IOException exception) {
				handleEndpointException(exception);
			}
		};

		// execute the insertion with the handler
		getCloudBackend().insert(newPost, handler);
		//    btSend.setEnabled(false);
	}

	// handles broadcast message and show a toast
	@Override
	public void onBroadcastMessageReceived(List<CloudEntity> l) {
		for (CloudEntity e : l) {
			String message = (String) e.get(BROADCAST_PROP_MESSAGE);
			int duration = Integer.parseInt((String) e.get(BROADCAST_PROP_DURATION));
			Toast.makeText(this, message, duration).show();
		}
	}
}
