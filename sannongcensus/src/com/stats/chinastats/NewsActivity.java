package com.stats.chinastats;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.stats.chinastats.adapter.NewsAdapter;
import com.stats.chinastats.model.Category;
import com.stats.chinastats.model.News;
import com.stats.chinastats.model.OnePageNews;
import com.stats.chinastats.util.Data;
import com.stats.chinastats.util.HttpHelper;
import com.stats.chinastats.xml.NewsHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class NewsActivity extends Activity {
	private ProgressDialog progressDialog = null;
	private int mWidth;
	private Context context;
	private ListView listview;
	private NewsAdapter adapter;
	private ImageButton go_back;
	private ImageButton refresh;
	private Category cate = new Category();
	private List<News> newss = new ArrayList<News>();;
	private List<News> addNews = new ArrayList<News>();;
	private OnePageNews opn = new OnePageNews();
	private String url = null;
	private String name = null;
	private ArrayList<String> Ids = new ArrayList<String>();
	private ArrayList<String> imgSrcs = new ArrayList<String>();
	private ArrayList<String> urls = new ArrayList<String>();
	private ArrayList<String> titles = new ArrayList<String>();
	private int visibleLastIndex = 0; // 最后的可视项索引
	private int visibleItemCount; // 当前窗口可见项总数
    private String szTitle = Data.szTitle;
	private View loadMoreView;
	private Button loadMoreButton;
	

	private boolean isCale = false;
	private String CaleUrl = "http://219.235.129.108:8080/NewManager/wap/upload/cale/caleiphone";
	private Button loadlocal;
	private boolean hasnetwork;
	private Button reload;
	private SharedPreferences sp;
    private Boolean isSleep;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_news);
		sp = getSharedPreferences("mysetup", 0);
		isSleep=sp.getBoolean("id", true);
		this.context = this;
		Display display = this.getWindowManager().getDefaultDisplay();
		mWidth = display.getWidth();
		Intent intent = getIntent();
		String str = intent.getStringExtra("name");
		cate.setName(str);
		str = intent.getStringExtra("url");
		cate.setUrl(str);

		listview = (ListView) findViewById(R.id.item_listview);

		go_back = (ImageButton) findViewById(R.id.top_back);
		refresh = (ImageButton) findViewById(R.id.item_refresh);

		TextView title = (TextView) findViewById(R.id.item_title);

		loadMoreView = LayoutInflater.from(this).inflate(R.layout.loadmore,
				null);
		loadMoreButton = (Button) loadMoreView
				.findViewById(R.id.loadMoreButton);
		loadMoreButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new getMoreNewsAsync().execute();

			}
		});
		listview.setOnItemClickListener(listItemClickListener);

		// listview.setOnScrollListener(listScrollListener);
		listview.addFooterView(loadMoreView); // 设置列表底部视图
		
		reload = (Button) findViewById(R.id.reload);
		hasnetwork = HttpHelper.hasNetwork(context);
		if (!hasnetwork) {
			listview.setVisibility(View.GONE);
			reload.setVisibility(View.VISIBLE);
		}

		reload.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				hasnetwork = HttpHelper.hasNetwork(context);
				if (!hasnetwork) {

					listview.setVisibility(View.GONE);
					reload.setVisibility(View.VISIBLE);

				} else {
					listview.setVisibility(View.VISIBLE);
					reload.setVisibility(View.GONE);
					new getNewsAsync().execute();

				}
			}
		});
		refresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				new getNewsAsync().execute();
			}
		});
		go_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});

		name = cate.getName();
		url = cate.getUrl();
		if (name.equals("日历"))
			isCale = true;
		
		title.setText(Data.szTitle + name);
		title.setTextSize(20);

		url = this.replace(name, url);

		new getNewsAsync().execute();

	}

	private List<News> parse(String xmlStr) {
		if (isCale) {
			newss.clear();
			opn = new OnePageNews();
			opn.setNextUrl("");
			for (int i = 1; i < 13; i++) {
				News news = new News();
				if (i < 10) {
					news.setTitle("0" + i + "月统计信息发布日程");
					news.setContentUrl(CaleUrl + "0" + i + ".png");
				} else {
					news.setTitle(i + "月统计信息发布日程");
					news.setContentUrl(CaleUrl + i + ".png");

				}

				news.setAbstractCon("0");
				news.setDate("");
				news.setTop("");
				newss.add(news);
			}
			return newss;
		}
		try {

			newss.clear();
			opn = new OnePageNews();

			SAXParser localSAXParser = SAXParserFactory.newInstance()
					.newSAXParser();
			NewsHandler newsHandler = new NewsHandler(newss, opn);

			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			XMLReader xmlReader = saxParserFactory.newSAXParser()
					.getXMLReader();
			xmlReader.setContentHandler(newsHandler);
			xmlReader.parse(new InputSource(new StringReader(xmlStr)));
		} catch (Exception e) {

		}
		return newss;

	}

	private class getNewsAsync extends AsyncTask<Void, Integer, String> {

		@Override
		protected void onPreExecute() {
			// 显示ProgressDialog

			progressDialog = ProgressDialog.show(context, "正在加载数据", "请稍等...",
					true, false);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			String result = null;

			result = HttpHelper.downLoadText(url);

			return result;
		}

		@Override
		protected void onProgressUpdate(Integer... values) { // 可以与UI控件交互
			// loading_top.setProgress(values[0]);
			// progressBar.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(String result) {

			// 关闭ProgressDialog
			progressDialog.dismiss();
			if (result != null) {
				newss = parse(result);

				// System.out.println(newss.get(1).toString());

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
				adapter = new NewsAdapter(context, mWidth, newss,isSleep);
				listview.setAdapter(adapter);

				// 如果到底了
				String nextUrl = opn.getNextUrl();
				if (nextUrl == null || nextUrl.equals(""))
					listview.removeFooterView(loadMoreView);
			}

			super.onPostExecute(result);
		}
	}

	OnScrollListener listScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			int itemsLastIndex = adapter.getCount() - 1; // 数据集最后一项的索引
			int lastIndex = itemsLastIndex + 1;
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& visibleLastIndex == lastIndex) {
				// 如果是自动加载,可以在这里放置异步加载数据的代码
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleitemcount, int totalItemCount) {
			visibleItemCount = visibleitemcount;
			visibleLastIndex = firstVisibleItem + visibleItemCount - 1;

			// 如果所有的记录选项等于数据集的条数，则移除列表底部视图
			// if(totalItemCount == adapter.getCount()+1 ){

			// listview.removeFooterView(loadMoreView);
			// Toast.makeText(context, "数据全部加载完!", Toast.LENGTH_LONG).show();
			// }
		}

	};
	

	OnItemClickListener listItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {

			Intent intent = new Intent();
			intent.putStringArrayListExtra("Ids", Ids);
			intent.putStringArrayListExtra("urls", urls);
			intent.putExtra("position", position);
			intent.putExtra("title", szTitle);
			intent.putStringArrayListExtra("titles", titles);
			intent.putStringArrayListExtra("imgSrcs", imgSrcs);
			intent.putExtra("activity", "news");

			intent.setClass(context, WebActivity.class);
			context.startActivity(intent);

			((Activity) context).overridePendingTransition(
					R.anim.slide_in_left, R.anim.slide_out_right);

		}

	};

	private class getMoreNewsAsync extends AsyncTask<Void, Integer, String> {

		@Override
		protected void onPreExecute() {
			loadMoreButton.setText("正在加载中..."); // 设置按钮文字
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {

			String nextUrl = opn.getNextUrl();
			String result = null;

			nextUrl = replace(name, nextUrl);

			result = HttpHelper.downLoadText(nextUrl);

			return result;
		}

		@Override
		protected void onProgressUpdate(Integer... values) { // 可以与UI控件交互
			// loading_top.setProgress(values[0]);
			// progressBar.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(String result) {

			if (result != null) {

				addNews = parse(result);
//				for (int i = 0; i < addNews.size(); i++) {
//					urls.add(addNews.get(i).getContentUrl());
//				}
//				for (int i = 0; i < addNews.size(); i++) {
//					titles.add(addNews.get(i).getTitle());
//				}
				for (int i = 0; i < addNews.size(); i++) {
					Ids.add(addNews.get(i).getId());
				}
				for (int i = 0; i < addNews.size(); i++) {
					urls.add(addNews.get(i).getContentUrl());
				}
				for (int i = 0; i < addNews.size(); i++) {
					titles.add(addNews.get(i).getTitle());
				}
				for (int i = 0; i < addNews.size(); i++) {
					imgSrcs.add(addNews.get(i).getToppicurl());
				}
				adapter.addNewsItem(addNews);
				adapter.notifyDataSetChanged();
			}
			loadMoreButton.setText("查看更多..."); // 恢复按钮文字
			// 到底了，去掉loadmore
			String nextUrl = opn.getNextUrl();
			if (nextUrl == null || nextUrl.equals(""))
				listview.removeFooterView(loadMoreView);

			super.onPostExecute(result);
		}
	}

	private static String encode(String paramString) {
		String str1 = null;
		try {
			String str2 = "utf-8";
			str1 = URLEncoder.encode(paramString, str2);
			str2 = str1;
			return str2;
		} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
			localUnsupportedEncodingException.printStackTrace();
			int i = 0;
		}
		return str1;
	}

	public static String replace(String paramString1, String paramString2) {
		String str = encode(paramString1);
		return paramString2.replace(paramString1, str);
	}

}
