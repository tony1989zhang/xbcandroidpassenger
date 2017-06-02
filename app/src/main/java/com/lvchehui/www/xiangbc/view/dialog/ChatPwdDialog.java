package com.lvchehui.www.xiangbc.view.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.utils.StringUtils;


/**
 * @author 微信密码框
 */
public class ChatPwdDialog extends Dialog implements View.OnClickListener {

	private IChatPwdBack mWayBack;
	// /**
	// * 标题
	// */
	// private TextView mTvTitle;
	/**
	 * 
	 */
	private ImageView mImgWay;
	/**
	 * 
	 */
	private TextView mTvValue;
	/**
	 * 账号
	 */
	// private TextView mTvAccount;
	/**
	 * 密码输入框
	 */
	private PasswordInputView mPwd;
	// private View mLinWay;
	/**
	 * 小标题
	 */
	private TextView mTvTitle2;
	private TextView tipTV;

	public ChatPwdDialog(Context context) {
		super(context, R.style.custom_dialog);
		this.setContentView(R.layout.chatpwd_dialog);
		WindowManager windowManager = ((Activity) context).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = (int) (display.getWidth() / 7 * 6); // 设置宽度
		getWindow().setAttributes(lp);
		findViewById(R.id.img_dialog_cancel).setOnClickListener(this);
		tipTV = (TextView) findViewById(R.id.tv_tip);
		mTvTitle2 = (TextView) findViewById(R.id.tv_wallet_password_title2);
		mImgWay = (ImageView) findViewById(R.id.img_wallet_paywithpassword);
		mTvValue = (TextView) findViewById(R.id.tv_wallet_paywithpassword);
		mPwd = (PasswordInputView) findViewById(R.id.pwd_wallet_paywithpassword);
		mPwd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.length() == 6) {
					ChatPwdDialog.this.cancel();
					mWayBack.pwdback(s.toString());
				}

			}
		});

	}

	/**
	 * @param way
	 *            2:银币 1: 聚币
	 * @param values
	 *            值
	 */
	public void setWay(String way, String values) {
		mTvValue.setText(StringUtils.NumFormat(values));
		if (way.equals("2")) {
			mTvTitle2.setText("提现至支付宝");
			mImgWay.setImageResource(R.mipmap.ic_launcher);
		} else if (way.equals("1")) {
			mTvTitle2.setText("提现至微信");
			mImgWay.setImageResource(R.mipmap.ic_launcher);
		} else {
			mTvTitle2.setVisibility(View.GONE);
			mImgWay.setVisibility(View.GONE);
		}
	}
	
	public void setTitle(String titleName){
		tipTV.setText(titleName);
	}

	public void clearPwd() {
		mPwd.setText("");
	}

	public void setWayBack(IChatPwdBack mWayBack) {
		this.mWayBack = mWayBack;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_dialog_cancel:
			this.cancel();
			break;
		default:
			break;
		}

	}

	/**
	 * @author Administrator
	 */
	public interface IChatPwdBack {
		void pwdback(String pwd);
	}

}
