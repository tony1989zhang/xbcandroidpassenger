package com.lvchehui.www.xiangbc.view.tag;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

import com.lvchehui.www.xiangbc.R;

public class TagView2 extends ToggleButton {

	private boolean mCheckEnable = true;

	public TagView2(Context paramContext) {
		super(paramContext);
		init();
	}

	public TagView2(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		init();
	}

	public TagView2(Context paramContext, AttributeSet paramAttributeSet,
					int paramInt) {
		super(paramContext, paramAttributeSet, 0);
		init();
	}

	private void init() {
		setTextOn(null);
		setTextOff(null);
		setText("");
		setBackgroundResource(R.drawable.tag_bg_selector);
	}

	public void setCheckEnable(boolean paramBoolean) {
		this.mCheckEnable = paramBoolean;
		if (!this.mCheckEnable) {
			super.setChecked(false);
		}
	}

	public void setChecked(boolean paramBoolean) {
		if (this.mCheckEnable) {
			super.setChecked(paramBoolean);
		}
	}
}
