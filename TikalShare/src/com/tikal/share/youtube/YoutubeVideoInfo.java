package com.tikal.share.youtube;

import java.io.Serializable;

public class YoutubeVideoInfo {
	private String id;
	private String title;
	private String summary;
	private String thumbnail;
	private String published;
	private Integer duration;

	public YoutubeVideoInfo(String id, String title, String summary, String thumbnail, String published,
			Integer duration) {
		super();
		this.id = id;
		this.title = title;
		this.summary = summary;
		this.thumbnail = thumbnail;
		this.published = published;
		this.duration = duration;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getSummary() {
		return summary;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public String getPublished() {
		return published;
	}

	public Integer getDuration() {
		return duration;
	}
}
