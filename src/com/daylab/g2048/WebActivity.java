package com.daylab.g2048;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebActivity extends Activity{
	WebView mWebView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		mWebView = new WebView(this);
		mWebView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setContentView(mWebView);
		
		WebSettings webSettings = mWebView.getSettings();
        webSettings.setBuiltInZoomControls(false);// 设置放大缩小
        webSettings.setSupportZoom(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
		String url = "file:///android_asset/index.html";
		mWebView.loadUrl(url);
	}
}
