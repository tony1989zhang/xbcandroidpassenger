package com.lvchehui.www.xiangbc.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.AddInvoiceActivity;
import com.lvchehui.www.xiangbc.activity.InvoiceActivity;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseListFragment;
import com.lvchehui.www.xiangbc.base.BasePageAdapter;
import com.lvchehui.www.xiangbc.bean.BaseBean;
import com.lvchehui.www.xiangbc.bean.InvoiceListBean;
import com.lvchehui.www.xiangbc.bean.InvoiceListDataBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.view.HalfListView;
import com.lvchehui.www.xiangbc.view.dialog.ListDialog;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

public class InvoiceFragment extends BaseListFragment {
	  public static final String INVOICE_NAME = "invoice_name";
	  public static final String INVOICE_GID = "invoice_gid";
	  public static final String INVOICE_GET_BUNDLE = "invoice_get_bundle";
	  public static final String INVOICEINFO_BUNDLE = "invoiceInfo_bundle";

	// 请求数据完进行解析
		@Override
		protected List convertToBeanList(String json) {
			// TODO Auto-generated method stub
			InvoiceListBean invoiceListBean = App.getInstance().getBeanFromJson(json, InvoiceListBean.class);
			if (invoiceListBean.errCode != -1){
				return invoiceListBean.resData;
			}
			return null;
		}

		@Override
		protected BasePageAdapter initAdapter() {
			// TODO Auto-generated method stub
			return new InvoiceAdapter();
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
			// TODO Auto-generated method stub

			return ConnectionManager.getInstance().invoiceList(getActivity(),(String) SPUtil.getInstance(getActivity()).get(Constants.USER_GID,""),"express_info_gid",this);
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
	class InvoiceAdapter extends BasePageAdapter  {
        class InVoiceItemViewHolder extends ViewHolder{
			@ViewInject(R.id.root)
			private View m_root;
			@ViewInject(R.id.tv_tickethead_content)
			private TextView ticktheadContentTV;
			@ViewInject(R.id.tv_address_content)
			private TextView tvAddressContent;
			public InVoiceItemViewHolder(View itemView) {
				super(itemView);
				x.view().inject(this,itemView);
			}
        }
		

		@Override
		protected ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
//			final View view = View.inflate(viewGroup.getContext(), R.layout.item_invoice, null);
			final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_invoice,viewGroup,false);
			return new InVoiceItemViewHolder(view);
		}

		@Override
		public void doBindViewHolder(ViewHolder viewHoder, int position) {
			if(viewHoder  instanceof InVoiceItemViewHolder){
				InVoiceItemViewHolder invoiceItemViewHolder = (InVoiceItemViewHolder) viewHoder;
				final InvoiceListDataBean bean = (InvoiceListDataBean) mItems.get(position);
				invoiceItemViewHolder.ticktheadContentTV.setText("" + bean.invoice_name);
				invoiceItemViewHolder.tvAddressContent.setText("" + bean.address);
				invoiceItemViewHolder.m_root.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
//						startActivityForResult(new Intent(getActivity()));
						FragmentActivity activity = getActivity();
						if (activity instanceof InvoiceActivity) {
							showToast("发票信息");
							InvoiceActivity inoviceActivity = (InvoiceActivity) getActivity();
							inoviceActivity.setInvoiceName(bean.invoice_name);
							inoviceActivity.setInvoiceInvoiceListDataBean(bean);
							inoviceActivity.finish();
						}
					}
				});
				invoiceItemViewHolder.m_root.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						showDelAndEdit((String) SPUtil.getInstance(getContext()).get(Constants.USER_GID, ""), bean);
						return true;
					}
				});
			}
		}
//		
	}
	public void showDelAndEdit(final String gid, final InvoiceListDataBean bean){
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
					Intent intent = new Intent(getActivity(), AddInvoiceActivity.class);
					intent.putExtra(Constants.SOURCE, InvoiceFragment.class.getSimpleName());
					Bundle bundle = new Bundle();
					bundle.putSerializable(INVOICEINFO_BUNDLE, bean);
					intent.putExtras(bundle);
					startActivityForResult(intent, 0);

				}else if(position == 1){
					showProgressDailog("正在删除发票");
					ConnectionManager.getInstance().invoiceDel(getContext(), gid, bean.invoice_gid, new InvoiceAndExpressDelOnDataLoadEndListener(InvoiceAndExpressDelOnDataLoadEndListener.INVOICEDEL_TYPE));
					ConnectionManager.getInstance().expressInfoDel(getContext(),gid,bean.express_info_gid,new InvoiceAndExpressDelOnDataLoadEndListener(InvoiceAndExpressDelOnDataLoadEndListener.EXPRESSDEL_TYPE));
				}
				listDialog.dismiss();
			}
		});
	}

	class InvoiceAndExpressDelOnDataLoadEndListener implements ConnectionUtil.OnDataLoadEndListener {
		public int del_type;
		public static final int INVOICEDEL_TYPE = 2001;
		public static final int EXPRESSDEL_TYPE = 2002;

		public InvoiceAndExpressDelOnDataLoadEndListener(int index){
             this.del_type = index;
		}

		@Override
		public void OnLoadEnd(String ret) {
			switch (del_type){
				case INVOICEDEL_TYPE:
					BaseBean beanFromJson = App.getInstance().getBeanFromJson(ret, BaseBean.class);
					showToast(beanFromJson.resMsg);
					if (beanFromJson.errCode != -1) {
						onRefresh();
						dismissProgressDialog();
					}
					break;
				case EXPRESSDEL_TYPE:
					BaseBean baseFromJsonExpress = App.getInstance().getBeanFromJson(ret, BaseBean.class);
					showToast(baseFromJsonExpress.resMsg);
					if (baseFromJsonExpress.errCode != -1) {
					}
					break;
			}

		}
	}
}
