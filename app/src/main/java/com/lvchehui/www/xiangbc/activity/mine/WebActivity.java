package com.lvchehui.www.xiangbc.activity.mine;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.view.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_web)
public class WebActivity extends BaseActivity {

	/***
	 * zhang cn update
	 */
	@ViewInject(R.id.title_view)
	TitleView mTitleView;
	@ViewInject(R.id.activty_wb_content)
	WebView mWebView;
	@ViewInject(R.id.load_faild)
	TextView mLoadFailed;
	@ViewInject(R.id.loading)
	View mLoading;

	public static final String WEB_EXT_URL = "ext_url";
	public static final String WEB_EXT_TITLE = "ext_title";

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		mTitleView = null;
//		mWebView = null;
//		mLoadFailed = null;
//		mLoading = null;
		setContentView(R.layout.view_null);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		String url = this.getIntent().getStringExtra(WEB_EXT_URL);
		String title = this.getIntent().getStringExtra(WEB_EXT_TITLE);

		mTitleView.setTitle(title);
		mTitleView.setTitleBackVisibility(View.VISIBLE);

		
		mWebView.setWebViewClient(new LCHWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);

		mWebView.loadUrl(url);
	}

	private class LCHWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (null == url){
				return false;
			}

			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			mLoading.setVisibility(View.GONE);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			mLoading.setVisibility(View.VISIBLE);
			mLoadFailed.setVisibility(View.GONE);
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			mLoadFailed.setVisibility(View.VISIBLE);

		}
	}

	@Event(value ={R.id.load_faild},type = OnClickListener.class)
	private void webViewOnClick(View v){
		switch (v.getId()) {
		case R.id.load_faild:
			mWebView.reload();

		default:
			break;
		}

	}
}
