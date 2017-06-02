package com.lvchehui.www.xiangbc.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.OneKeyShare.ShareUtils;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseFragment;
import com.lvchehui.www.xiangbc.bean.RedpacketShareBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.SPUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 作用：我的伙伴
 */

@ContentView(R.layout.layout_member_iv)
public class MemberIvFragment extends BaseFragment implements ConnectionUtil.OnDataLoadEndListener {
	@ViewInject(R.id.tv_total_people)
	private TextView m_tv_total_people;

	@ViewInject(R.id.tv_recruiting)
	private TextView m_tv_recruiting;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	}

	@Event(value=R.id.tv_recruiting)
	private void memberIvOnClick(View v){
		showProgressDailog("生成招募码");
		ConnectionManager.getInstance().agentExtendsGetcode(getContext(),(String) SPUtil.getInstance(getContext()).get(Constants.USER_GID,""),this);
	}

	@Override
	public void OnLoadEnd(String ret) {
		dismissProgressDialog();
		RedpacketShareBean redpacketShareBean = App.getInstance().getBeanFromJson(ret, RedpacketShareBean.class);
		showToast(redpacketShareBean.resMsg);
		if (redpacketShareBean.errCode != -1){
			ShareUtils.getInstance().showShare(getContext(),redpacketShareBean.resData.redPacketCode);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ShareUtils.getInstance().stopShow();
	}
}
