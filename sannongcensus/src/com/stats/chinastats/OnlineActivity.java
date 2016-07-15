package com.stats.chinastats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.stats.chinastats.adapter.CateAdapter;
import com.stats.chinastats.model.Category;
import com.stats.chinastats.model.ItemInfo;
import com.stats.chinastats.util.Data;
import com.stats.chinastats.util.HttpHelper;
import com.stats.chinastats.util.SDHelper;
import com.stats.chinastats.xml.CategoryHandler;

import android.app.AlertDialog;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class OnlineActivity extends Activity {

	private ListView listView = null;
	private Button reload = null;
	private Button loadlocal = null;
	private View settinglayout;
	private TextView filelisttext;
	private EditText url_setting;
	private ImageButton go_back;
	private ImageButton refresh;
	private Button save;
	private Button restore;
	private WebView open;
	SharedPreferences mSharedPreferences;
	ProgressDialog progressDialog = null;
	private String url = "http://doc.hb.stats.cn/news/online/filelist.xml";
	private List<Category> cates = null;
	private String szTitle ;
	boolean hasnetwork;
	CateAdapter adapter;
	Context context;
	ItemInfo itemInfo = new ItemInfo();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = this;
		setContentView(R.layout.online_category);

		TextView title = (TextView) findViewById(R.id.item_title);
		szTitle =Data.szTitle + "下载安装";
		title.setText(szTitle);

		go_back = (ImageButton) findViewById(R.id.top_back);
		refresh = (ImageButton) findViewById(R.id.item_refresh);
		refresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				new getCateAsync().execute();
			}
		});
		go_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});


		listView = (ListView) findViewById(R.id.group_list);
		listView.setVisibility(View.VISIBLE);
		listView.setOnItemClickListener(listItemClickListener);

		reload = (Button) findViewById(R.id.reload);
		reload.setOnClickListener(reloadOnClickListener);

		loadlocal = (Button) findViewById(R.id.loadlocal);
		loadlocal.setOnClickListener(loadlocalOnClickListener);

		hasnetwork = HttpHelper.hasNetwork(this);
		if (!hasnetwork) {

			listView.setVisibility(View.GONE);
			reload.setVisibility(View.VISIBLE);
			loadlocal.setVisibility(View.VISIBLE);
			return;
		}

		new getCateAsync().execute();

	}

	OnClickListener reloadOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			hasnetwork = HttpHelper.hasNetwork(context);
			if (!hasnetwork) {

				listView.setVisibility(View.GONE);
				reload.setVisibility(View.VISIBLE);
				loadlocal.setVisibility(View.VISIBLE);

			} else {
				listView.setVisibility(View.VISIBLE);
				reload.setVisibility(View.GONE);
				loadlocal.setVisibility(View.GONE);
				new getCateAsync().execute();

			}
		}
	};
	OnClickListener loadlocalOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String result = null;
			String filename = null;
			listView.setVisibility(View.GONE);
			try {
				filename = SDHelper.getAppDataPath() + File.separator
						+ "filelist.xml";
				FileReader fr = new FileReader(filename);
				char charbuff[] = new char[1024];
				int len;
				int pos = 0;
				StringBuffer sb = new StringBuffer();
				while (true) {
					len = fr.read(charbuff, 0, 1024);
					if (len == -1)
						break;
					sb.append(charbuff, 0, len);

				}
				result = sb.toString();
				// System.out.println("result:" + result);
				fr.close();
				if (result != null) {
					reload.setVisibility(View.GONE);
					loadlocal.setVisibility(View.GONE);
					cates = parse(result);
					if (cates == null || cates.size() == 0)
						return;
					adapter = new CateAdapter(context, cates);
					cates.clear();
					listView.setAdapter(adapter);
					listView.setVisibility(View.VISIBLE);
				}
			}

			catch (IOException e) {
				e.printStackTrace();
				System.out.println("打开失败：" + filename + " " + e.getMessage());

			}

		}
	};
	OnClickListener returnOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	OnItemClickListener listItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {

			// �ļ���û������,�˳�
			Category cate;
			cate = (Category) adapter.getItem(position);
			if (!cate.isExist())
				return;

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			String filename = cate.getFilename();
			String ext = filename.substring(filename.lastIndexOf(".") + 1);
			File f = new File(filename);
			String mark = null;
			if (ext.equalsIgnoreCase("htm") || ext.equalsIgnoreCase("html")) {
				intent = new Intent();
				ArrayList<String> urls= new ArrayList<String> ();
				urls.add("file://" + cate.getFilename());
				intent.putStringArrayListExtra("urls", urls);
				intent.putExtra("position", 0);
				intent.putExtra("title", szTitle);
				intent.setClass(context, WebActivity.class);
				context.startActivity(intent);
				return;
			}

			else if (ext.equalsIgnoreCase("doc")
					|| ext.equalsIgnoreCase("docx"))
				mark = "application/msword";
			else if (ext.equalsIgnoreCase("xls")
					|| ext.equalsIgnoreCase("xlsx"))
				mark = "application/vnd.ms-excel";
			else if (ext.equalsIgnoreCase("ppt")
					|| ext.equalsIgnoreCase("pptx"))
				mark = "application/vnd.ms-powerpoint";
			else if (ext.equalsIgnoreCase("apk"))
				mark = "application/vnd.android.package-archive";
			intent.setDataAndType(Uri.fromFile(f), mark);
			startActivity(intent);

		}
	};

	private List<Category> parse(String xmlStr) {
		try {

			cates = new ArrayList<Category>();
			CategoryHandler categoryHandler = new CategoryHandler(cates);

			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			XMLReader xmlReader = saxParserFactory.newSAXParser()
					.getXMLReader();
			xmlReader.setContentHandler(categoryHandler);
			xmlReader.parse(new InputSource(new StringReader(xmlStr)));
			/*
			 * for (Iterator iterator = cates.iterator(); iterator.hasNext();) {
			 * Category category = (Category) iterator.next();
			 * System.out.println(category); }
			 */
		} catch (Exception e) {
			return null;
		}
		return cates;

	}

	private class getCateAsync extends AsyncTask<Void, Integer, String> {

		@Override
		protected void onPreExecute() {


			// 显示ProgressDialog
			progressDialog = ProgressDialog.show(OnlineActivity.this, "正在加载数据",
					"请稍等...", true, false);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			String result = "";

			result = HttpHelper.downLoadText(url);
			// д filelist.xml�� ����

			System.out.println("result" + result);
			return result;
		}

		@Override
		protected void onProgressUpdate(Integer... values) { // ������UI�ؼ�����
			// loading_top.setProgress(values[0]);
			// progressBar.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();
			createAllView(result);
			super.onPostExecute(result);
		}
	}

	private void createAllView(String result) {

		listView.setVisibility(View.GONE);
		if (result.equals(""))
			return;
		cates = parse(result);
		if (cates == null || cates.size() == 0)
			return;

		/*
		 * if (cates.size() < 1 || cates == null) {
		 * 
		 * AlertDialog.Builder localBuilder1 = new
		 * AlertDialog.Builder(OnlineActivity.this)
		 * .setTitle(R.string.app_name).setMessage("�����Ϸ�������");
		 * 
		 * localBuilder1.setPositiveButton("�˳�", new
		 * android.content.DialogInterface.OnClickListener() {
		 * 
		 * public void onClick( DialogInterface paramDialogInterface, int
		 * paramInt) { OnlineActivity.this.finish(); }
		 * 
		 * }
		 * 
		 * ).show(); return; }
		 */
		try {
			String filename = SDHelper.getAppDataPath() + File.separator
					+ "filelist.xml";
			FileWriter fw = new FileWriter(filename);

			fw.write(result);
			fw.close();
		}

		catch (IOException e) {
			e.printStackTrace();

		}

	    adapter = new CateAdapter(this, cates);
		listView.setVisibility(View.VISIBLE);
		listView.setAdapter(adapter);
		cates.clear();
	}
}

