package com.stats.chinastats.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OnePageNews implements Serializable {
	@Override
	public String toString() {
		return "OnePageNews [news=" + news + ", nextUrl=" + nextUrl
				+ ", preUrl=" + preUrl + ", getNews()=" + getNews()
				+ ", getNextUrl()=" + getNextUrl() + ", getPreUrl()="
				+ getPreUrl() + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5158847514926128778L;
	private List<News> news;
	private String nextUrl;
	private String preUrl;

	public OnePageNews() {
		// TODO Auto-generated constructor stub
		this.news = new ArrayList<News>();

	}

	public List<News> getNews() {
		return news;
	}

	public void setNews(List<News> news) {
		this.news = news;
	}

	public String getNextUrl() {
		return nextUrl;
	}

	public void setNextUrl(String nextUrl) {
		this.nextUrl = nextUrl;
	}

	public String getPreUrl() {
		return preUrl;
	}

	public void setPreUrl(String preUrl) {
		this.preUrl = preUrl;
	}

}
