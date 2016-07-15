package com.stats.chinastats.wxapi;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.stats.chinastats.WebActivity;
import com.stats.chinastats.util.Const;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/** 微信客户端回调activity示例 */  
            
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {  
    // IWXAPI 是第三方app和微信通信的openapi接口  
    private IWXAPI api;  
    private String result;
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        api = WXAPIFactory.createWXAPI(this, Const.WX_AppId,true);  
        api.handleIntent(getIntent(), this);  
        super.onCreate(savedInstanceState);  
    }  
    @Override  
    public void onReq(BaseReq arg0) {
    	this.finish();
//    	try {
//			Intent intent = new Intent(this,WebActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			this.startActivity(intent);
//		} catch (Exception e) {
//		}

    }  
  
    @Override  
    public void onResp(BaseResp resp) {  
        Log.i("QQQQQQQQQQQQ", "resp.errCode:" + resp.errCode + ",resp.errStr:"  
                + resp.errStr);  
        switch (resp.errCode) {  
        case BaseResp.ErrCode.ERR_OK:  
        	result = "分享成功";
            //分享成功  
            break;  
        case BaseResp.ErrCode.ERR_USER_CANCEL: 
        	result = "分享取消";
            //分享取消  
            break;  
        case BaseResp.ErrCode.ERR_AUTH_DENIED: 
        	result = "分享拒绝";
            //分享拒绝  
            break;  
        }  
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();   

        this.finish();   
    }  
}  
