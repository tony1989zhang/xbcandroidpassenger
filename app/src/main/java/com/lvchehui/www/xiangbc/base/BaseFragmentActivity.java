package com.lvchehui.www.xiangbc.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dialog.CustomProgressDialog;

import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.lang.ref.WeakReference;

public class BaseFragmentActivity extends FragmentActivity{
	private static final String TextView = null;
	private CustomProgressDialog mProgress;
	private View toastView;
	private Toast toast;

	public WeakReference<Activity> WriActivity = new WeakReference<Activity>(this);
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		App.getInstance().aliveActivitys.add(WriActivity);

//		SystemBarTintManager tintManager = new SystemBarTintManager(this);
//		tintManager.setStatusBarTintEnabled(true);
//		tintManager.setNavigationBarTintEnabled(true);
//		tintManager.setTintColor(getResources().getColor(R.color.title_bg_color));

		mProgress = CustomProgressDialog.createDialog(this);
		toastView = LayoutInflater.from(this).inflate(R.layout.toast_view, null);
		toast = Toast.makeText(this, null, Toast.LENGTH_LONG);
		toast.setView(toastView);

	}
	
	public void showProgressDailog() {
		showProgressDailog("请稍后");
	}

	public void showProgressDailog(String strMessage) {
		showProgressDialog(strMessage, false);
	}

	public void showProgressDialog(String strMessage, boolean isCancelAble) {

		if (null == mProgress) {
			return;
		}

		if (isCancelAble) {
			mProgress.setCancelable(true);
			mProgress.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					mProgress.dismiss();
				}
			});
		}

		mProgress.setMessage(strMessage);
		mProgress.show();
	}

	public void dismissProgressDialog() {
		if (null != mProgress && mProgress.isShowing()) {
			mProgress.dismiss();
		}
	}

	public void showToast(String hint) {
		showToast(hint, Toast.LENGTH_LONG);
	}

	public void showToast(String hint, int duration) {
		if (null == toast || null == toastView)
			return;

		TextView tv = (TextView) toastView.findViewById(R.id.toastMessage);
		tv.setText(hint);
		toast.setDuration(duration);
		toast.show();
	}

	@Event(value = { R.id.title_back_iv, R.id.title_right_tv}, type = View.OnClickListener.class)
	private void titleClick(View v) {
		switch (v.getId()) {
		case R.id.title_back_iv:
			finish();
			break;
		case R.id.title_right_tv:
			break;

		default:
			break;
		}
	}
	
	public void setTitleView(TitleView title_view, String title){
		setTitleView(title_view, title, null);
	}

	public void setTitleView(TitleView title_view, String title, String rightText){
		if(title_view == null)
			return;
		
		title_view.setTitle(title);
		title_view.setTitleBackVisibility(View.VISIBLE);
		if(null != rightText && !("").equals(rightText)){
			title_view.setTitleRightText(rightText);
		}else{
		
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		setContentView(R.layout.view_null);
		App.getInstance().aliveActivitys.remove(WriActivity);
		//App.getInstance().getRefWatcher(this).watch(this);//检测内存是否释放
	}

}
