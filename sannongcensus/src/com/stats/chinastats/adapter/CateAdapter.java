package com.stats.chinastats.adapter;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;

import com.stats.chinastats.R;
import com.stats.chinastats.WebActivity;
import com.stats.chinastats.model.Category;
import com.stats.chinastats.model.ItemInfo;
import com.stats.chinastats.util.Data;
import com.stats.chinastats.util.SDHelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;

public class CateAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<Category> cates;
	
	private String download_path;
	private String indexPath;
	Context context;
	ItemInfo itemInfo  =new ItemInfo();

	public CateAdapter(Context context, List<Category> cates) {
		download_path = SDHelper.getAppDataPath() + File.separator + "download";
		File file = new File(download_path);
		if (!file.exists()) {
			file.mkdir();
		}
		this.context = context;
		this.cates = new ArrayList<Category>();
		this.cates.addAll(cates);
		int length= cates.size();
		Category cate;
		
		for(int i=0;i<cates.size();i++)
		{
			cate=cates.get(i);
			String url =cate.getUrl();
			if (url.equals("0"))  {
				cate.setEnable(false);
			}
			else { 
				cate.setEnable(true);
			}
			
			String fileName = download_path + File.separator
					+ getFilename(url);
			cate.setFilename(fileName);
			file = new File(fileName);
			cate.setExist(file.exists());
			
		}
		layoutInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return cates.size();
	}
	public boolean isEnabled(int position) {
		
	   Category cate;
		cate = cates.get(position);
		return cate.isEnable();
	/*	if (cate.getUrl().equals("0")) {
			return false;
		}
		return super.isEnabled(position);
	*/
		}

	public Object getItem(int position) {
		return cates.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Category cate;
		cate = cates.get(position);
		ViewHolder viewHolder;
		viewHolder = new ViewHolder();
		//if (convertView == null) {
		//	viewHolder = new ViewHolder();
			if (!cate.isEnable()) {
				convertView = layoutInflater.inflate(
						R.layout.group_list_item_tag, null);
			} else {
				convertView = layoutInflater.inflate(R.layout.group_list_item,
						null);
				viewHolder.icon = (ImageView) convertView
						.findViewById(R.id.group_list_item_image);
				viewHolder.button = (Button) convertView
						.findViewById(R.id.group_list_item_button);
				viewHolder.button.setOnClickListener(new lvButtonListener(
						position));
					File file = new File(cate.getFilename());
				String ext = getExt(file);
				
				int resid = R.drawable.web;
				if (ext.equalsIgnoreCase("apk"))
					resid = R.drawable.apk;
				else if (ext.equalsIgnoreCase("doc")||ext.equalsIgnoreCase("docx"))
					resid = R.drawable.msword_logo;
				else if (ext.equalsIgnoreCase("xls")||ext.equalsIgnoreCase("xlsx"))
					resid = R.drawable.excel_logo;
				else if (ext.equalsIgnoreCase("ppt")||ext.equalsIgnoreCase("pptx"))
					resid = R.drawable.powerpoint_logo;
				else if (ext.equalsIgnoreCase("pdf"))
					resid = R.drawable.pdf;
				else if (ext.equalsIgnoreCase("htm")
						|| ext.equalsIgnoreCase("html"))
					resid = R.drawable.web;

				viewHolder.icon.setBackgroundResource(resid);

				if (cate.isExist()) {
		
					if (ext.equalsIgnoreCase("apk"))
						viewHolder.button.setText("安装");

					else
						viewHolder.button.setText("打开");

				}
			}
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.group_list_item_text);
	//		convertView.setTag(viewHolder);
	//	} else {
	//		viewHolder = (ViewHolder) convertView.getTag();
	//	}

		viewHolder.title.setText(cate.getName());
		// convertView.setBackgroundColor(Color.rgb(200, 200, 200));
		return convertView;

	}

	class lvButtonListener implements View.OnClickListener {
		private int position;
		Category cate;
		Button button;
		ProgressBar progressbar;

		lvButtonListener(int pos) {
			position = pos;
			cate = cates.get(position);
	}

		@Override
		public void onClick(View v) {
			if (cate.isExist()) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				File f = new File(cate.getFilename());
				String ext = getExt(f);
				String mark = null;
				if(ext.equalsIgnoreCase("htm")||ext.equalsIgnoreCase("html"))
				{
					 intent = new Intent();
					 ArrayList<String> urls= new ArrayList<String> ();
						urls.add("file://" + cate.getFilename());
						intent.putStringArrayListExtra("urls", urls);
						intent.putExtra("position", 0);
						intent.putExtra("title", Data.szTitle+"下载安装");
					intent.setClass(context, WebActivity.class);
					context.startActivity(intent);
					return ;

				}
				else if (ext.equalsIgnoreCase("doc")||ext.equalsIgnoreCase("docx"))
					mark = "application/msword";
				else if (ext.equalsIgnoreCase("xls")||ext.equalsIgnoreCase("xlsx"))
					mark = "application/vnd.ms-excel";
				else if (ext.equalsIgnoreCase("ppt")||ext.equalsIgnoreCase("pptx"))
					mark = "application/vnd.ms-powerpoint";
				else if (ext.equalsIgnoreCase("pdf"))
					mark = "application/pdf";
				
				else if (ext.equalsIgnoreCase("apk"))
					mark = 	"application/vnd.android.package-archive";   
				intent.setDataAndType(Uri.fromFile(f), mark);
				context.startActivity(intent);
				return;
			}
			String url = cate.getUrl();
			button = (Button) v;
			View parent = (View) v.getParent();
			progressbar = (ProgressBar) parent.findViewById(R.id.progressBar);

			Toast.makeText(context, "下载：" + cate.getName(), Toast.LENGTH_SHORT)
					.show();
			//Toast.makeText(context, "position:" + position, Toast.LENGTH_SHORT)
			//.show();

			new getFileAsync(button, progressbar, cate,position).execute(url);
		}
	}

	public static String getFilename(String url) {
		int position = url.lastIndexOf("/");
		return url.substring(position + 1);
	}

	public static String getExt(File f) {
		String s = f.getName();
		try {
			s = s.substring(s.lastIndexOf(".") + 1);
		} catch (Exception e) {
			s = "";
		}
		return s;
	}

	public static String getExt(String fileName) {
		try {
			fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
		} catch (Exception e) {
			fileName = "";
		}
		return fileName;
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

	public static String replaceChineseUrl(String url) {
		String filename = getFilename(url);
		String str = encode(filename);
		return url.replace(filename, str);
	}

	private class getFileAsync extends AsyncTask<Object, Integer, String> {
		Button button;
		ProgressBar progressbar;
		Category cate;
		int position;

		public getFileAsync(Button button, ProgressBar progressbar,
				Category cate,int pos) {
			super();
			// TODO Auto-generated constructor stub
			this.cate = cate;
			this.button = button;
			this.progressbar = progressbar;
			this.position = pos;
			
		}

		@Override
		protected void onPreExecute() {

			button.setVisibility(View.GONE);
			progressbar.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(Object... params) {
			String url = (String) params[0];

			int TIMEOUT = 5 * 10000;
			String result = "N";
			int maxlength = 0;
			InputStream inputstream = null;
			try {
				URL downloadUrl = new URL(replaceChineseUrl(url));
				HttpURLConnection httpConn = (HttpURLConnection) downloadUrl
						.openConnection();
				httpConn.setConnectTimeout(TIMEOUT);

				httpConn.setRequestMethod("GET");
				// 以GET方式连接
				if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					maxlength = httpConn.getContentLength();
					if (maxlength == 0)
						return result;
					inputstream = httpConn.getInputStream();
					String filename = download_path + File.separator
							+ getFilename(url);
					File file = new File(cate.getFilename());
					file.createNewFile();
					RandomAccessFile raf = new RandomAccessFile(file, "rw");
					byte[] buf = new byte[1024];
					int length = 0;
					int sum = 0;
					while ((length = inputstream.read(buf, 0, 1024)) != -1) {
						raf.write(buf, 0, length);
						sum += length;
						float ratio = (float) sum / (float) maxlength * 100;
						publishProgress((int) ratio);
					}
					inputstream.close();
					raf.close();

					result = "Y";

				}
			} catch (Exception ex) {
				ex.printStackTrace();
				result = "N";
			}
			return result;
		}

		@Override
		protected void onProgressUpdate(Integer... values) { // 可以与UI控件交互
			progressbar.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			button.setVisibility(View.VISIBLE);
			if (result.equals("N")) {
				button.setText("下载失败");
				cate.setExist(false);
				Toast.makeText(context,  cate.getName()+ " 下载失败" , Toast.LENGTH_SHORT)
				.show();
			} else {
				String ext = getExt(cate.getName());
				if (ext.equalsIgnoreCase("apk"))
						button.setText("安装");
				else 		
				     button.setText("打开");
				cate.setExist(true);
		    	Toast.makeText(context, cate.getName()+" 下载成功！", Toast.LENGTH_SHORT)
				.show();
		    	//刷新列表
		    	
				notifyDataSetChanged();

		    	//建索引
		    	

			}

			progressbar.setVisibility(View.GONE);
			super.onPostExecute(result);
		}
	}

	private class ViewHolder {
		ImageView icon;
		TextView title;
		Button button;
	}

}
