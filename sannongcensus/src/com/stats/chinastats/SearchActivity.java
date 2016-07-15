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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends Activity {
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
	private String search_key = "搜索";
	private ArrayList<String> Ids = new ArrayList<String>();
	private ArrayList<String> urls = new ArrayList<String>();
	private ArrayList<String> titles = new ArrayList<String>();
	private ArrayList<String> imgSrcs = new ArrayList<String>();
	private int visibleLastIndex = 0; // 最后的可视项索引
	private int visibleItemCount; // 当前窗口可见项总数
    private String szTitle = Data.szTitle;
	private View loadMoreView;
	private Button loadMoreButton;
	
	private EditText phrase_edit;
	private ImageButton search_fulltext;
	private String  first_url=Data.startUrl+"/getSearchCategoryNewsXml?startnid=0&count=15&cid=0&keyword=";
	private String url;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_layout);
		this.context = this;
		Display display = this.getWindowManager().getDefaultDisplay();
		mWidth = display.getWidth();

		listview = (ListView) findViewById(R.id.item_listview);

		go_back = (ImageButton) findViewById(R.id.top_back);
	    phrase_edit= (EditText) findViewById(R.id.phrase_edit);
		phrase_edit.setOnKeyListener(peOnKeyListener);

        search_fulltext =(ImageButton)findViewById(R.id.search_fulltext);
        search_fulltext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openSearch();
				

			}

        });
	
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

		 go_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});


	}
	public void openSearch() {
	
		//
		search_key = phrase_edit.getText().toString().trim(); 
	    url=first_url+ search_key;
		new getNewsAsync().execute();
		
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);   
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);   
  

	
	}
	OnKeyListener peOnKeyListener = new OnKeyListener() {

		public boolean onKey(View v, int keyCode, KeyEvent event) {

			// 监视硬键盘按键

			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& keyCode == KeyEvent.KEYCODE_ENTER) {
				//打开搜索
                 openSearch();

				return true;

			}

			return false;

		}

	};

	private List<News> parse(String xmlStr) {
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
			  //把 url中搜索汉字换成 UTF-8格式
			url=replace(search_key,url);
			Log.d("TTTTTTTTTTTTTTT",url);
		
			result = HttpHelper.downLoadText(url);
			Log.d("TTTTTTTTTTTTTTT",result);
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
                Ids.clear();
                urls.clear();
                titles.clear();
                imgSrcs.clear();
				for (int i = 0; i < newss.size(); i++) {
					Ids.add(newss.get(i).getId());
				}
				for (int i = 0; i < newss.size(); i++) {
					titles.add(newss.get(i).getTitle());
				}
				for (int i = 0; i < newss.size(); i++) {
					urls.add(newss.get(i).getContentUrl());
				}
				for (int i = 0; i < newss.size(); i++) {
					imgSrcs.add(newss.get(i).getToppicurl());
				}
				adapter = new NewsAdapter(context, mWidth, newss,true);
				listview.setAdapter(adapter);

				// 如果到底了
				String nextUrl = opn.getNextUrl();
				Log.d("TTTTTTT","nextUrl:"+nextUrl);
				if (nextUrl == null || nextUrl.equals(""))
					listview.removeFooterView(loadMoreView);
			}

			super.onPostExecute(result);
		}
		private String encode(String paramString) {
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

		public String replace(String paramString1, String paramString2) {
			String str = encode(paramString1);
			return paramString2.replace(paramString1, str);
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
			intent.putExtra("activity", "news");

			intent.putStringArrayListExtra("titles", titles);
			intent.putStringArrayListExtra("imgSrcs", imgSrcs);

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
            //把 url中搜索汉字换成 UTF-8格式
			nextUrl = replace(search_key, nextUrl);

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
				for (int i = 0; i < addNews.size(); i++) {
					Ids.add(addNews.get(i).getId());
				}
				for (int i = 0; i < addNews.size(); i++) {
					titles.add(addNews.get(i).getTitle());
				}
				for (int i = 0; i < addNews.size(); i++) {
					urls.add(addNews.get(i).getContentUrl());
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
