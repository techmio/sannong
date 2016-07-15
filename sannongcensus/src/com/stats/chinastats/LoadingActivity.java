package com.stats.chinastats;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;

import com.stats.chinastats.database.LaucherDataBase;
import com.stats.chinastats.databean.ContentItem;
import com.stats.chinastats.myanimations.MyAnimations;
import com.stats.chinastats.util.Configure;
import com.stats.chinastats.util.Data;

public class LoadingActivity extends Activity {
	//LaucherDataBase database = new LaucherDataBase(LoadingActivity.this);
	boolean isLaucher;
	boolean isFinish=false;
	public HashMap<Integer, String[]> map = new HashMap<Integer,String[]>();
	private TextView title;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_loading);
		title = (TextView) findViewById(R.id.loading_title);
		Animation a = MyAnimations.getScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f,
				1000);
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
				 title.startAnimation(MyAnimations.getScaleAnimation(0.0f,
						1.0f, 1.0f, 1.0f, 1000));
				 intentToMain();
			}
		});
		 title.startAnimation(a);

	//	Configure.inits(LoadingActivity.this);
	//	intentToMain();
	      //  checkItems();
	}
//	public void checkItems() {
//		database.open();
//		isLaucher = database.hasLauncher();
//		database.close();
//		if (!isLaucher) {// 没有预存列表数据
//			ArrayList<ContentItem> add_items = new ArrayList<ContentItem>();
//			ArrayList<ContentItem> laucher_items = new ArrayList<ContentItem>();
//
//			for (int j = 0; j < Data.Item0.length; j++) {
//				ContentItem item = new ContentItem();
//				item.setFrom_icon(Data.Items_icon[j]);
//				item.setFrom(Data.Items_text[0]);
//				item.setIcon(Data.Item0_icon[j]);
//				item.setText(Data.Item0[j]);
//				item.setChoice(true);
//				laucher_items.add(item);
//			}
//			map.put(0, Data.Item0);map.put(1, Data.Item1);map.put(2, Data.Item2);map.put(3, Data.Item3);map.put(4, Data.Item4);
//			map.put(5, Data.Item5);map.put(6, Data.Item6);map.put(7, Data.Item7);
//			for (int i = 0; i < Data.ItemCount; i++) {
//				for (int j = 0; j < map.get(i).length; j++) {
//					ContentItem item = new ContentItem();
//					item.setFrom_icon(Data.Items_icon[i]);
//					item.setFrom(Data.Items_text[i]);
//					item.setIcon(-1);
//					item.setText(map.get(i)[j]);
//					if(i==0) item.setChoice(true);
//					add_items.add(item);
//				}
//			}
//			database.open();
//			database.deleteItems();
//			database.insertItems(add_items);
//			database.insertLauncher(laucher_items);
//			database.close();
//
//			intentToMain();
//
//		} else {
//			intentToMain();
//		}
//	}

@Override
public boolean dispatchKeyEvent(KeyEvent event) {
	// TODO Auto-generated method stub\
	if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getRepeatCount() == 0) {
			isFinish=true;
			finish();
			return true;
		}
	}
	return super.dispatchKeyEvent(event);
}
	public void intentToMain(){
		if(!isFinish){
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent();
					intent.setClass(LoadingActivity.this, MiLaucherActivity.class);
					startActivityForResult(intent, 10);
					overridePendingTransition(R.anim.anim_fromright_toup6,
							R.anim.anim_down_toleft6);
					finish();
				}
			},3000);
		}
	}
	

}









