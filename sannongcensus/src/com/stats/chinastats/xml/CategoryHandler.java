package com.stats.chinastats.xml;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.stats.chinastats.model.Category;

public class CategoryHandler extends DefaultHandler {
	private List<Category> categorys = null;
	private Category category = null;
	private String tagName = null;
	private StringBuffer sb = null;
	
	public CategoryHandler (List<Category> cates) {
		super();
		this.categorys = cates;
		sb =new StringBuffer();
	}

	public List<Category> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<Category> categorys) {
		this.categorys = categorys;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// String temp = new String(ch, start, length);
		sb.append(ch, start, length);
		String temp = sb.toString();

		if (tagName.equals("name")) {
			category.setName(temp);
		} else if (tagName.equals("url")) {
			category.setUrl(temp);
			// System.out.println("url:"+temp);
		}
	}

	@Override
	public void endDocument() throws SAXException {

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equals("cate")) {
			categorys.add(category);
		}
		tagName = "";// ��ֹһЩ����
		// super.endElement(uri, localName, qName);
	}

	@Override
	public void startDocument() throws SAXException {

		super.startDocument();
		// sb.delete(0,sb.length());
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		sb.delete(0, sb.length());
		this.tagName = localName;
		if (tagName.equals("cate")) {
			category = new Category();
		}
	}

}
