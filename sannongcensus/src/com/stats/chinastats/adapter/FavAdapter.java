package com.stats.chinastats.adapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.stats.chinastats.database.DBAdapter;
import com.stats.chinastats.model.Category;
import com.stats.chinastats.model.News;
import com.stats.chinastats.util.Const;
import com.stats.chinastats.util.Data;
import com.stats.chinastats.util.HttpHelper;
import com.stats.chinastats.util.SDHelper;
import com.stats.chinastats.R;
import com.stats.chinastats.WebActivity;

public class FavAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<News> newss = new ArrayList<News>();
	private int mWidth;
	private boolean isDelete = false;
	private Context context;

	public FavAdapter(Context context, int mWdith, List<News> newss) {
		this.context = context;
		this.newss.addAll(newss);
		this.mWidth = mWdith;
		layoutInflater = LayoutInflater.from(context);
	}
	public void setDelete(boolean isDelete)
	{
		this.isDelete = isDelete;
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

//		if (position == 0 && i.getToppicurl()!=null) 
//			convertView = layoutInflater.inflate(R.layout.common_top_layout,
//					null);
//		else 
			convertView = layoutInflater.inflate(R.layout.common_layout,
					null);

			viewHolder = new ViewHolder();
			viewHolder.toppic = (ImageView) convertView
					.findViewById(R.id.toppic);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.delete = (ImageButton) convertView.findViewById(R.id.delete);
			viewHolder.delete.setOnClickListener(new lvButtonListener(
					position));

			if(isDelete == true) 
				viewHolder.delete.setVisibility(View.VISIBLE);
			else 
				viewHolder.delete.setVisibility(View.INVISIBLE);
			viewHolder.abstract_c = (TextView) convertView
					.findViewById(R.id.abstract_c);
			viewHolder.date = (TextView) convertView.findViewById(R.id.date);
			//convertView.setTag(viewHolder);

		if (position >= this.newss.size())
			return convertView;

	
		if (i.getTop().equals("pictop")) {
			viewHolder.toppic.setVisibility(View.VISIBLE);
//			if (position == 0) {
//				viewHolder.date.setVisibility(View.GONE);
//				new com.stats.chinastats.util.ListPic(viewHolder.toppic,
//						Data.startUrl+ i.getToppicurl(), mWidth).getPic();
//			}
//			else
				new com.stats.chinastats.util.ListPic(viewHolder.toppic,
						Data.startUrl+ i.getToppicurl(),100).getPic();
		} else
			viewHolder.toppic.setVisibility(View.GONE);

		viewHolder.title.setText(i.getTitle());
		String temp = i.getAbstractCon();
		if (!temp.equals("0"))
			viewHolder.abstract_c.setText(temp);
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
		ImageButton delete;
		TextView abstract_c;
		TextView date;
	}


class lvButtonListener implements View.OnClickListener {
	private int position;
	private News news;
	Button button;
	ProgressBar progressbar;
	private DBAdapter db;

	lvButtonListener(int pos) {
		position = pos;
		news = newss.get(position);
    }

	@Override
	public void onClick(View v) {
		 //打开数据库
		db = new DBAdapter(context);
	   try {
		  db.open();
		  if(db.delete(news.getId())) {
		        Toast.makeText(context, "已成功从收藏夹中删除！",   
		                Toast.LENGTH_LONG).show();   
				//删除列表
				newss.remove(position);
				//更新列表
				notifyDataSetChanged();
		  
		  }
		  else 
		        Toast.makeText(context, "不能从收藏夹中删除！",   
		                Toast.LENGTH_LONG).show();   
	   }
	   catch(SQLException except) {
	        Toast.makeText(context,except.getMessage(),   
	                Toast.LENGTH_LONG).show(); 
	        return;
		   
	   }
		db.close();
	
		
	 }
  }
}
