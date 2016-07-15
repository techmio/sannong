package com.stats.chinastats.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class News implements Serializable, Parcelable {
	public String getContentUrl() {
		return ContentUrl;
	}

	public int describeContents() {
		return 1;
	}
	public void writeToParcel(Parcel paramParcel, int paramInt)
	  {
	  }
	@Override
	public String toString() {
		return "News [ContentUrl=" + ContentUrl + ", abstractCon="
				+ abstractCon + ", category=" + category + ", date=" + date
				+ ", id=" + id + ", title=" + title + ", top=" + top
				+ ", toppicurl=" + toppicurl + ", getContentUrl()="
				+ getContentUrl() + ", getAbstractCon()=" + getAbstractCon()
				+ ", getCategory()=" + getCategory() + ", getDate()="
				+ getDate() + ", getId()=" + getId() + ", getTitle()="
				+ getTitle() + ", getTop()=" + getTop() + ", getToppicurl()="
				+ getToppicurl() + "]";
	}

	public void setContentUrl(String contentUrl) {
		ContentUrl = contentUrl;
	}

	public String getAbstractCon() {
		return abstractCon;
	}

	public void setAbstractCon(String abstractCon) {
		this.abstractCon = abstractCon;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public String getToppicurl() {
		return toppicurl;
	}

	public void setToppicurl(String toppicurl) {
		this.toppicurl = toppicurl;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1253241841847586130L;
	private String ContentUrl;
	private String abstractCon;
	private String category;
	private String date;
	private String id;
	private String title;
	private String top;
	private String toppicurl;

	public News() {
		// TODO Auto-generated constructor stub
	}

}
