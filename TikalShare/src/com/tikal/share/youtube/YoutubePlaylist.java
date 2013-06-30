package com.tikal.share.youtube;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class YoutubePlaylist implements Serializable {
	private String title;
	private String id;
	private List<YoutubeVideoInfo> youtubeVideoInfo = new LinkedList<YoutubeVideoInfo>(); 

	public YoutubePlaylist(String title, String id) {
		super();
		this.title = title;
		this.id = id;
	}
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
}
