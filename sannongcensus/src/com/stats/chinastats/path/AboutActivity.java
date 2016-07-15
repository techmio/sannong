package com.stats.chinastats.path;


import com.stats.chinastats.R;
import com.stats.chinastats.util.Const;
import com.stats.chinastats.util.Data;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class AboutActivity extends Activity {
	
	String curVersion;
	int curVersionCode;
	Context ctx;
	private WebView webview;
	
	private IWXAPI wxApi; 
	private Context mContext;
	private ImageButton share_sent;
	private TextView bottom_str;
	

	
	protected void onCreate(Bundle paramBundle) {

		super.onCreate(paramBundle);
		setContentView(R.layout.layout_about);
		wxApi = WXAPIFactory.createWXAPI(this, Const.WX_AppId);  
		wxApi.registerApp(Const.WX_AppId);  

		ImageButton back = (ImageButton) findViewById(R.id.topleftimage);
		back.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AboutActivity.this.setResult(RESULT_OK, getIntent());
				AboutActivity.this.finish();
				overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
			}
		});
		share_sent = (ImageButton) findViewById(R.id.share_sent);
		share_sent.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				wechatShare(Data.helpUrl, "统计系统移动终端软件--农普助手介绍","来自农普助手分享",0);
			}
		});

		ctx= this;
		webview = (WebView) findViewById(R.id.webview);

		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setSupportZoom(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.loadUrl(Data.helpUrl);
		webview.setWebViewClient(new HelloWebViewClient());
		webview.setDownloadListener(new MyWebViewDownLoadListener());
			String vercode = Data.getCurVersion(this);
			bottom_str = (TextView )findViewById(R.id.bottom_str);
			bottom_str.setText(Html.fromHtml("湖北省统计局  &nbsp;&nbsp;"+"<font color=red>"+vercode+"</font>&nbsp;&nbsp;版"));

	
	
	}
	private class MyWebViewDownLoadListener implements DownloadListener{   
		 
        @Override  
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,   
	                                   long contentLength) {              
	            Uri uri = Uri.parse(url);   
	            Intent intent = new Intent(Intent.ACTION_VIEW, uri);   
	           startActivity(intent);              
	      }   
    }  


	private void wechatShare(String url, String title, String description ,int flag){  
		
		if (!wxApi.isWXAppInstalled()) {   
			        Toast.makeText(AboutActivity.this, "您还未安装微信客户端",   
			                Toast.LENGTH_LONG).show();   
			        return;   
			    }  
	    WXWebpageObject webpage = new WXWebpageObject();  
	    webpage.webpageUrl = url;  
	    WXMediaMessage msg = new WXMediaMessage(webpage);  
	    msg.title = title;  
	    msg.description = description;  
	    //这里替换一张自己工程里的图片资源  
	    Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);  
	    msg.setThumbImage(thumb);  
	      
	    SendMessageToWX.Req req = new SendMessageToWX.Req();  
	    req.transaction = String.valueOf(System.currentTimeMillis());  
	    req.message = msg;  
	    req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;  
	    wxApi.sendReq(req);  
	}  
	// Web��ͼ
	private class HelloWebViewClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			/*
			 * if(view.canGoBack()) { btn_prev.setGravity(gravity)(true) ; }
			 * else btn_prev.setActivated(false) ;
			 * 
			 * if(view.canGoForward()) { btn_next.setActivated(true) ; } else
			 * btn_next.setActivated(false) ;
			 */}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

	}
	private void getCurVersion() {
		try {
			PackageInfo pInfo = ctx.getPackageManager().getPackageInfo(
					ctx.getPackageName(), 0);
			curVersion = pInfo.versionName;
			curVersionCode = pInfo.versionCode;
		} catch (NameNotFoundException e) {
			Log.e("update", e.getMessage());
			curVersion = "2.0";
			curVersionCode = 2;
		}
	//	versioninfo="<font color='red'>V"+curVersion+"</font> " +versioninfo;
	}

	// 退出
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
			return false;
		}
		return false;
	 }
}

