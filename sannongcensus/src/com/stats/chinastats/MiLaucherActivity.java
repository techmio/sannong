package com.stats.chinastats;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.stats.chinastats.databean.ContentItem;
import com.stats.chinastats.myanimations.MyAnimations;
import com.stats.chinastats.myview.DragGridView;
import com.stats.chinastats.myview.ScrollLayout;
import com.stats.chinastats.path.AboutActivity;
import com.stats.chinastats.path.FeedbackActivity;
import com.stats.chinastats.path.HelpActivity;
import com.stats.chinastats.path.InstallActivity;
import com.stats.chinastats.path.SetActivity;
import com.stats.chinastats.util.Configure;
import com.stats.chinastats.util.Data;
import com.stats.chinastats.util.FileOperation;

public class MiLaucherActivity extends Activity {

	// LaucherDataBase database = new LaucherDataBase(MiLaucherActivity.this);

	private boolean areButtonsShowing;
	private RelativeLayout composerButtonsWrapper;
	private ImageView composerButtonsShowHideButtonIcon;
	private RelativeLayout composerButtonsShowHideButton;
	private UpdateManager updateMan;
	private ProgressDialog updateProgressDialog;

	ContentItem map_none = new ContentItem();
	ContentItem map_null = new ContentItem();
	ArrayList<ContentItem> addDate = new ArrayList<ContentItem>();// 每一页的数据
	/** GridView. */
	private ScrollLayout lst_views;
	private ImageButton btn_search;// int oldPage=1;
	private TextView tv_page;
	private ImageView runImage, delImage;
	float bitmap_width, bitmap_height;

	LinearLayout.LayoutParams param;

	TranslateAnimation left, right;
	Animation up, down;

	public static final int PAGE_SIZE = 6;
	public int PAGE_COUNT = 2, PAGE_CURRENT = 0;;
	ArrayList<DragGridView> gridviews = new ArrayList<DragGridView>();

	ArrayList<ArrayList<ContentItem>> lists = new ArrayList<ArrayList<ContentItem>>();// 全部数据的集合集lists.size()==countpage;
	ArrayList<ContentItem> lstDate = new ArrayList<ContentItem>();// 每一页的数据
	ContentItem map;

	SensorManager sm;
	SensorEventListener lsn;
	boolean isClean = false;
	Vibrator vibrator;
	int rockCount = 0;

	int addPosition = 0, addPage = 0;

	ImageButton btn_skin;
	SharedPreferences sp_skin;
	BroadcastReceiver setpositionreceiver;
	IntentFilter setPositionFilter;
	boolean finishCount = false;

	Class<?>[] classes = { AboutActivity.class, InstallActivity.class,
			HelpActivity.class, HelpActivity.class, FeedbackActivity.class,
			HelpActivity.class };
	ProgressDialog progressDialog;

	// 0227更新壁纸切换：
	BroadcastReceiver setbgreceiver;
	IntentFilter setbgFilter;
	private TextView bottom_str;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_milaucher);

		// MobclickAgent.setDebugMode(true);
		// MobclickAgent.setSessionContinueMillis(1000);
		// MobclickAgent.onError(this);
		// MobclickAgent.setAutoLocation(false);
		// UmengConstants.enableCacheInUpdate = false;
		// UMFeedbackService.enableNewReplyNotification(this,
		// NotificationType.AlertDialog);
		// MobclickAgent.setUpdateOnlyWifi(false);
		// MobclickAgent.update(this, 1000 * 60 * 60 * 24);// daily
		// MobclickAgent
		// .setDefaultReportPolicy(this, ReportPolicy.BATCH_AT_LAUNCH);
		// MobclickAgent.updateOnlineConfig(this);

		// 取出文字和图标

		for (int i = 0; i < Data.Item0.length; i++) {

			ContentItem item = new ContentItem();
			item.setText(Data.Item0[i]);
			item.setIcon(Data.Item0_icon[i]);
			item.setUrl(Data.urls[i]);
			lstDate.add(item);
		}

		// database.open();
		// lstDate = database.getLauncher();
		// addDate = lstDate;
		// database.close();

		// map_none.setText("none");
		// map_null.setText(null);
		if (lstDate.size() == 0)
			Toast.makeText(MiLaucherActivity.this, "网络有点不给力哦", 2200).show();

		// 显示第一页 数字
		init();
		initData();
		initPath();
		initBroadCast();
		initBgBroadCast();
		
	     
		for (int i = 0; i < Configure.countPages; i++) {
			lst_views.addView(addGridView(i));
		}

		lst_views.setPageListener(new ScrollLayout.PageListener() {
			@Override
			public void page(int page) {
				setCurPage(page);
			}
		});

		runImage = (ImageView) findViewById(R.id.run_image);
		setImageBgAndRun();

		delImage = (ImageView) findViewById(R.id.dels);

		// vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		// sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		// Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// lsn = new SensorEventListener() {
		// public void onSensorChanged(SensorEvent e) {
		// if (e.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
		// if (!isClean && rockCount >= 7) {
		// isClean = true;
		// rockCount = 0;
		// vibrator.vibrate(100);
		// CleanItems();
		// return;
		// }
		// float newX = e.values[SensorManager.DATA_X];
		// float newY = e.values[SensorManager.DATA_Y];
		// float newZ = e.values[SensorManager.DATA_Z];
		// // if ((newX >= 18 || newY >= 20||newZ >= 20 )&&rockCount<4)
		// // {
		// if ((newX >= 16 || newY >= 18 || newZ >= 18)
		// && rockCount % 2 == 0) {
		// rockCount++;
		// return;
		// }
		// if ((newX <= -16 || newY <= -18 || newZ <= -18)
		// && rockCount % 2 == 1) {
		// rockCount++;
		// return;
		// }
		//
		// }
		// }
		//
		// @Override
		// public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// // TODO Auto-generated method stub
		//
		// }
		// };
		//
		// sm.registerListener(lsn, sensor, SensorManager.SENSOR_DELAY_GAME);
		updateMan = new UpdateManager(this, appUpdateCb);
		updateMan.checkUpdate();

	}

	public void init() {
		// relate = (RelativeLayout) findViewById(R.id.relate);
		lst_views = (ScrollLayout) findViewById(R.id.views);


		tv_page = (TextView) findViewById(R.id.tv_page);
		tv_page.setText("1");
//		btn_search =(ImageButton) findViewById(R.id.btn_search);
//		btn_search.setOnClickListener(searchOnClickListener);
		//btn_search.setText("搜");
		 Configure.inits(MiLaucherActivity.this);
		param = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT);
		int height=Configure.getScreenHeight(this);
		float  desityDpi= Configure.densityDpi;
		float scale= desityDpi/160;
		param.rightMargin = 100;
		param.topMargin =(height - (int)(scale*600))/2;
		param.leftMargin = 20;
	}

	public void initData() {
		Configure.countPages = (int) Math.ceil(lstDate.size()
				/ (float) PAGE_SIZE);

		if (Configure.countPages == 0)
			return;
		lists = new ArrayList<ArrayList<ContentItem>>();
		for (int i = 0; i < Configure.countPages; i++) {
			lists.add(new ArrayList<ContentItem>());
			for (int j = PAGE_SIZE * i; j < (PAGE_SIZE * (i + 1) > lstDate
					.size() ? lstDate.size() : PAGE_SIZE * (i + 1)); j++)
				lists.get(i).add(lstDate.get(j));
		}
		boolean isLast = true;
		for (int i = lists.get(Configure.countPages - 1).size(); i < PAGE_SIZE; i++) {
			if (isLast) {
				// lists.get(Configure.countPages - 1).add(map_null);
				isLast = false;
			}
			// else
			// lists.get(Configure.countPages - 1).add(map_none);
		}
	}

	public void CleanItems() {
		lstDate = new ArrayList<ContentItem>();
		for (int i = 0; i < lists.size(); i++) {
			for (int j = 0; j < lists.get(i).size(); j++) {
				if (lists.get(i).get(j).getText() != null
						&& !lists.get(i).get(j).getText().equals("none")) {
					lstDate.add(lists.get(i).get(j));
				}
			}
		}
		initData();
		lst_views.removeAllViews();
		gridviews = new ArrayList<DragGridView>();
		for (int i = 0; i < Configure.countPages; i++) {
			lst_views.addView(addGridView(i));
		}
		isClean = false;
		lst_views.snapToScreen(0);
	}

	public void resetNull(int position) {
		if (getFristNonePosition(lists.get(position)) > 0
				&& getFristNullPosition(lists.get(position)) < 0) {
			lists.get(position).set(getFristNonePosition(lists.get(position)),
					map_null);
		}
		if (getFristNonePosition(lists.get(position)) < 0
				&& getFristNullPosition(lists.get(position)) < 0) {
			if (position == Configure.countPages - 1
					|| (getFristNullPosition(lists.get(lists.size() - 1)) < 0 && getFristNonePosition(lists
							.get(lists.size() - 1)) < 0)) {
				lists.add(new ArrayList<ContentItem>());
				lists.get(lists.size() - 1).add(map_null);
				for (int i = 1; i < PAGE_SIZE; i++)
					lists.get(lists.size() - 1).add(map_none);
				lst_views.addView(addGridView(Configure.countPages));
				Configure.countPages++;
			} else if (getFristNonePosition(lists.get(lists.size() - 1)) > 0
					&& getFristNullPosition(lists.get(lists.size() - 1)) < 0) {
				lists.get(lists.size() - 1).set(
						getFristNonePosition(lists.get(lists.size() - 1)),
						map_null);
				((DragGridAdapter) ((gridviews.get(lists.size() - 1))
						.getAdapter())).notifyDataSetChanged();
			}
		}
	}

	public int getFristNonePosition(ArrayList<ContentItem> array) {
		for (int i = 0; i < array.size(); i++) {
			if (array.get(i) != null && array.get(i).getText() != null
					&& array.get(i).getText().equals("none")) {
				return i;
			}
		}
		return -1;
	}

	public int getFristNullPosition(ArrayList<ContentItem> array) {
		for (int i = 0; i < array.size(); i++) {
			if (array.get(i) != null && array.get(i).getText() == null) {
				return i;
			}
		}
		return -1;
	}

	public LinearLayout addGridView(int i) {
		// if (lists.get(i).size() < PAGE_SIZE)
		// lists.get(i).add(null);

		LinearLayout linear = new LinearLayout(MiLaucherActivity.this);
		DragGridView gridView = new DragGridView(MiLaucherActivity.this);
		gridView.setAdapter(new DragGridAdapter(MiLaucherActivity.this,
				gridView, lists.get(i)));

		gridView.setNumColumns(2);
		gridView.setHorizontalSpacing(0);
		gridView.setVerticalSpacing(0);
		
		final int ii = i;
		final View view;
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0,final View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub

				// arg1.setBackgroundColor(Color.parseColor("#87CEFA"));
				
				String text = lists.get(ii).get(arg2).getText();

				Intent intent = new Intent();
				intent.putExtra("url", lists.get(ii).get(arg2).getUrl());
				intent.putExtra("name", lists.get(ii).get(arg2).getText());
				intent.putExtra("username", lists.get(ii).get(arg2)
						.getText());
			
				
				if (text != null && text.equals("none")) {
					return;
				}
				// else if (text == null) {
				// addPage = ii;addPosition = arg2;
				// intent.setClass(MiLaucherActivity.this,
				// AddItemActivity.class);
				// }
				// else if(text.equals("我的微博")){
				// if(Configure.N_USER_NAME==null ||
				// Configure.N_USER_NAME.equals("")){
				// intentToLogin();
				// return;
				// }
				// else{
				// intent.setClass(MiLaucherActivity.this,UserWeiboActivity.class);
				// }
				// }
				else if (text.equals("下载安装")) {
					intent.setClass(MiLaucherActivity.this,
							OnlineActivity.class);

				}
				else if (text.equals("离线收藏夹")) {
					intent.setClass(MiLaucherActivity.this,
							FavActivity.class);

				}
				else if (text.equals("关于软件")) {
					intent.setClass(MiLaucherActivity.this,
							AboutActivity.class);

				}
				else if (text.equals("搜索")) {
					intent.setClass(MiLaucherActivity.this,
							SearchActivity.class);

				}
				else {
					intent.setClass(MiLaucherActivity.this, NewsActivity.class);
				}
				
				Animation	go = AnimationUtils.loadAnimation(MiLaucherActivity.this,
						R.anim.grid_go);
	    		 arg1.startAnimation(go);
			
			
			
				
				
				
				
				startActivity(intent);
				overridePendingTransition(R.anim.anim_fromright_toup6,
						R.anim.anim_down_toleft6);
			}
		});
		gridView.setSelector(R.drawable.selector_null);
		gridView.setPageListener(new DragGridView.G_PageListener() {
			@Override
			public void page(int cases, int page) {
				switch (cases) {
				case 0:// 滑动页面
					lst_views.snapToScreen(page);
					setCurPage(page);
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							Configure.isChangingPage = false;
						}
					}, 800);
					break;
				case 1:// 删除按钮上来
					delImage.setBackgroundResource(R.drawable.del);
					delImage.setVisibility(0);
					delImage.startAnimation(up);
					break;
				case 2:// 删除按钮变深
					delImage.setBackgroundResource(R.drawable.del_check);
					Configure.isDelDark = true;
					break;
				case 3:// 删除按钮变淡
					delImage.setBackgroundResource(R.drawable.del);
					Configure.isDelDark = false;
					break;
				case 4:// 删除按钮下去
					delImage.startAnimation(down);
					break;
				case 5:// 松手动作
					delImage.startAnimation(down);
					lists.get(Configure.curentPage).add(Configure.removeItem,
							map_null);
					lists.get(Configure.curentPage).remove(
							Configure.removeItem + 1);
					((DragGridAdapter) ((gridviews.get(Configure.curentPage))
							.getAdapter())).notifyDataSetChanged();
					break;
				}
			}
		});
		gridView.setOnItemChangeListener(new DragGridView.G_ItemChangeListener() {
			@Override
			public void change(int from, int to, int count) {
				ContentItem toString = (ContentItem) lists.get(
						Configure.curentPage - count).get(from);

				lists.get(Configure.curentPage - count).add(from,
						(ContentItem) lists.get(Configure.curentPage).get(to));
				lists.get(Configure.curentPage - count).remove(from + 1);
				lists.get(Configure.curentPage).add(to, toString);
				lists.get(Configure.curentPage).remove(to + 1);

				((DragGridAdapter) ((gridviews
						.get(Configure.curentPage - count)).getAdapter()))
						.notifyDataSetChanged();
				((DragGridAdapter) ((gridviews.get(Configure.curentPage))
						.getAdapter())).notifyDataSetChanged();
			}
		});
		gridviews.add(gridView);
		linear.addView(gridView, param);
		return linear;
	}

	public void initBgBroadCast() {
		setbgreceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				setImageBgAndRun();
			}
		};
		setbgFilter = new IntentFilter("intentToBgChange");
		registerReceiver(setbgreceiver, setbgFilter);
	}

	public void runAnimation() {
		down = AnimationUtils.loadAnimation(MiLaucherActivity.this,
				R.anim.griditem_del_down);
		up = AnimationUtils.loadAnimation(MiLaucherActivity.this,
				R.anim.griditem_del_up);
		down.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				delImage.setVisibility(8);
			}
		});

		right = new TranslateAnimation(Animation.ABSOLUTE, 0f,
				Animation.ABSOLUTE, -bitmap_width
						+ Configure.getScreenWidth(MiLaucherActivity.this),
				Animation.ABSOLUTE, 0f, Animation.ABSOLUTE, 0f);
		left = new TranslateAnimation(Animation.ABSOLUTE, -bitmap_width
				+ Configure.getScreenWidth(MiLaucherActivity.this),
				Animation.ABSOLUTE, 0f, Animation.ABSOLUTE, 0f,
				Animation.ABSOLUTE, 0f);
		right.setDuration(25000);
		left.setDuration(25000);
		right.setFillAfter(true);
		left.setFillAfter(true);

		right.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				runImage.startAnimation(left);
			}
		});
		left.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				runImage.startAnimation(right);
			}
		});
		runImage.startAnimation(right);
	}

	public void setImageBgAndRun() {
		System.out.println(getSharedPreferences("mysetup", 0)
				.getInt("bg_id", 0) + "==");
		Bitmap bitmap = BitmapFactory.decodeStream(
				getResources().openRawResource(
						Configure.images[getSharedPreferences("mysetup", 0)
								.getInt("bg_id", 0)]), null, null);
		bitmap_width = bitmap.getWidth();
		bitmap_height = bitmap.getHeight();

		// if(bitmap_width<=screen_width || bitmap_height <=screen_height){
		Matrix matrix = new Matrix();
		float scaleW = (Configure.getScreenWidth(MiLaucherActivity.this) * 3 / 2)
				/ bitmap_width;
		System.out.println(scaleW + "==");
		float scaleH = Configure.getScreenHeight(MiLaucherActivity.this)
				/ bitmap_height;
		matrix.postScale(scaleW, scaleH);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		bitmap_width = bitmap.getWidth();
		bitmap_height = bitmap.getHeight();
		// }
		runImage.setImageBitmap(bitmap);
		runAnimation();
	}

	public void setCurPage(final int page) {
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
				tv_page.setText((page + 1) + "");
				tv_page.startAnimation(MyAnimations.getScaleAnimation(0.0f,
						1.0f, 1.0f, 1.0f, 300));
			}
		});
		tv_page.startAnimation(a);

	}

	UpdateManager.UpdateCallback appUpdateCb = new UpdateManager.UpdateCallback() {

		public void downloadProgressChanged(int progress) {
			if (updateProgressDialog != null
					&& updateProgressDialog.isShowing()) {
				updateProgressDialog.setProgress(progress);
			}

		}

		public void downloadCompleted(Boolean sucess, CharSequence errorMsg) {
			if (updateProgressDialog != null
					&& updateProgressDialog.isShowing()) {
				updateProgressDialog.dismiss();
			}
			if (sucess) {
				updateMan.update();
			} else {
				DialogHelper.Confirm(MiLaucherActivity.this,
						R.string.dialog_error_title,
						R.string.dialog_downfailed_msg,
						R.string.dialog_downfailed_btnnext,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								updateMan.downloadPackage();

							}
						}, R.string.dialog_downfailed_btnnext, null);
			}
		}

		public void downloadCanceled() {
			// TODO Auto-generated method stub

		}

		public void checkUpdateCompleted(Boolean hasUpdate,
				CharSequence updateInfo) {
			if (hasUpdate) {
				DialogHelper.Confirm(
						MiLaucherActivity.this,
						getText(R.string.dialog_update_title),
						getText(R.string.dialog_update_msg).toString()
								+ updateInfo
								+ getText(R.string.dialog_update_msg2)
										.toString(),
						getText(R.string.dialog_update_btnupdate),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								updateProgressDialog = new ProgressDialog(
										MiLaucherActivity.this);
								updateProgressDialog
										.setMessage(getText(R.string.dialog_downloading_msg));
								updateProgressDialog.setIndeterminate(false);
								updateProgressDialog
										.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
								updateProgressDialog.setMax(100);
								updateProgressDialog.setProgress(0);
								updateProgressDialog.show();

								updateMan.downloadPackage();
							}
						}, getText(R.string.dialog_update_btnnext), null);
			}

		}
	};

	public void initPath() {
		MyAnimations.initOffset(MiLaucherActivity.this);
		btn_skin = (ImageButton) findViewById(R.id.composer_button_sleep);
		sp_skin = getSharedPreferences("skin", MODE_PRIVATE);
		btn_skin.setBackgroundResource(sp_skin.getBoolean("id", true) ? R.drawable.composer_sleep
				: R.drawable.composer_sun);
		composerButtonsWrapper = (RelativeLayout) findViewById(R.id.composer_buttons_wrapper);
		composerButtonsShowHideButton = (RelativeLayout) findViewById(R.id.composer_buttons_show_hide_button);
		composerButtonsShowHideButtonIcon = (ImageView) findViewById(R.id.composer_buttons_show_hide_button_icon);
		//
		composerButtonsShowHideButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!areButtonsShowing) {
					composerButtonsShowHideButtonIcon
							.startAnimation(MyAnimations.getRotateAnimation(0,
									-270, 300));
					MyAnimations.startAnimationsIn(composerButtonsWrapper, 300);
				} else {
					composerButtonsShowHideButtonIcon
							.startAnimation(MyAnimations.getRotateAnimation(
									-270, 0, 300));
					MyAnimations
							.startAnimationsOut(composerButtonsWrapper, 300);
				}
				areButtonsShowing = !areButtonsShowing;
			}
		});
		for (int i = 0; i < composerButtonsWrapper.getChildCount(); i++) {
			final int position = i;
			composerButtonsWrapper.getChildAt(i).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							if (position == 5) {
								sp_skin.edit()
										.putBoolean("id",
												!sp_skin.getBoolean("id", true))
										.commit();
								btn_skin.setBackgroundResource(sp_skin
										.getBoolean("id", true) ? R.drawable.composer_sleep
										: R.drawable.composer_sun);
								Toast.makeText(
										MiLaucherActivity.this,
										!sp_skin.getBoolean("id", true) ? "已开启夜间模式"
												: "夜间模式已关闭", 3000).show();
							} else {
								Intent intent = new Intent(
										MiLaucherActivity.this,
										classes[position]);
								startActivity(intent);
								overridePendingTransition(
										R.anim.anim_fromright_toup6,
										R.anim.anim_down_toleft6);
							}
						}
					});
		}
		composerButtonsShowHideButton.startAnimation(MyAnimations
				.getRotateAnimation(0, 360, 200));
	}

	// public void intentToLogin(){
	// progressDialog = ProgressDialog.show(MiLaucherActivity.this,
	// "请稍等片刻...", "马上为您准备登录", true, true);
	// new Thread() {
	// public void run() {
	// System.setProperty("weibo4j.oauth.consumerKey",
	// Weibo.CONSUMER_KEY);
	// System.setProperty("weibo4j.oauth.consumerSecret",
	// Weibo.CONSUMER_SECRET);
	// String authUrl =null;
	// Weibo weibo = new Weibo();
	// RequestToken requestToken;
	// try {
	// requestToken = weibo
	// .getOAuthRequestToken("life://UserCheckActivity");
	// OAuthConstantBean.getInstance()
	// .setRequestToken(requestToken);
	// authUrl = requestToken
	// .getAuthenticationURL()
	// + "&display=mobile";
	// } catch (WeiboException e) {
	// e.printStackTrace();
	// }
	// Message msg = loginHandler.obtainMessage();
	// msg.obj=authUrl;
	// loginHandler.sendMessage(msg);
	// }
	// }.start();
	// }
	// Handler loginHandler = new Handler() {
	// public void handleMessage(Message msg) {
	// progressDialog.dismiss();
	// Intent intent = new Intent();
	// Bundle bundle = new Bundle();
	// bundle.putString("url",(String) msg.obj);
	// intent.putExtras(bundle);
	// intent.setClass(MiLaucherActivity.this,
	// UserLoginActivity.class);
	// startActivity(intent);
	// }
	// };
	// @Override
	// protected void onNewIntent(final Intent intent) {
	// progressDialog = ProgressDialog.show(MiLaucherActivity.this,
	// "请稍等片刻...", "授权验证中，马上为您跳转", true, true);
	// new Thread() {
	// public void run() {
	// try {
	// Uri uri = intent.getData();
	// if (uri == null) { return; }
	// Weibo weibo = OAuthConstantBean.getInstance().getWeibo();
	// RequestToken requestToken = OAuthConstantBean.getInstance()
	// .getRequestToken();
	// String nulls = uri.getQueryParameter("oauth_verifier");
	// if (nulls != null) {
	// AccessToken accessToken = requestToken
	// .getAccessToken(nulls);
	// weibo.setToken(accessToken.getToken(),
	// accessToken.getTokenSecret());
	// List<weibo4android.Status> statuses = null;
	// statuses = weibo.getUserTimeline();
	// if (statuses.size() != 0) {
	// SharedPreferences refreshtime = getSharedPreferences(
	// "sp_users", 0);
	// refreshtime.edit().putLong("UserId",accessToken.getUserId()).commit();
	// refreshtime.edit().putString("UserName",statuses.get(0).getUser().getName()).commit();
	// refreshtime.edit().putString("Token", accessToken.getToken()).commit();
	// refreshtime.edit().putString("TokenSecret",accessToken.getTokenSecret()).commit();
	// Configure.setUserWeibo(accessToken.getUserId(),statuses.get(0).getUser().getName(),accessToken.getToken(),accessToken.getTokenSecret());
	// } else {
	// Toast.makeText(getApplicationContext(), "授权失败,请重试",
	// Toast.LENGTH_LONG).show();
	// }
	// }
	// } catch (WeiboException e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// }
	// Message msg = ForwardHandler.obtainMessage();
	// ForwardHandler.sendMessage(msg);
	// }
	// }.start();
	//
	// }
	// Handler ForwardHandler = new Handler() {
	// public void handleMessage(Message msg) {
	// progressDialog.dismiss();
	// Intent intent = new Intent();
	// intent.setClass(MiLaucherActivity.this,
	// UserWeiboActivity.class);
	// startActivity(intent);
	// }
	// };
	
	

	public void initBroadCast() {
		setpositionreceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				final String str = intent.getStringExtra("text");
				boolean isExit = false;
				Configure.countPages = lists.size();
				for (int i = 0; i < lists.size(); i++) {
					for (int j = 0; j < lists.get(i).size(); j++) {
						if (lists.get(i).get(j).getText() != null
								&& lists.get(i).get(j).getText().equals(str)) {
							isExit = true;
							lists.get(i).add(j, map_null);
							lists.get(i).remove(j + 1);
							((DragGridAdapter) ((gridviews.get(i)).getAdapter()))
									.notifyDataSetChanged();
						}
					}
				}
				if (!isExit) {
					ContentItem item = new ContentItem();
					item.setText(str);
					// database.open();
					// item.setIcon(database.getItemsUrl(str));
					// database.close();
					if (lists.get(addPage).get(addPosition).getText() == null) {// 当前add位置是否已占有
						lists.get(addPage).set(addPosition, item);
						resetNull(lists.size() - 1);
						((DragGridAdapter) ((gridviews.get(addPage))
								.getAdapter())).notifyDataSetChanged();
					} else {
						if (getFristNonePosition(lists.get(lists.size() - 1)) > 0) {
							lists.get(lists.size() - 1)
									.set(getFristNonePosition(lists.get(lists
											.size() - 1)), item);
							resetNull(lists.size() - 1);
							((DragGridAdapter) ((gridviews
									.get(gridviews.size() - 1)).getAdapter()))
									.notifyDataSetChanged();
						} else if (getFristNullPosition(lists
								.get(lists.size() - 1)) > 0) {
							lists.get(lists.size() - 1)
									.set(getFristNullPosition(lists.get(lists
											.size() - 1)), item);
							resetNull(lists.size() - 1);
							((DragGridAdapter) ((gridviews
									.get(gridviews.size() - 1)).getAdapter()))
									.notifyDataSetChanged();
						} else {// 当前最后页面已经填满
							lists.add(new ArrayList<ContentItem>());
							lists.get(lists.size() - 1).add(item);
							for (int i = 1; i < PAGE_SIZE; i++)
								lists.get(lists.size() - 1).add(map_none);
							lst_views
									.addView(addGridView(Configure.countPages));
							Configure.countPages++;
						}
					}
				}
			}
		};
		setPositionFilter = new IntentFilter("intentToAddLauncher");
		registerReceiver(setpositionreceiver, setPositionFilter);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub\
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {
				if (finishCount) {

					// if(lstDate.size()==0){
					finish();
					return true;
					// }
					// progressDialog =
					// ProgressDialog.show(MiLaucherActivity.this, "请稍等片刻...",
					// "小夜正在努力的为您保存状态", true, true);
					// new Thread(){
					// public void run(){
					// LaucherDataBase database = new
					// LaucherDataBase(MiLaucherActivity.this);
					// database.open();
					// database.deleteLauncher();
					// for (int i = 0; i < lists.size(); i++) {
					// database.insertLauncher(lists.get(i));
					// }
					// database.close();
					// SharedPreferences settings =
					// PreferenceManager.getDefaultSharedPreferences(MiLaucherActivity.this);
					// boolean isClearImage =
					// settings.getBoolean("checkbox_clearimage", false);
					// if(isClearImage){
					// File f = new File("/sdcard/night_girls/weibos");
					// FileOperation.deleteFile(f);
					// f.delete();
					// }
					// sm.unregisterListener(lsn);
					// unregisterReceiver(setpositionreceiver);unregisterReceiver(setbgreceiver);
					// if
					// (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
					// clear(MiLaucherActivity.this.getCacheDir());
					// }
					// Message msg = finishHandler.obtainMessage();
					// finishHandler.sendMessage(msg);
					// }
					// }.start();
				} else {
					finishCount = true;
					Toast.makeText(MiLaucherActivity.this, "再按一次返回键退出", 2000)
							.show();
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							// finishCount=false;
						}
					}, 2000);
				}
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	private Handler finishHandler = new Handler() {
		public void handleMessage(Message msg) {
			progressDialog.dismiss();
			finish();
		}
	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		PAGE_COUNT = Configure.countPages;
		PAGE_CURRENT = Configure.curentPage;
		// MobclickAgent.onPause(this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Configure.countPages = PAGE_COUNT;
		Configure.curentPage = PAGE_CURRENT;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void clear(File cacheDir) {
		File[] files = cacheDir.listFiles();
		for (File f : files)
			f.delete();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// MobclickAgent.onResume(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// if (MobclickAgent.isDownloadingAPK()) {
			// warningDialog(this);
			// }
		}
		return super.onKeyDown(keyCode, event);
	}

	private void warningDialog(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("You are downloading apk, are you sure to exits ?");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				MiLaucherActivity.this.finish();
			}

		});
		builder.create();
		builder.show();
	}
}
