package com.tikal.share;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.cloud.backend.android.tikal.share.sync.CloudSync;
import com.tikal.share.youtube.YoutubePlaylist;
import com.tikal.share.youtube.YoutubeVideoInfo;

@SuppressLint("ValidFragment")
public class MyListFragment extends ListFragment {
	List<YoutubeVideoInfo> listVideos = new ArrayList<YoutubeVideoInfo>();

	public MyListFragment() {
	}

	public MyListFragment(YoutubePlaylist youtubePlaylist) {
		List<YoutubeVideoInfo> list = youtubePlaylist.getYoutubeVideoInfo();
		for (YoutubeVideoInfo node : list) {
			listVideos.add(node);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		VideoListAdapter vla = new VideoListAdapter(getActivity(),
				R.layout.list_videos_row, listVideos);
		setListAdapter(vla);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent i = new Intent(getActivity(), PlayerActivity.class);
				String videoId = listVideos.get(position).getId();
				i.putExtra(CloudSync.INTENT_VIDEO_ID, videoId);
				startActivity(i);
			}
		});
	}

	public class VideoListAdapter extends ArrayAdapter<YoutubeVideoInfo> {

		private int resource;
		private LayoutInflater inflater;

		public VideoListAdapter(Context ctx, int resourceId, List<YoutubeVideoInfo> objects) {
			super(ctx, resourceId, objects);
			resource = resourceId;
			inflater = LayoutInflater.from(ctx);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			/* create a new view of my layout and inflate it in the row */
			convertView = (RelativeLayout) inflater.inflate(resource, null);

			/* Extract the city's object to show */
			YoutubeVideoInfo video = (YoutubeVideoInfo) getItem(position);

			ImageView imageView = (ImageView) convertView.findViewById(R.id.video_image);
			if (video.getThumbnailBmp() != null) {
				imageView.setImageBitmap(video.getThumbnailBmp());
			}	
			/* Set Video Title */
			TextView txtName = (TextView) convertView
					.findViewById(R.id.video_title);
			txtName.setText(video.getTitle());

			/* Set Video Description */
			txtName = (TextView) convertView
					.findViewById(R.id.video_discreption);
			txtName.setText(video.getSummary());

			/* Set Video length */
			txtName = (TextView) convertView.findViewById(R.id.video_length);
			txtName.setText(Integer.toString(video.getDuration()));

			/* Set Video created */
			txtName = (TextView) convertView.findViewById(R.id.video_created);
			txtName.setText(video.getPublished());

			return convertView;
		}
	}

}
