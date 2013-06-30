
package com.tikal.share;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;

public class ListFragment extends SherlockListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		List listVideos = new ArrayList();
		listVideos.add(new Video("London", "video about London", "10:22", "10/02/2012"));
		listVideos.add(new Video("Rome", "video about Rome", "10:22", "10/02/2012"));
		listVideos.add(new Video("Paris", "video about Paris", "10:22", "10/02/2012"));
		listVideos.add(new Video("New York", "video about New York", "10:22", "10/02/2012"));
		listVideos.add(new Video("Tel aviv", "video about Tel Aviv", "10:22", "10/02/2012"));
		listVideos.add(new Video("Holon", "video about Holon", "10:22", "10/02/2012"));

		VideoListAdapter vla = new VideoListAdapter(getSherlockActivity(), R.layout.list_videos_row, listVideos);
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
			Video video = (Video) getItem(position);

			/* Set Video Title */
			TextView txtName = (TextView) convertView.findViewById(R.id.video_title);
			txtName.setText(video.getName());

			/* Set Video Description */
			txtName = (TextView) convertView.findViewById(R.id.video_discreption);
			txtName.setText(video.getDescription());
			
			/* Set Video length */
			txtName = (TextView) convertView.findViewById(R.id.video_length);
			txtName.setText(video.getLength());
			/* Set Video Title */
			 txtName = (TextView) convertView.findViewById(R.id.video_created);
			txtName.setText(video.getCreated());

			/* Take the ImageView from layout and set the city's image */
			/*ImageView imageCity = (ImageView) convertView.findViewById(R.id.ImageCity);
			String uri = "drawable/" + video.getImage();
			int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
			Drawable image = context.getResources().getDrawable(imageResource);
			imageCity.setImageDrawable(image);*/
			return convertView;
		}
	}
}
