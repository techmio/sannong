package com.stats.chinastats.xml;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.stats.chinastats.model.Category;
import com.stats.chinastats.model.News;
import com.stats.chinastats.model.OnePageNews;

public class NewsHandler extends DefaultHandler {

	private String tagName = null;
	private List<News> newss = null;
	private News news = null;
	private OnePageNews opn = null;
	private StringBuilder sb;

	public NewsHandler(List<News> newss, OnePageNews opn) {
		super();
		// TODO Auto-generated constructor stub
		this.newss = newss;
		this.opn = opn;
		this.sb = new StringBuilder();
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// String temp = new String(ch, start, length);
		sb.append(ch, start, length);
		String temp = sb.toString();

		if (tagName.equals("id")) {
			this.news.setId(temp);
		}
		else if (tagName.equals("title")) {
			this.news.setTitle(temp);
		} else if (tagName.equals("date")) {
			this.news.setDate(temp);
		} else if (tagName.equals("abstract")) {
			this.news.setAbstractCon(temp);
		} else if (tagName.equals("category")) {
			this.news.setCategory(temp);
		} else if (tagName.equals("contenturl")) {
			this.news.setContentUrl(temp);
		} else if (tagName.equals("top")) {
			this.news.setTop(temp);
		} else if (tagName.equals("toppicurl")) {
			this.news.setToppicurl(temp);
		} else if (tagName.equals("preurl")) {
			this.opn.setPreUrl(temp);
		} else if (tagName.equals("nexturl")) {
			
			this.opn.setNextUrl(temp);
		}
	}

	public void endDocument() throws SAXException {

	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equals("new")) {
			newss.add(news);
		}
	
		tagName = "";// ��ֹһЩ����
		// super.endElement(uri, localName, qName);
	}

	public void startDocument() throws SAXException {
		super.startDocument();
	//   this.opn = new OnePageNews();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		sb.delete(0, sb.length());
		this.tagName = localName;
		if (tagName.equals("new")) {
			news = new News();
			//得到 <new id = vlaue
			for(int i=0;i<attributes.getLength();i++) {
				if((attributes.getLocalName(i)).equals("id"))
					this.news.setId(attributes.getValue(i));
			}
		}
	}
}
