package com.stats.chinastats.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stats.chinastats.model.News;
import com.stats.chinastats.util.Data;
import com.stats.chinastats.R;

public class NewsAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<News> newss = new ArrayList<News>();
	private int mWidth;
	private Boolean isSleep;

	public NewsAdapter(Context context, int mWdith, List<News> newss,Boolean isSleep) {
		this.newss.addAll(newss);
		this.mWidth = mWdith;
		this.isSleep = isSleep;
		layoutInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return this.newss.size();
	}

	public Object getItem(int position) {
		return this.newss.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		News i = this.newss.get(position);

		if (position == 0 && i.getToppicurl()!=null) 
			convertView = layoutInflater.inflate(R.layout.common_top_layout,
					null);
		else 
			convertView = layoutInflater.inflate(R.layout.common_layout,
					null);
 			viewHolder = new ViewHolder();
			viewHolder.toppic = (ImageView) convertView
					.findViewById(R.id.toppic);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.abstract_c = (TextView) convertView
					.findViewById(R.id.abstract_c);
			viewHolder.date = (TextView) convertView.findViewById(R.id.date);
			//convertView.setTag(viewHolder);

		if (position >= this.newss.size())
			return convertView;

	
		if (i.getTop().equals("pictop")) {
			viewHolder.toppic.setVisibility(View.VISIBLE);
			if (position == 0) {
				viewHolder.date.setVisibility(View.GONE);
				new com.stats.chinastats.util.ListPic(viewHolder.toppic,
						Data.startUrl+ i.getToppicurl(), mWidth).getPic();
			}
			else
				new com.stats.chinastats.util.ListPic(viewHolder.toppic,
						Data.startUrl+ i.getToppicurl(),100).getPic();
		} else
			viewHolder.toppic.setVisibility(View.GONE);

		viewHolder.title.setText(i.getTitle());
		String temp = i.getAbstractCon();
		if (!temp.equals("0"))
			viewHolder.abstract_c.setText(Html.fromHtml(temp));
		else
			viewHolder.abstract_c.setVisibility(View.GONE);
		viewHolder.date.setText(i.getDate());
		viewHolder.date.setTextSize(15);

		if (position % 2 != 1)
			convertView.setBackgroundColor(Color.argb(255, 230, 230, 230));
		return convertView;
	}

	public void addNewsItem(List<News> addNews) {
		newss.addAll(addNews);
	}

	private class ViewHolder {
		ImageView toppic;
		TextView title;
		TextView abstract_c;
		TextView date;
	}

}
