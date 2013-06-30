
package com.tikal.share;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
import android.widget.Toast;

import com.tikal.share.youtube.YoutubePlaylist;
import com.tikal.share.youtube.YoutubeVideoInfo;

public class MyListFragment extends ListFragment {
	List listVideos = new ArrayList();

	public MyListFragment() {
	}

	public MyListFragment(YoutubePlaylist youtubePlaylist) {
		List<YoutubeVideoInfo> list = youtubePlaylist.getYoutubeVideoInfo();
		for (YoutubeVideoInfo node : list) {
			listVideos.add(node);
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(arg0.getContext(), "clicked on " + arg3, Toast.LENGTH_LONG).show();

			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		/*
		 * List listVideos = new ArrayList(); listVideos.add(new Video("London", "video about London", "10:22",
		 * "10/02/2012")); listVideos.add(new Video("Rome", "video about Rome", "10:22", "10/02/2012"));
		 * listVideos.add(new Video("Paris", "video about Paris", "10:22", "10/02/2012")); listVideos.add(new
		 * Video("New York", "video about New York", "10:22", "10/02/2012")); listVideos.add(new Video("Tel aviv",
		 * "video about Tel Aviv", "10:22", "10/02/2012")); listVideos.add(new Video("Holon", "video about Holon",
		 * "10:22", "10/02/2012"));
		 */VideoListAdapter vla = new VideoListAdapter(getActivity(),
				R.layout.list_videos_row, listVideos);
		setListAdapter(vla);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public class VideoListAdapter extends ArrayAdapter {

		private int resource;
		private LayoutInflater inflater;
		private Context context;

		public VideoListAdapter(Context ctx, int resourceId, List objects) {
			super(ctx, resourceId, objects);
			resource = resourceId;
			inflater = LayoutInflater.from(ctx);
			context = ctx;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			/* create a new view of my layout and inflate it in the row */
			convertView = (RelativeLayout) inflater.inflate(resource, null);

			/* Extract the city's object to show */
			YoutubeVideoInfo video = (YoutubeVideoInfo) getItem(position);

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

			/* Take the ImageView from layout and set the city's image */
			/*
			 * ImageView imageCity = (ImageView) convertView.findViewById(R.id.ImageCity); String uri = "drawable/" +
			 * video.getImage(); int imageResource = context.getResources().getIdentifier(uri, null,
			 * context.getPackageName()); Drawable image = context.getResources().getDrawable(imageResource);
			 * imageCity.setImageDrawable(image);
			 */

			ImageView imageView = (ImageView) convertView.findViewById(R.id.video_image);
			if (video.getThumbnailBmp() != null) {
				imageView.setImageBitmap(video.getThumbnailBmp());
			}

			return convertView;
		}
	}

}
