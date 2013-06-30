
package com.tikal.share;

public class Video {
	private String name;
	private String description;
	private String length;
	private String created;

	public Video(String name, String description, String length, String created) {
		super();
		this.name = name;
		this.description = description;
		this.length = length;
		this.created = created;
	}

	public String getName() {
		return name;
	}

	public void setName(String nameText) {
		name = nameText;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}
}
