package com.tikal.share.youtube;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class YoutubeData implements Serializable {
	private static final long serialVersionUID = 1L;
	private String userName;
	private Date updated;
	private List<YoutubePlaylist> playList;

	public YoutubeData(String userName) {
		super();
		this.userName = userName;
	}

	public YoutubeData() {
	}

	public List<YoutubePlaylist> getPlayList() {
		return playList;
	}

	public void setPlayList(List<YoutubePlaylist> playList) {
		this.playList = playList;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getUserName() {
		return userName;
	}

	public void write(ObjectOutputStream objectOutputStream) throws IOException {
		objectOutputStream.writeObject(userName);
		objectOutputStream.writeObject(updated);
		objectOutputStream.writeInt(playList.size());
		for (YoutubePlaylist youtube : playList) {
			youtube.write(objectOutputStream);
		}
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void read(ObjectInputStream objectInputStream) throws OptionalDataException, ClassNotFoundException,
			IOException {
		userName = (String) objectInputStream.readObject();
		updated = (Date) objectInputStream.readObject();
		playList = new LinkedList<YoutubePlaylist>();
		int size = objectInputStream.readInt();
		for (int i=0; i<size;i++){
			YoutubePlaylist youtubePlaylist = new YoutubePlaylist();
			youtubePlaylist.read(objectInputStream);
			playList.add(youtubePlaylist);
		}
	}
}
