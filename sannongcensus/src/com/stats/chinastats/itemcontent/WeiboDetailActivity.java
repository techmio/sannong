package com.stats.chinastats.itemcontent;

import java.util.LinkedList;
import java.util.List;

import com.stats.chinastats.util.Configure;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.stats.chinastats.R;


public class WeiboDetailActivity extends Activity  implements OnClickListener, OnPageChangeListener, OnItemClickListener {
	ViewPager viewpager;ImageButton btn_back;ProgressBar loading_pb;
	LinearLayout[] linear_btns=new LinearLayout[5];int[] linear_ids={R.id.d_refresh,R.id.d_comment,R.id.d_forward,R.id.d_collect,R.id.d_more};
	LinkedList<View> view_List;ViewpageAdater adapter;

	ProgressBar loadPb;ProgressDialog progressDialog;boolean isLoadingMore=false;
	SharedPreferences sp_skin;boolean skin_id;
	
	boolean filterWeibo=true,LoadImgOrNot = false;
	
	private int intentTo=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp_skin = getSharedPreferences("skin", MODE_PRIVATE);Configure.inits(WeiboDetailActivity.this);
		skin_id = sp_skin.getBoolean("id", true);
		// =***************是否下载图片**************
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		String isLoadImgS = settings.getString("isload_pic", "1");
		setContentView(skin_id?R.layout.layout_weibo_detail2:R.layout.layout_weibo_detail);
		initWidght();
		
		loadPb.setVisibility(0);viewpager.setVisibility(8);
	    
	}
		public void initWidght(){
		if(skin_id){
			LinearLayout li_bg = (LinearLayout) findViewById(R.id.weibo_detail);
			li_bg.setBackgroundResource(Configure.images[getSharedPreferences("mysetup", 0).getInt("bg_id", 0)]);
		}
		btn_back =(ImageButton) findViewById(R.id.detail_back);btn_back.setOnClickListener(this);
		viewpager = (ViewPager) findViewById(R.id.detail_viewpager);
		loadPb =(ProgressBar) findViewById(R.id.detail_progress);
		view_List = new LinkedList<View>();
		for(int i=0;i<5;i++){
			linear_btns[i]=(LinearLayout) findViewById(linear_ids[i]);
			linear_btns[i].setOnClickListener(this);
		}
		TextView title = (TextView) findViewById(R.id.today_title);
		title.setText("username");
	}
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}

}












