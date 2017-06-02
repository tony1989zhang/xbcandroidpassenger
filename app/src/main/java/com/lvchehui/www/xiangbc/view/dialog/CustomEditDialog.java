package com.lvchehui.www.xiangbc.view.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.impl.OnOperationListener;
import com.lvchehui.www.xiangbc.utils.StringUtils;

public class CustomEditDialog extends Dialog implements View.OnClickListener {
	private OnOperationListener operationListener;
	private EditText edit;
	private InputMethodManager imm;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

	}
	public CustomEditDialog(Context context) {
		super(context, R.style.custom_dialog);
		this.context = context;
		setContentView(R.layout.custom_edit_dialog);
		findViewById(R.id.etDialogLeftBtn).setOnClickListener(this);
		findViewById(R.id.etDialogRightBtn).setOnClickListener(this);
		edit = (EditText) findViewById(R.id.dialogEdit);

		if (null == imm) {
			imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		}
		boolean isOpen = imm.isActive();
		if (!isOpen){
			imm.showSoftInput(edit,InputMethodManager.SHOW_FORCED);
		}
	}

	public CustomEditDialog(Context context, View.OnClickListener listener) {
		super(context, R.style.custom_dialog);
		this.context = context;
		setContentView(R.layout.custom_edit_dialog);
		findViewById(R.id.etDialogLeftBtn).setOnClickListener(this);
		findViewById(R.id.etDialogRightBtn).setOnClickListener(listener);
		edit = (EditText) findViewById(R.id.dialogEdit);

		if (null == imm) {
			imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		}
		boolean isOpen = imm.isActive();
		if (!isOpen){
			imm.showSoftInput(edit,InputMethodManager.SHOW_FORCED);
		}
	}

	public void setOperationListener(OnOperationListener operationListener) {
		this.operationListener = operationListener;
	}

	public void setEditText(CharSequence content) {
		edit.setText(content);
		if (StringUtils.isNotEmpty(content)) {
			if (content.toString().length() >= 20) {
				edit.setSelection(20);

			} else {

				edit.setSelection(content.toString().length());
			}
		}
	}

	public void setEditHint(CharSequence hint) {
		edit.setHint(hint);
	}

	public void setEditMax(int length) {
		edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
	}

	public void setEditInputType(int type) {
		// InputType.TYPE_CLASS_NUMBER 只能数字
		edit.setInputType(type);
	}

	public String getEditText() {
		return edit.getText().toString();
	}

	public void hideEditText() {
		edit.setVisibility(View.GONE);
	}

	public void showEditText() {
		edit.setVisibility(View.VISIBLE);
	}

	@Override
	public void setTitle(CharSequence title) {

		SpannableStringBuilder builder = new SpannableStringBuilder(title);
		ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.BLACK);
		builder.setSpan(redSpan, 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		((TextView) findViewById(R.id.dialogTitle)).setText(builder);

	}

	public void setTag(Object tag) {

		findViewById(R.id.dialogTitle).setTag(tag);
	}

	public Object getTag() {

		return findViewById(R.id.dialogTitle).getTag();
	}

	public void setMessage(CharSequence message) {
		((TextView) findViewById(R.id.dialogText)).setText(message);
	}

	public void setButtonsText(CharSequence left, CharSequence right) {

		((TextView) findViewById(R.id.etDialogLeftBtn)).setText(left);
		((TextView) findViewById(R.id.etDialogRightBtn)).setText(right);
	}

	@Override
	public void onClick(View v) {
		if (null == imm) {
			imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		}
		boolean isOpen = imm.isActive();
		switch (v.getId()) {
		case R.id.etDialogLeftBtn:
			if (isOpen){
				imm.hideSoftInputFromWindow(edit.getWindowToken(),InputMethodManager.SHOW_FORCED);
			}
			if (operationListener == null){
				this.cancel();}
			else {
				operationListener.onLeftClick();
			}
			break;

		case R.id.etDialogRightBtn:
			if (isOpen){
				imm.hideSoftInputFromWindow(edit.getWindowToken(),InputMethodManager.SHOW_FORCED);
			}
			if (operationListener == null) {

				this.cancel();
			}
			else {
				operationListener.onRightClick();
			}
			break;
		}
	}

	@Override
	public void show() {
		super.show();
	}

	public void showAndClearEdit() {
		setEditText("");
		super.show();
	}

}
