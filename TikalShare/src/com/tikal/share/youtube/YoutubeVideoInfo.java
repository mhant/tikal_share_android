package com.tikal.share.youtube;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;

public class YoutubeVideoInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private String summary;
	private String thumbnail;
	private String published;
	private Integer duration;
	private Bitmap thumbnailBmp;

	public YoutubeVideoInfo(String id, String title, String summary, String thumbnail, String published,
			Integer duration) {
		super();
		this.id = id;
		this.title = title;
		this.summary = summary;
		this.thumbnail = thumbnail != null ? thumbnail.trim() : null;
		this.published = published;
		this.duration = duration;
	}

	public YoutubeVideoInfo(Parcel source) {
		id = source.readString();
		title = source.readString();
		summary = source.readString();
		thumbnail = source.readString();
		published = source.readString();
		duration = source.readInt();
		thumbnailBmp = Bitmap.CREATOR.createFromParcel(source);
	}

	public YoutubeVideoInfo() {
		// TODO Auto-generated constructor stub
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

	public Bitmap getThumbnailBmp() {
		return thumbnailBmp;
	}

	public void setThumbnailBmp(Bitmap thumbnailBmp) {
		this.thumbnailBmp = thumbnailBmp;
	}

	public void write(ObjectOutputStream objectOutputStream) throws IOException {
		objectOutputStream.writeObject(id);
		objectOutputStream.writeObject(title);
		objectOutputStream.writeObject(summary);
		objectOutputStream.writeObject(thumbnail);
		objectOutputStream.writeObject(published);
		objectOutputStream.writeInt(duration);
		thumbnailBmp.compress(Bitmap.CompressFormat.JPEG, 100, objectOutputStream);
	}

	public void read(ObjectInputStream objectInputStream) throws OptionalDataException, ClassNotFoundException, IOException {
		id = (String)objectInputStream.readObject();
		title = (String)objectInputStream.readObject();
		summary = (String)objectInputStream.readObject();
		thumbnail = (String)objectInputStream.readObject();
		published = (String)objectInputStream.readObject();
		duration = objectInputStream.readInt();
		thumbnailBmp = BitmapFactory.decodeStream(objectInputStream);
	}
}
