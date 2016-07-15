package com.stats.chinastats.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.stats.chinastats.R;

public class Data {
	
	public static String[] urls=  {
		"http://doc.hb.stats.cn/news/getSpecifyCategoryNewsXml?startnid=0&count=15&cid=1",
		"http://doc.hb.stats.cn/news/getSpecifyCategoryNewsXml?startnid=0&count=15&cid=2",
		"http://doc.hb.stats.cn/news/getSpecifyCategoryNewsXml?startnid=0&count=15&cid=3",
		"http://doc.hb.stats.cn/news/getSpecifyCategoryNewsXml?startnid=0&count=15&cid=4",
		"http://doc.hb.stats.cn/news/getSpecifyCategoryNewsXml?startnid=0&count=15&cid=5",
		"http://doc.hb.stats.cn/news/getSpecifyCategoryNewsXml?startnid=0&count=15&cid=6",
		"http://doc.hb.stats.cn/news/getSpecifyCategoryNewsXml?startnid=0&count=15&cid=7",
		"",
		"",
		"",
		"",
		""
	};
	public static final String UrlApk = "http://doc.hb.stats.cn/ip/apk/sannongcensus.apk";
	public static String startUrl=	"http://doc.hb.stats.cn/news";
	public static String  feedbackUrl="http://doc.hb.stats.cn/news/postComment";
	public static String helpUrl= "http://doc.hb.stats.cn/ip/sannongcensus_about.html";
	public static String szTitle = "全国农业普查-";
	public static String Item0[] = { 
		"普查新闻", "文件通知", "普查机构","领导讲话","工作简报",
		"普查动态", "二农普回顾","下载安装","离线收藏夹","搜索","关于软件"
		};
	
	
	public static int gridbackground[]={ R.drawable.red,R.drawable.orange,R.drawable.blue,
			R.drawable.red2,R.drawable.purple,
			R.drawable.blue1,R.drawable.blue,R.drawable.green,
			R.drawable.green,R.drawable.blue,R.drawable.red
			};

	public static int[] Item0_icon = { R.drawable.c_net_new,R.drawable.c_financial, R.drawable.c_user
		,R.drawable.c_love_channel,R.drawable.c_entertainment,R.drawable.c_technoledge,
		 R.drawable.c_net_new,R.drawable.c_financial, R.drawable.c_user
			,R.drawable.c_love_channel,R.drawable.c_entertainment,R.drawable.c_technoledge,};
	
	public static String  getCurVersion(Context ctx) {
		String	curVersion="";
		int curVersionCode=0;
		try {
			PackageInfo pInfo = ctx.getPackageManager().getPackageInfo(
					ctx.getPackageName(), 0);
			curVersion = pInfo.versionName;
		 	 curVersionCode = pInfo.versionCode;
		} catch (NameNotFoundException e) {
			Log.e("update", e.getMessage());
		
		}
	 return curVersion;
	}

}