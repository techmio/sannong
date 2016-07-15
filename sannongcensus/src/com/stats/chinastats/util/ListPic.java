package com.stats.chinastats.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import com.stats.chinastats.R;
import com.stats.chinastats.util.ImageTool;

public class ListPic {
	
	private ImageView view;
	String img_path;
	String img_short;
	int mWidth;
	String url;
	Bitmap img,scale_img;
	
	private boolean down_result;
	
	private class UpdateUI implements Runnable
	{
		@Override
		public void run() {
			if(down_result)
			{
				img = BitmapFactory.decodeFile(img_path);
				
				scale_img=ImageTool.createBitmapBySize(img, mWidth, (int)(mWidth*img.getHeight()/img.getWidth()));
				view.setImageBitmap(scale_img);
				view.setTag(R.id.toppic,"downloaded");
			}
			else
			{
				//view.setImageResource(R.drawable.default_pic);
			}
		}
		
	}
	
	public ListPic(ImageView view,String url,int mWidth)
	{
		this.view=view;
		this.url = url;
		this.mWidth = mWidth*(int)Configure.screenDensity ;
		this.img_path= SDHelper.getSDPATH()+ Const.PIC_CACH_PATH ;
		File file = new File(img_path);
		
		if (!file.exists()) {  //����Ŀ¼
			file.mkdirs();
		}
	
         img_path = img_path + File.separator + GetPicId(url);
	
	}
	
	public void getPic()
	{
		//if(view.getTag(R.id.toppic)!=null)//�Ѿ���ͼƬ
	//	{
	//		view.setImageBitmap(BitmapFactory.decodeFile(img_path));
	//	}
	//	else
		{
				
			if(SDHelper.checkFileExists( img_path))
			{
				img = BitmapFactory.decodeFile(img_path);
				
				scale_img=ImageTool.createBitmapBySize(img, mWidth, (int)(mWidth*img.getHeight()/img.getWidth()));
				view.setImageBitmap(scale_img);
	//			view.setImageBitmap(BitmapFactory.decodeFile(img_path));
			}
			else
			{
				//���߳�����ͼƬ
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
						//	System.out.println("����:"+ url +",����:"+img_path);
							down_result=HttpHelper.downloadFile(replaceChineseUrl(url), img_path);
							// ���µĲ������UI�߳���
							Activity a = (Activity)view.getContext();
							a.runOnUiThread(new UpdateUI());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				}).start();
			}
		}
	}
	public static String GetPicId(String url)
	{
		int position=url.lastIndexOf("/");
		return url.substring(position+1);
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
		String filename = GetPicId (url);
		String str = encode(filename);
		return url.replace(filename, str);
	}
	
}
