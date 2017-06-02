package com.lvchehui.www.xiangbc.wxapi;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.home.HomeActivity;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.pay_result)
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler{
	@ViewInject(R.id.title_view)
	private TitleView m_title_view;

	@ViewInject(R.id.iv_gif)
	private ImageView m_iv_gif;

	@ViewInject(R.id.tv_ok)
	private TextView m_tv_ok;


	@ViewInject(R.id.tv_finish_said)
	private TextView m_tv_finish_said;
	/**
	 * 支付成功回调页面
	 * */
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleView(m_title_view,"支付结果");

		String paySource = getIntent().getStringExtra(Constants.PAYSOURCE.PAY_SOURCE);
		if (null != paySource &&paySource.equals(Constants.PAYSOURCE.PAY_FROM_ALIPAY)){

		}else {
			api = WXAPIFactory.createWXAPI(this, Constants.WX_APPID);
			api.handleIntent(getIntent(), this);
		}
		m_tv_finish_said.setText(getString(R.string.send_needs_wait_offers));
	}

	@Event(value = R.id.tv_ok)
	private void finishPayOnClick(View v){
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra(Constants.SOURCE,WXPayEntryActivity.class.getSimpleName());
		startActivity(intent);
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		XgoLog.e("onPayFinish, errCode = \" + resp.errCode");

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.app_tip);
			builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
			builder.show();
		}
	}
}