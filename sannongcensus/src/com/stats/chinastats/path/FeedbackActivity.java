/**
 * 
 */
package com.stats.chinastats.path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.stats.chinastats.R;
import com.stats.chinastats.WebActivity;
import com.stats.chinastats.adapter.NewsAdapter;
import com.stats.chinastats.model.Parameter;
import com.stats.chinastats.util.Data;
import com.stats.chinastats.util.HttpHelper;
import com.stats.chinastats.util.HttpUtil;
import com.stats.chinastats.util.SyncHttp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Administrator
 *
 */
public class FeedbackActivity extends Activity {
	private Button feeback_sent;
	private EditText edit_feed;
	private TextView bottom_str;
	private Context context;

	protected void onCreate(Bundle paramBundle) {

		super.onCreate(paramBundle);
		setContentView(R.layout.layout_more_feeback);
		context =  this;
		ImageButton back = (ImageButton) findViewById(R.id.topleftimage);
		
		back.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FeedbackActivity.this.setResult(RESULT_OK, getIntent());
				FeedbackActivity.this.finish();
				overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
			}
		});
		
		feeback_sent = (Button) findViewById(R.id.feeback_sent);
		feeback_sent.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				new PostCommentAsync().execute();
				//Thread th= new PostCommentThread();
				//th.start();
				//th.run();
				}
		});
		edit_feed = (EditText)findViewById(R.id.edit_feed);
		String vercode = Data.getCurVersion(this);
		bottom_str = (TextView )findViewById(R.id.bottom_str);
		bottom_str.setText(Html.fromHtml("湖北省统计局 &nbsp;&nbsp; "+"<font color=red>"+vercode+"</font>&nbsp;&nbsp;版"));

	}
	
	private class PostCommentAsync extends AsyncTask<Void, Integer, String> {

		@Override
		protected void onPreExecute() {
//			// 显示ProgressDialog
//
//			progressDialog = ProgressDialog.show(context, "正在加载数据", "请稍等...",
//					true, false);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			String result = null;

			SyncHttp syncHttp = new SyncHttp();
			String url=Data.feedbackUrl;
			List<Parameter> Params = new ArrayList<Parameter>();
			Params.add(new Parameter("nid",  "500"));
			Params.add(new Parameter("region", "未知地区"));
			Params.add(new Parameter("content", edit_feed.getText().toString()));
			try
			{
				result = syncHttp.httpPost(url, Params);
//					JSONObject jsonObject = new JSONObject(retStr);
//				int retCode = jsonObject.getInt("ret");
//				if (0 == retCode)
//				{
//			        Toast.makeText(FeedbackActivity.this, "意见反馈已成功提交！",   
//			                Toast.LENGTH_LONG).show(); 
//			}

			} catch (Exception e)
			{
				e.printStackTrace();
			}

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
//			progressDialog.dismiss();
	        Toast.makeText(context, "意见反馈已成功提交！",   
	                Toast.LENGTH_LONG).show(); 
	        finish();
			super.onPostExecute(result);
		}
	}

	
	
	private class PostCommentThread extends Thread
	{
		@Override
		public void run()
		{
			SyncHttp syncHttp = new SyncHttp();
			String url=Data.feedbackUrl;
			List<Parameter> params = new ArrayList<Parameter>();
			params.add(new Parameter("nid",  "500"));
			params.add(new Parameter("region", "未知地区"));
			params.add(new Parameter("content", edit_feed.getText().toString()));
			try
			{
				String retStr = syncHttp.httpPost(url, params);
//					JSONObject jsonObject = new JSONObject(retStr);
//				int retCode = jsonObject.getInt("ret");
//				if (0 == retCode)
//				{
//			        Toast.makeText(FeedbackActivity.this, "意见反馈已成功提交！",   
//			                Toast.LENGTH_LONG).show(); 
//			}

			} catch (Exception e)
			{
				e.printStackTrace();
			}
	        Toast.makeText(context, "意见反馈已成功提交！",   
	                Toast.LENGTH_LONG).show(); 
	        finish();
		}
	}

}
