package com.lvchehui.www.xiangbc.Fragment;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.mine.MemberActivity;
import com.lvchehui.www.xiangbc.base.BaseListFragment;
import com.lvchehui.www.xiangbc.base.BasePageAdapter;
import com.lvchehui.www.xiangbc.bean.MemberBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class MemberMsgFragment extends BaseListFragment {

	@Override
	protected List convertToBeanList(String json) {
		ArrayList<MemberBean> arrayList = new ArrayList<>();

		for (int i = 0; i <= 30; i++) {
			MemberBean textBean = new MemberBean();
			textBean.name = "张灿能";
			textBean.phone = "15859254561";
		
			arrayList.add(textBean);
		}
		return arrayList;
	}

	@Override
	protected BasePageAdapter initAdapter() {
		return new MemberAdapter();
	}

	@Override
	protected boolean isSwipeRefreshLayoutEnabled() {
		return false;
	}

	@Override
	protected int getSizeInPage() {
		return 12;
	}

	@Override
	protected Request initRequest(int start) {
		// TODO Auto-generated method stub


		return ConnectionManager.getInstance().findSms(getContext(),"15859254561",this);
	}

	@Override
	protected boolean isPageEnabled() {
		return false;
	}

	@Override
	protected boolean isDataGot() {
		return false;
	}

	class MemberAdapter extends BasePageAdapter {

		class MemberItemViewHolder extends RecyclerView.ViewHolder {

			@ViewInject(R.id.root)
			private View m_root;
			@ViewInject(R.id.et_campany_name)
			private TextView m_tv_name;
			@ViewInject(R.id.tv_other)
			private TextView m_tv_other;
			public MemberItemViewHolder(View itemView) {
				super(itemView);
				x.view().inject(this,itemView);
			}

		}

		@Override
		protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
			View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_member, viewGroup, false);
			return new MemberItemViewHolder(inflate);
		}

		@Override
		public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
			if (viewHoder instanceof MemberItemViewHolder) {
				MemberItemViewHolder holder = (MemberItemViewHolder) viewHoder;
				MemberBean bean = (MemberBean) mItems.get(position);
				holder.m_root.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(getActivity(),MemberActivity.class));
					}
				});
				holder.m_tv_name.setText(bean.name);
			}
		}
	}

}
