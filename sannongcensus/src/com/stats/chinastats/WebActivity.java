package com.stats.chinastats;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.stats.chinastats.database.DBAdapter;
import com.stats.chinastats.model.Category;
import com.stats.chinastats.myanimations.MyAnimations;
import com.stats.chinastats.util.Configure;
import com.stats.chinastats.util.Const;
import com.stats.chinastats.util.HttpHelper;
import com.stats.chinastats.util.SDHelper;
import com.stats.chinastats.wxapi.WXEntryActivity;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;

public class WebActivity extends Activity {
	private static int REQUESTCODE = 15;
	private WebView webview;
	private Context mContext;
	private TextView title;
	private String szTitle;
	private ArrayList<String> Ids;
	private ArrayList<String> urls;
	private ArrayList<String> szTitles;
	private ArrayList<String> imgSrcs;
	private String activityMark;
	private int position;
	private GridView gridView;
	private IWXAPI wxApi;  
	private DBAdapter db;
	ProgressBar progressbar;

	//TAB文字标题
	private String[] titles = new String[] { 
			"上一篇", "下一篇",
			"加入收藏", "分享微信",
			 "刷新"
			 };
	// 图片ID数组
	private int[] images = new int[] { R.drawable.b_back, R.drawable.b_forward,
			R.drawable.b_fav,R.drawable.b_share, R.drawable.b_refresh
	    };

	Button Btnrtn = null;

	public WebActivity() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		wxApi = WXAPIFactory.createWXAPI(this, Const.WX_AppId);  
		wxApi.registerApp(Const.WX_AppId);  
		mContext = this;
	    
		setContentView(R.layout.webpage);
		Intent intent = getIntent();
		Ids = intent.getStringArrayListExtra("Ids");
		urls = intent.getStringArrayListExtra("urls");
		szTitles = intent.getStringArrayListExtra("titles");
		imgSrcs = intent.getStringArrayListExtra("imgSrcs");
		position = intent.getIntExtra("position", 0);
		activityMark = intent.getStringExtra("activity");
		String url = CheckUrl(urls.get(position));
		
		szTitle = intent.getStringExtra("title");

		title = (TextView) findViewById(R.id.title);
		title.setText(szTitle);
		title.setTextSize(20);

		ImageButton go_back = (ImageButton) findViewById(R.id.top_back);

		go_back.setOnClickListener(returnOnClickListener);
	
		progressbar = (ProgressBar) findViewById(R.id.progressBar);
		
		webview = (WebView) findViewById(R.id.webview);

		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setSupportZoom(true);
		webview.getSettings().setBuiltInZoomControls(true);
		
		webview.loadUrl(url);
		webview.setWebViewClient(new HelloWebViewClient());
		webview.setDownloadListener(new MyWebViewDownLoadListener());
		
		gridView = (GridView) findViewById(R.id.tab_view);
		
//		if(urls.size()==1)
//		  gridView.setVisibility(View.INVISIBLE);
		
		TabAdapter adapter = new TabAdapter(titles, images, this);
		
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int pos, long id) {

				if(pos ==0) {  //上一篇
						if (position > 0) position--;
						else position = urls.size()-1;
					webview.clearView();
		            webview.loadUrl( CheckUrl(urls.get(position)));
		    		Animation a = MyAnimations.getScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f,
		    				300);
		    		a.setAnimationListener(new Animation.AnimationListener() {
		    			@Override
		    			public void onAnimationStart(Animation animation) {
		    			}

		    			@Override
		    			public void onAnimationRepeat(Animation animation) {
		    			}

		    			@Override
		    			public void onAnimationEnd(Animation animation) {
		    				// TODO Auto-generated method stub
		    				 webview.startAnimation(MyAnimations.getScaleAnimation(0.0f,
		    						1.0f, 1.0f, 1.0f, 300));
		    			}
		    		});
		    		 webview.startAnimation(a);

		            
		            
		            
		            
//		            
//		            Animation an =  (Animation) AnimationUtils.loadAnimation(
//		            		mContext, R.anim.slide_in_left);
//		            
//		            
//		            webview.setAnimation(an);
					
				}
				else if(pos ==1) {  //下一篇
					if (position  < urls.size()-1)
						position ++;
//					webview.setOutAnimation(this, R.anim.push_left_out);
					webview.clearView();
					webview.loadUrl( CheckUrl(urls.get(position)));
					
//					
//					webview.setInAnimation(this, R.anim.push_left_in);

					
					
					
					
		    		Animation a = MyAnimations.getScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f,
		    				300);
		    		a.setAnimationListener(new Animation.AnimationListener() {
		    			@Override
		    			public void onAnimationStart(Animation animation) {
		    			}

		    			@Override
		    			public void onAnimationRepeat(Animation animation) {
		    			}

		    			@Override
		    			public void onAnimationEnd(Animation animation) {
		    				// TODO Auto-generated method stub
		    				 webview.startAnimation(MyAnimations.getScaleAnimation(0.0f,
		    						1.0f, 1.0f, 1.0f, 300));
		    			}
		    		});
		    		 webview.startAnimation(a);
			  
			    }
				else if(pos == 2) {  //添加到收藏夹
					if(activityMark.equals("fav")){
				        Toast.makeText(WebActivity.this, "收藏夹中已存在！",   
				                Toast.LENGTH_LONG).show();   
					
					}
					else {
					 //打开数据库
				   db = new DBAdapter(mContext);
				   try {
					  db.open();
					 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss 收藏");
				     String datetime=format.format(new Date());
					long ret=db.insert(Ids.get(position),szTitles.get(position),urls.get(position), imgSrcs.get(position),datetime);
					if(ret==-1) {
				        Toast.makeText(WebActivity.this, "收藏夹中已存在！",   
				                Toast.LENGTH_LONG).show();   
					}
					else  {
						
						//下载URL页面内容 到本地
						
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
								  String tmpUrl= urls.get(position);
									int tmp = tmpUrl.lastIndexOf("/");
									 if(tmp>0) tmpUrl=tmpUrl.substring(tmp+1);
								   String path =SDHelper.getSDPATH()+ Const.PIC_CACH_PATH + "/" + tmpUrl;
								   
									HttpHelper.downloadFile(urls.get(position), path);
									
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							
						}).start();
				
				        Toast.makeText(WebActivity.this, "成功保存到收藏夹！",   
				                Toast.LENGTH_LONG).show(); 
					}
				   }
				   catch(SQLException except) {
				        Toast.makeText(WebActivity.this,except.getMessage(),   
				                Toast.LENGTH_LONG).show(); 
				        return;
					   
				   }
					db.close();
				 }
	
			    }
				else if(pos == 3) {  //分享微信
					wechatShare(urls.get(position),szTitles.get(position),"来自农普助手分享",0);
			    }
				else if (pos ==4 ) {
					webview.clearView();
		            webview.loadUrl( CheckUrl(urls.get(position)));
				
				}
			}
		});

	}
	
	public  String getFilename(String url) {
		int position = url.lastIndexOf("/");
		return url.substring(position + 1);
	}

	public String getExt(File f) {
		String s = f.getName();
		try {
			s = s.substring(s.lastIndexOf(".") + 1);
		} catch (Exception e) {
			s = "";
		}
		return s;
	}

	public  String getExt(String fileName) {
		try {
			fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
		} catch (Exception e) {
			fileName = "";
		}
		return fileName;
	}

	private class MyWebViewDownLoadListener implements DownloadListener{   
		 
        @Override  
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,   
	                                   long contentLength) {              
/*	            Uri uri = Uri.parse(url);   
	            Intent intent = new Intent(Intent.ACTION_VIEW, uri);   
	           startActivity(intent);       
	                  
*/	
        	//if(activityMark.equals("fav")) return ;
        	String filename =  SDHelper.getAppDataPath() + File.separator 
					+ getFilename(url);
        	File file =new File(filename);
        	if (file.exists()) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				String ext = getExt(file);
				String mark = null;
			    if (ext.equalsIgnoreCase("doc")||ext.equalsIgnoreCase("docx"))
					mark = "application/msword";
				else if (ext.equalsIgnoreCase("xls")||ext.equalsIgnoreCase("xlsx"))
					mark = "application/vnd.ms-excel";
				else if (ext.equalsIgnoreCase("ppt")||ext.equalsIgnoreCase("pptx"))
					mark = "application/vnd.ms-powerpoint";
				else if (ext.equalsIgnoreCase("pdf"))
					mark = "application/pdf";
				
				else if (ext.equalsIgnoreCase("apk"))
					mark = 	"application/vnd.android.package-archive";   
				intent.setDataAndType(Uri.fromFile(file), mark);
				mContext.startActivity(intent);

        	}
        	else 
        	   new  getFileAsync().execute(url);
        	
        	}   
    }  

	private class getFileAsync extends AsyncTask<Object, Integer, String> {
		String url;
      	String filename; 

		public getFileAsync() {
			super();
			// TODO Auto-generated constructor stub
			
		}

		@Override
		protected void onPreExecute() {

			progressbar.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(Object... params) {
			 url = (String) params[0];
			filename =  SDHelper.getAppDataPath() + File.separator 
					+ getFilename(url);

			int TIMEOUT = 5 * 10000;
			String result = "N";
			int maxlength = 0;
			InputStream inputstream = null;
			try {
				URL downloadUrl = new URL(url);
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
					File file = new File(filename);
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
			if (result.equals("N")) {
				Toast.makeText(mContext,   "不能打开：" + url, Toast.LENGTH_SHORT)
				.show();
			} else {
	        	File file =new File(filename);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				String ext = getExt(file);
				String mark = null;
			    if (ext.equalsIgnoreCase("doc")||ext.equalsIgnoreCase("docx"))
					mark = "application/msword";
				else if (ext.equalsIgnoreCase("xls")||ext.equalsIgnoreCase("xlsx"))
					mark = "application/vnd.ms-excel";
				else if (ext.equalsIgnoreCase("ppt")||ext.equalsIgnoreCase("pptx"))
					mark = "application/vnd.ms-powerpoint";
				else if (ext.equalsIgnoreCase("pdf"))
					mark = "application/pdf";
				
				else if (ext.equalsIgnoreCase("apk"))
					mark = 	"application/vnd.android.package-archive";   
				intent.setDataAndType(Uri.fromFile(file), mark);
				mContext.startActivity(intent);
		 }

			progressbar.setVisibility(View.GONE);
			super.onPostExecute(result);
		}
	}
	
private String CheckUrl(String url) {
		String tmpurl =url;
		 if( !HttpHelper.hasNetwork(mContext)){
			
			int p= tmpurl.lastIndexOf("/");
			if(p>0) tmpurl =tmpurl.substring(p+1);
			 tmpurl = "file://"+SDHelper.getSDPATH()+ Const.PIC_CACH_PATH + "/" + tmpurl;
		 }
		 return tmpurl;
	}
	/*** 
	 * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码） 
	 * @param flag(0:分享到微信好友，1：分享到微信朋友圈) 
	 */  
	private void wechatShare(String url, String title, String description ,int flag){  
		
		if (!wxApi.isWXAppInstalled()) {   
			        Toast.makeText(WebActivity.this, "您还未安装微信客户端",   
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

	OnClickListener returnOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			finish();
		}
	};
	OnClickListener goNextOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

//			Intent intent = new Intent();
//			intent.putStringArrayListExtra("urls", urls);
//			intent.putExtra("title", szTitle);
//
		if (position + 1 < urls.size())
				position ++;
//			intent.putExtra("position", position);
//			intent.setClass(mContext, Web2Activity.class);
//			mContext.startActivity(intent);

			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
			
        //     WebView view = (WebView) v;
			 webview.clearView();
             webview.loadUrl(urls.get(position));
          
			//finish();

		}
	};
	OnClickListener goPreOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
//
//			Intent intent = new Intent();
//			intent.putStringArrayListExtra("urls", urls);
//			intent.putExtra("title", szTitle);
			if (position > 0) position--;
				//position = position - 1;

//			intent.putExtra("position", position);
//			intent.setClass(mContext, Web2Activity.class);
//			mContext.startActivity(intent);
			overridePendingTransition(R.anim.slide_out_left,
					R.anim.slide_in_right);
           // WebView view = (WebView) v;
			webview.clearView();
            webview.loadUrl(urls.get(position));

		//	finish();

		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack(); // goBack()
			return true;
		}
		return super.onKeyDown(keyCode, event);
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

	}
//自定义适配器
class TabAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Picture> pictures;
	public TabAdapter(String[] titles, int[] images, Context context) {
		super();
		pictures = new ArrayList<Picture>();
		inflater = LayoutInflater.from(context);
		for (int i = 0; i < images.length; i++) {
			Picture picture = new Picture(titles[i], images[i]);
			pictures.add(picture);
		}
	}

	@Override
	public int getCount() {
		if (null != pictures) {
			return pictures.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return pictures.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.tab_view, null);
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.tab_text);
			viewHolder.image = (ImageView) convertView
					.findViewById(R.id.tab_image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.title.setText(pictures.get(position).getTitle());
		viewHolder.image.setImageResource(pictures.get(position).getImageId());
		return convertView;
	}

}

class ViewHolder {
	public TextView title;
	public ImageView image;
}

class Picture {
	private String title;
	private int imageId;

	public Picture() {
		super();
	}

	public Picture(String title, int imageId) {
		super();
		this.title = title;
		this.imageId = imageId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
}
