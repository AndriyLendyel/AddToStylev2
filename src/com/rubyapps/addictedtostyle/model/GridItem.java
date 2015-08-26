package com.rubyapps.addictedtostyle.model;

public class GridItem {
	
	private int imageId;
	private String url;
	private String name;

	public GridItem(int imageId, String name, String url) {
		super();
		this.imageId = imageId;
		this.url = url;
		this.name = name;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
