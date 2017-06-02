package com.lvchehui.www.xiangbc.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.bean.BaseBean;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.dialog.CustomProgressDialog;
import com.lvchehui.www.xiangbc.view.DividerItemDecoration;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.toast.ToastManager;

import java.util.List;

/**
 * RecyclerView分页基类，支持自动加载分页和下拉刷新
 * create by zcn
 * */
public abstract class BaseListFragment extends Fragment implements OnRefreshListener,ConnectionUtil.OnDataLoadEndListener {


	protected RecyclerView mViewList;
	protected TextView mEmptyTips;
	BasePageAdapter mAdapter;
	SwipeRefreshLayout mSwipeRefreshLayout;
	private Request mRequest;
	protected TitleView mTitleView;
	private Dialog mLoadingDialog;

	private CustomProgressDialog mProgress;
	private View toastView;
	private Toast toast;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View root  = inflater.inflate(getLayoutRes(), container, false);
		initViews(root);
		return root;
	}

	protected void initViews(View root)
	{
		initViews(root,true);
	}


	protected void initViews(View root,boolean hasDivider){
		mProgress = CustomProgressDialog.createDialog(getActivity());
		toastView = LayoutInflater.from(getActivity()).inflate(R.layout.toast_view, null);
		toast = Toast.makeText(getActivity(), null, Toast.LENGTH_LONG);
		toast.setView(toastView);
		mTitleView = (TitleView)root.findViewById(R.id.title_view);

		mViewList = (RecyclerView) root.findViewById(R.id.recycler_view);
		mViewList.setLayoutManager(initLayoutManager());
//		  ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) ;
//		  TextView textView = new TextView(getActivity());
//		textView.setLayoutParams(params);
//		textView.setText("测试测试");
//		mViewList.addHeaderView(textView);

		mAdapter = initAdapter();
		mAdapter.init(mViewList, isPageEnabled());
		if (hasDivider) {
			mViewList.setItemAnimator(new DefaultItemAnimator());
			mViewList.addItemDecoration(new DividerItemDecoration(mViewList.getContext(), LinearLayoutManager.VERTICAL));
		}

		mEmptyTips = (TextView) root.findViewById(R.id.empty_propt);
		mEmptyTips.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(ConnectionUtil.getInstance().isNetworkConnected(mViewList.getContext())){
					mSwipeRefreshLayout.setRefreshing(true);
					requestData(0,false);
				} else {
					//Toast.makeText(getContext(), R.string.network_not_connection, Toast.LENGTH_SHORT).show();
				}

			}
		});

		mAdapter.setPagingableListener(new BasePageAdapter.Pagingable() {

			@Override
			public void onLoadMoreItems() {
				// TODO Auto-generated method stub
				if(mAdapter.hasMoreItems()){
					requestData(mAdapter.getItems() == null ? 0 : mAdapter.getItems().size(), false);
				} else {
					mAdapter.onFinishLoading(false);
				}
			}
		});

		//TitleBarMovableTouchListener touchListener = new TitleBarMovableTouchListener(this.getActivity().findViewById(R.id.activity_title));
		//mViewList.setOnTouchListener(touchListener);
		mSwipeRefreshLayout = (SwipeRefreshLayout)root.findViewById(R.id.swipe_refresh_widget);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		mSwipeRefreshLayout.setEnabled(isSwipeRefreshLayoutEnabled());
		mSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_refrsh_color1, R.color.swipe_refrsh_color2, R.color.swipe_refrsh_color3,
				R.color.swipe_refrsh_color4);
		mViewList.setAdapter(mAdapter);
		mHandler.sendEmptyMessageDelayed(MSG_REQUEST_DATA, FIRST_INIT_DELAY);

	}

	public void showEmpty(){
		mViewList.setVisibility(View.GONE);
		mEmptyTips.setVisibility(View.VISIBLE);
	}

	public void hideEmpty(){
		mViewList.setVisibility(View.VISIBLE);
		mEmptyTips.setVisibility(View.GONE);
	}

	protected void requestData(int start, boolean showloading) {
		if (mRequest != null) {
			mRequest.cancel();
		}
		mRequest = initRequest(start);
		hideEmpty();
		if (showloading) {
			showLoading();
		}

	}

	/**
	 * 解析Json，得到List,在子线程中运行
	 * */
	protected abstract List<BaseBean> convertToBeanList(String json);

	protected RecyclerView.LayoutManager initLayoutManager() {
		LinearLayoutManager layoutManager = new LinearLayoutManager(
				mViewList.getContext());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		return layoutManager;
	}

	/**
	 * 初始化Adapter
	 * */
	protected abstract BasePageAdapter initAdapter();

	/**
	 * 返回是否需要下拉刷新
	 * */
	protected abstract boolean isSwipeRefreshLayoutEnabled();

	/**
	 * 返回每一页的数量
	 * */
	protected abstract int getSizeInPage();

	/**
	 * 获取Request并执行网络请求
	 * */
	protected abstract Request initRequest(int start);

	protected abstract boolean isPageEnabled();

	protected abstract boolean isDataGot();

	protected int getLayoutRes(){
		return R.layout.list_normal;
	}




	private static final int MSG_REQUEST_DATA = 1;
	private static final long FIRST_INIT_DELAY = 50;
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case MSG_REQUEST_DATA :{
					mSwipeRefreshLayout.setRefreshing(true);
					requestData(mAdapter.getItems() == null ? 0:mAdapter.getItems().size(),false);
					break;
				}
			}
		}
	};
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mRequest != null && !mRequest.isCanceled()) {
			mRequest.cancel();

		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if(ConnectionUtil.getInstance().isNetworkConnected(mViewList.getContext())){
			reloadData();
		} else {
			//ToastManager.getManager().show(R.string.network_not_connection);
			mSwipeRefreshLayout.setRefreshing(false);
		}
	}

	public void reloadData(){
		if(mAdapter != null){
			mAdapter.clearData();
		}
		requestData(0, false);
	}


	private class GetBeanListTask extends AsyncTask{

		@Override
		protected Object doInBackground(Object... params) {
			XgoLog.e("doInbackground:" + params[0].toString());
			// TODO Auto-generated method stub
			return convertToBeanList(params[0].toString());
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			onBeanListGot(result == null ? null : (List<BaseBean>)result);
		}
	}

	@Override
	public void OnLoadEnd(String ret) {
		XgoLog.i("onLoadEnd:" + ret);
		new GetBeanListTask().execute(ret == null ? "" : ret);		
	}

	private void onBeanListGot(List<BaseBean> listResult){
		mRequest = null;
		dismissLoading();
		mSwipeRefreshLayout.setRefreshing(false);
		hideEmpty();
		if(listResult == null){
			mAdapter.onFinishLoading(true);
			checkShowEmpty(mAdapter);
			return;
		}
		
		
		List<BaseBean> items = mAdapter.getItems();
		if(items == null){
			items = listResult;
		} else {
			items.addAll(listResult);
		}
		mAdapter.setItems(items);
		if(listResult.size() < getSizeInPage()){
			mAdapter.onFinishLoading(false);
		} else {
			mAdapter.onFinishLoading(true);
		}
		checkShowEmpty(mAdapter);
		mAdapter.notifyDataSetChanged();
	}

	private void checkShowEmpty(BasePageAdapter adapter){
		final List items = adapter.getItems();
		if((items == null || items.size() == 0) && !isDataGot()){
			showEmpty();
		}
	}
	
	protected void showLoading(){
		if(mLoadingDialog == null){
			mLoadingDialog = new ProgressDialog(mViewList.getContext());
			mLoadingDialog.setCancelable(false);
		}
		mLoadingDialog.show();
	}

	protected void dismissLoading(){
		if(mLoadingDialog != null && mLoadingDialog.isShowing()){
			mLoadingDialog.dismiss();
		}
	}
	
	
	public void showProgressDailog() {
		showProgressDailog("请稍后");
	}

	public void showProgressDailog(String strMessage) {
		showProgressDialog(strMessage, false);
	}

	public void showProgressDialog(String strMessage, boolean isCancelAble) {

		if (null == mProgress) {
			return;
		}

		if (isCancelAble) {
			mProgress.setCancelable(true);
			mProgress.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					mProgress.dismiss();
				}
			});
		}

		mProgress.setMessage(strMessage);
		mProgress.show();
	}

	public void dismissProgressDialog() {
		if (null != mProgress && mProgress.isShowing()) {
			mProgress.dismiss();
		}
	}

	public void showToast(String hint) {
		showToast(hint, Toast.LENGTH_LONG);
	}

	public void showToast(String hint, int duration) {
		if (null == toast || null == toastView)
			return;

		TextView tv = (TextView) toastView.findViewById(R.id.toastMessage);
		tv.setText(hint);
		toast.setDuration(duration);
		toast.show();
	}
}
