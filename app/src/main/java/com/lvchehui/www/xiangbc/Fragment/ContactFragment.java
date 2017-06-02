package com.lvchehui.www.xiangbc.Fragment;

import java.util.List;

import com.android.volley.Request;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.mine.AddContactActivity;
import com.lvchehui.www.xiangbc.activity.ContactActivity;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseListFragment;
import com.lvchehui.www.xiangbc.base.BasePageAdapter;
import com.lvchehui.www.xiangbc.bean.BaseBean;
import com.lvchehui.www.xiangbc.bean.ContactsListBean;
import com.lvchehui.www.xiangbc.bean.ContactsListDataBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.HalfListView;
import com.lvchehui.www.xiangbc.view.dialog.ListDialog;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactFragment extends BaseListFragment {

	public static final String CONTACT_NAME = "CONTACT_NAME";
	public static final String CONTACT_PHONE = "CONTACT_PHONE";
	public static final String CONTACTS_GID = "contacts_gid";

	// 请求数据完进行解析
		@Override
		protected List convertToBeanList(String json) {
			// TODO Auto-generated method stub
			ContactsListBean beanFromJson = App.getInstance().getBeanFromJson(json, ContactsListBean.class);
			if (beanFromJson.errCode != -1){
//				showToast("" + beanFromJson.resData.list);
				return beanFromJson.resData;
			}
			return null;
		}

		@Override
		protected BasePageAdapter initAdapter() {
			// TODO Auto-generated method stub
			return new ContactListAdapter();
		}

		@Override
		protected boolean isSwipeRefreshLayoutEnabled() {
			// TODO Auto-generated method stub
			return true;
		}

		// 加载页面数
		@Override
		protected int getSizeInPage() {
			// TODO Auto-generated method stub
			return 12;
		}

		// 访问防落请求
		@Override
		protected Request initRequest(int start) {
			return ConnectionManager.getInstance().contactsList(getActivity(), (String)SPUtil.getInstance(getActivity()).get(Constants.USER_GID,""),this);
		}

		@Override
		protected boolean isPageEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		// 是否请求道数据
		@Override
		protected boolean isDataGot() {
			// TODO Auto-generated method stub
			return false;
		}
	
	class ContactListAdapter extends BasePageAdapter {

		class ContactItemViewHolder extends RecyclerView.ViewHolder {
			public TextView mName;
			public View mRoot;
			private TextView mPhone;

			public ContactItemViewHolder(View itemView) {
				super(itemView);
				mRoot = itemView.findViewById(R.id.root);
				mName = (TextView) itemView.findViewById(R.id.tv_name_content);
				mPhone = (TextView) itemView.findViewById(R.id.tv_phone_content);
			}

		}


		@Override
		protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
			//final View view = View.inflate(viewGroup.getContext(), R.layout.item_contact, null);
			final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_contact,viewGroup,false);
			return new ContactItemViewHolder(view);
		}

		@Override
		public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
			if (viewHoder instanceof ContactItemViewHolder) {
				ContactItemViewHolder contactItemViewHolder = (ContactItemViewHolder) viewHoder;
				final ContactsListDataBean bean = (ContactsListDataBean) mItems.get(position);
				contactItemViewHolder.mName.setText("" + bean.name);
				contactItemViewHolder.mPhone.setText(""  + bean.phone);
                contactItemViewHolder.mRoot.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getActivity() instanceof ContactActivity){
                            ContactActivity contactActivity = (ContactActivity) getActivity();
                            contactActivity.setContactName("" + bean.name);
                            contactActivity.setContactPhone("" + bean.phone);
                            contactActivity.finish();
                        }
                    }
                });
				contactItemViewHolder.mRoot.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						showDelAndEdit((String) SPUtil.getInstance(getContext()).get(Constants.USER_GID, ""), bean.contacts_gid,"" + bean.name,""  + bean.phone);
						return true;
					}
				});
			}
		}


	}

	public void showDelAndEdit(final String gid, final String contacts_gid,final String name,final String phone){
		final String[] params = { "编辑", "删除"};


		HalfListView halfListView = new HalfListView(getContext());
		ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_choose, params);
		halfListView.setAdapter(adapter);
		halfListView.setCacheColorHint(0x000000);

		final ListDialog listDialog = new ListDialog(getContext(),halfListView);
		listDialog.show();
		halfListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if (position == 0){
					Intent intent = new Intent(getActivity(), AddContactActivity.class);
					intent.putExtra(Constants.SOURCE,ContactFragment.class.getSimpleName());
					intent.putExtra(CONTACT_NAME,name);
					intent.putExtra(CONTACT_PHONE,phone);
					intent.putExtra(CONTACTS_GID,contacts_gid);
					startActivityForResult(intent,0);

				}else if(position == 1){
					ConnectionManager.getInstance().delConstacts(getContext(), gid, contacts_gid, new ConnectionUtil.OnDataLoadEndListener() {
						@Override
						public void OnLoadEnd(String ret) {
							BaseBean beanFromJson = App.getInstance().getBeanFromJson(ret, BaseBean.class);
							showToast(beanFromJson.resMsg);
							if (beanFromJson.errCode != -1){
								onRefresh();
							}
						}
					});
				}
				listDialog.dismiss();
			}
		});
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		XgoLog.e("onActivity_resutl" + resultCode);
		if (resultCode == Activity.RESULT_OK){
			onRefresh();
		}
	}
}
