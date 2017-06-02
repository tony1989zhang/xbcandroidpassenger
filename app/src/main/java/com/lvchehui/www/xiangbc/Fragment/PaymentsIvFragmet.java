package com.lvchehui.www.xiangbc.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 作用：总收入
 */
@ContentView(R.layout.layout_payments_iv)
public class PaymentsIvFragmet extends BaseFragment {

	@ViewInject(R.id.tv_tx)
	TextView txTV;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Event(R.id.tv_tx)
	private void txOnClick(View v) {


	}
}
