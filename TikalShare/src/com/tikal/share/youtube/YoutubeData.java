package com.tikal.share.youtube;

import java.util.Date;
import java.util.List;

public class YoutubeData {
	private String userName;
	public YoutubeData(String userName) {
		super();
		this.userName = userName;
	}
	private List<YoutubePlaylist> playList;
	private Date updated;
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
}
