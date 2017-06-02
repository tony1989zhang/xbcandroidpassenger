package com.lvchehui.www.xiangbc.view.button;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;

import java.util.Timer;
import java.util.TimerTask;

/**
  * @ClassName: CaptchaButton
  * @Description: 获取验证码button 
  * @author 4evercai
  * @date 2014年12月24日 上午8:48:26
  *
  */
public class ButtonForCaptcha extends TextView {
	private Timer countdownTimer;
	private int countdownSecond;
	private int COUNTDOWN_TIME = 60; //倒计时默认时间

	public ButtonForCaptcha(Context context) {
		super(context);
		initView();
	}

	public ButtonForCaptcha(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public ButtonForCaptcha(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		setText(getContext().getResources().getString(R.string.fetch_captcha));
		this.setTextColor(this.getResources().getColor(R.color.white));
		this.setSingleLine();
		this.setEllipsize(TextUtils.TruncateAt.MARQUEE);
		this.setMarqueeRepeatLimit(-1);
		this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
	}

	private Handler countdownHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				cancelCountdown();
				setText(ButtonForCaptcha.this.getContext().getResources()
						.getString(R.string.fetch_captcha));
				ButtonForCaptcha.this.setEnabled(true);
			} else {
				// ButtonForCaptcha.this.setText("获取验证码(" + msg.what + ")");
				ButtonForCaptcha.this.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bottom_bg_pressed));
				ButtonForCaptcha.this.setText(msg.what + "s");
			}
			super.handleMessage(msg);
		}
	};

	/**
	  * @Title: startCountdown
	  * @Description: 开始倒计时
	  * @throws
	  */
	public void startCountdown() {
		if (countdownTimer == null) {
			countdownTimer = new Timer();
			countdownSecond = COUNTDOWN_TIME;
			this.setEnabled(false);
			countdownTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					Message msg = new Message();
					msg.what = countdownSecond--;
					countdownHandler.sendMessage(msg);
				}

			}, 0, 1000);
		}
	}

	/**
	  * @Title: cancelCountdown
	  * @Description: 取消倒计时
	  */
	public void cancelCountdown() {
		if (countdownTimer != null) {
			countdownTimer.cancel();
			countdownTimer = null;
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		cancelCountdown();
	}
}
