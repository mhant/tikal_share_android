package com.tikal.share.youtube;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class YoutubePlaylist implements Serializable {
	private static final long serialVersionUID = 1L;
	private String title;
	private String id;
	private List<YoutubeVideoInfo> youtubeVideoInfo = new LinkedList<YoutubeVideoInfo>();

	public YoutubePlaylist(String title, String id) {
		super();
		this.title = title;
		this.id = id;
	}

	public YoutubePlaylist() {}

	public String getTitle() {
		return title;
	}

	public String getId() {
		return id;
	}

	public List<YoutubeVideoInfo> getYoutubeVideoInfo() {
		return youtubeVideoInfo;
	}

	public void setYoutubeVideoInfo(List<YoutubeVideoInfo> youtubeVideoInfo) {
		this.youtubeVideoInfo = youtubeVideoInfo;
	}

	public void write(ObjectOutputStream objectOutputStream) throws IOException {
		objectOutputStream.writeObject(title);
		objectOutputStream.writeObject(id);
		objectOutputStream.writeInt(youtubeVideoInfo.size());
		for (YoutubeVideoInfo youtubeVideo : youtubeVideoInfo) {
			youtubeVideo.write(objectOutputStream);
		}
	}

	public void read(ObjectInputStream objectInputStream) throws OptionalDataException, ClassNotFoundException,
			IOException {
		title = (String) objectInputStream.readObject();
		id = (String) objectInputStream.readObject();
		youtubeVideoInfo = new LinkedList<YoutubeVideoInfo>();
		int size = objectInputStream.readInt();
		for (int i = 0; i < size; i++) {
			YoutubeVideoInfo youtubeVideo = new YoutubeVideoInfo();
			youtubeVideo.read(objectInputStream);
			youtubeVideoInfo.add(youtubeVideo);
		}
	}

}
