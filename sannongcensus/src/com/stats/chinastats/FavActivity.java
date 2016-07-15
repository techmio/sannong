package com.stats.chinastats;

import java.util.ArrayList;
import java.util.List;

import com.stats.chinastats.adapter.FavAdapter;
import com.stats.chinastats.adapter.NewsAdapter;
import com.stats.chinastats.database.DBAdapter;
import com.stats.chinastats.model.News;
import com.stats.chinastats.util.Const;
import com.stats.chinastats.util.Data;
import com.stats.chinastats.util.HttpHelper;
import com.stats.chinastats.util.SDHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FavActivity extends Activity {

	private Context  context;
	private String szTitle;
	private ImageButton go_back;
	private ImageButton delete;
	private DBAdapter db;
	private Cursor cur;
	private List<News> newss = new ArrayList<News>();
	private ArrayList<String> Ids = new ArrayList<String>();
	private ArrayList<String> urls = new ArrayList<String>();
	private ArrayList<String> titles = new ArrayList<String>();
	private ArrayList<String> imgSrcs = new ArrayList<String>();

	private FavAdapter adapter;
	private int mWidth;
	private ListView listview;
	private Cursor cursor;
	private boolean isDelete = false;
	boolean hasnet=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.context = this;
		setContentView(R.layout.layout_fav);
		TextView title = (TextView) findViewById(R.id.item_title);
		Intent intent =  getIntent();
		String str = intent.getStringExtra("name");

		szTitle =Data.szTitle + str;
		title.setText(szTitle);
	
		listview = (ListView) findViewById(R.id.item_listview);

		go_back = (ImageButton) findViewById(R.id.top_back);
		delete = (ImageButton) findViewById(R.id.item_delete);
		if(!isDelete) delete.setBackgroundResource(R.drawable.del_btn_nor);
		else 
			delete.setBackgroundResource(R.drawable.del_btn_press);
		delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				//delete.setImageDrawable();
				isDelete = ! isDelete;
				if(!isDelete) delete.setBackgroundResource(R.drawable.del_btn_nor);
				else 
					delete.setBackgroundResource(R.drawable.del_btn_press);
				adapter.setDelete(isDelete);
				adapter.notifyDataSetChanged();
			
			}
		});
		go_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});

        getFavData();
		Display display = this.getWindowManager().getDefaultDisplay();
		mWidth = display.getWidth();
        adapter = new FavAdapter(context, mWidth, newss);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(listItemClickListener);

	}
	OnItemClickListener listItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {

			Intent intent = new Intent();
			urls.clear();
			
			for (int i = 0; i < newss.size(); i++) {
				Ids.add(newss.get(i).getId());
			}
			for (int i = 0; i < newss.size(); i++) {
				urls.add(newss.get(i).getContentUrl());
			}
			for (int i = 0; i < newss.size(); i++) {
				titles.add(newss.get(i).getTitle());
			}
			for (int i = 0; i < newss.size(); i++) {
				imgSrcs.add(newss.get(i).getToppicurl());
			}

			intent.putStringArrayListExtra("Ids", Ids);
			intent.putStringArrayListExtra("urls", urls);
			intent.putExtra("position", position);
			intent.putExtra("title", szTitle);
			intent.putExtra("activity", "fav");
        	intent.putStringArrayListExtra("titles", titles);
			intent.putStringArrayListExtra("imgSrcs", imgSrcs);
			intent.setClass(context, WebActivity.class);
			context.startActivity(intent);

			((Activity) context).overridePendingTransition(
					R.anim.slide_in_left, R.anim.slide_out_right);

		}

	};

	private void getFavData() {
		// TODO Auto-generated method stub
		News news = null;
		 //打开数据库
		db = new DBAdapter(context);
	   try {
		    db.open();
		    cursor= this.db.getAll();
		    
		    if(cursor.moveToFirst()) {
			do {
					News item = new News();
					item.setId(cursor.getString(0));
					item.setTitle(cursor.getString(1));
					item.setContentUrl(cursor.getString(2));
					item.setToppicurl(cursor.getString(3));
					String imgsrc =cursor.getString(3);
					if(imgsrc == null )
						 item.setTop("off");
					else
						item.setTop("pictop");
					item.setDate(cursor.getString(4));
					item.setAbstractCon("0");
					newss.add(item);
				} while (cursor.moveToNext());
		    }
			cursor.close();

		 
	   }
	   catch(SQLException except) {
	        Toast.makeText(FavActivity.this,except.getMessage(),   
	                Toast.LENGTH_LONG).show(); 
	        return;
		   
	   }
	   db.close();
		
	}

}
