package com.stats.chinastats.path;

import com.stats.chinastats.R;
import com.stats.chinastats.WebActivity;
import com.stats.chinastats.util.Const;
import com.stats.chinastats.util.Data;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class InstallActivity extends Activity {

	private TextView bottom_str;

	protected void onCreate(Bundle paramBundle) {

		super.onCreate(paramBundle);
		setContentView(R.layout.layout_more_install);

		ImageButton back = (ImageButton) findViewById(R.id.topleftimage);
		
		back.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InstallActivity.this.setResult(RESULT_OK, getIntent());
				InstallActivity.this.finish();
				overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
			}
		});
		String vercode = Data.getCurVersion(this);
		bottom_str = (TextView )findViewById(R.id.bottom_str);
		bottom_str.setText(Html.fromHtml("湖北省统计局 &nbsp;&nbsp; "+"<font color=red>"+vercode+"</font>&nbsp;&nbsp;版"));
	
	}

}
