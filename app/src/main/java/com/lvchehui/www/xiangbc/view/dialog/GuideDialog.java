package com.lvchehui.www.xiangbc.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.utils.PixelUtil;

/**
 * @author 张灿能
 * 引导效果 广告指导之类的
 */

public class GuideDialog extends Dialog implements View.OnClickListener {
	private ImageView ivTip;
	private RelativeLayout layoutGuide;
	private LayoutParams params;
	private int width, height,statusBarHeight;
	private Context context;

	public GuideDialog(Context context) {
		super(context, R.style.custom_full_dialog);
		this.context = context;
		setContentView(R.layout.dialog_guide);
		layoutGuide = (RelativeLayout) findViewById(R.id.layout_guide);
		ivTip = (ImageView) findViewById(R.id.iv_tip);
		layoutGuide.setOnClickListener(this);
		getDMWidthAndHeight();
	}


	private void getDMWidthAndHeight() {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		width = metrics.widthPixels;
		height = metrics.heightPixels;
		statusBarHeight = PixelUtil.getStatusHeight((Activity) context);
	}

	public void showTopAndLeft(View view,int drawableId) {
		Rect rect = new Rect();
		view.getGlobalVisibleRect(rect);
		ivTip.setImageResource(drawableId);
		params = (LayoutParams) ivTip.getLayoutParams();
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.bottomMargin =height- rect.top -rect.height()/2;
		params.rightMargin = width - rect.left-rect.width()/2;
		show();
	}
	public void showTopAndRight(View view,int drawableId){
		Rect rect = new Rect();
		view.getGlobalVisibleRect(rect);
		ivTip.setImageResource(drawableId);
		params = (LayoutParams) ivTip.getLayoutParams();
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.bottomMargin = height - rect.top -rect.height()/2;
		params.leftMargin = rect.right-rect.width()/2;
		show();
	}
	public void showBottomAndLeft(View view,int drawableId) {
		Rect rect = new Rect();
		view.getGlobalVisibleRect(rect);
		ivTip.setImageResource(drawableId);
		params = (LayoutParams) ivTip.getLayoutParams();
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.topMargin = rect.top - (rect.height()) / 2 ;
		params.rightMargin = width - rect.left;
		show();
	}
	public void showBottomAndRight(View view,int drawableId){
		Rect rect = new Rect();
		view.getGlobalVisibleRect(rect);
		ivTip.setImageResource(drawableId);
		params = (LayoutParams) ivTip.getLayoutParams();
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params.topMargin = rect.bottom-statusBarHeight-rect.height()/2;
		params.leftMargin = rect.right;
		show();
	}
	public void showMagrinTopAndLeft(int magrinTop,int magrinLeft,int drawableId){
		ivTip.setImageResource(drawableId);
		params = (LayoutParams) ivTip.getLayoutParams();
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params.topMargin = magrinTop;
		params.leftMargin = magrinLeft;
		show();
	}
	public void showMagrinTopAndRight(int magrinTop,int magrinRight,int drawableId){
		ivTip.setImageResource(drawableId);
		params = (LayoutParams) ivTip.getLayoutParams();
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.topMargin = magrinTop;
		params.rightMargin = magrinRight;
		show();
	}
	@Override
	public void onClick(View v) {
		if (this.isShowing()) {
			this.dismiss();
		}
	}

}
