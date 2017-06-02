package com.lvchehui.www.xiangbc.view.popupwindow;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommonAdapter<T> extends BaseAdapter implements Serializable {

	private static final long serialVersionUID = 1L;
	protected LayoutInflater mInflater;
	protected Context mContext;
	protected List<T> mDatas;
	protected final int mItemLayoutId;
	private int MaxCount = -1;

	public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mDatas = mDatas;
		this.mItemLayoutId = itemLayoutId;
	}

	@Override
	public int getCount() {
		if (MaxCount == -1)
			return mDatas.size();
		else {
			if (MaxCount > mDatas.size())
				return mDatas.size();
			else
				return MaxCount;
		}
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setMaxCount(int maxCount) {
		MaxCount = maxCount;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final CommonViewHolder viewHolder = getViewHolder(position, convertView, parent);
		convert(viewHolder, getItem(position));
		return viewHolder.getConvertView();

	}
	
	public void notifyDataSetChanged(List<T> mDatas) {
		this.mDatas = mDatas;
		super.notifyDataSetChanged();
	}

	public abstract void convert(CommonViewHolder helper, T item);

	private CommonViewHolder getViewHolder(int position, View convertView,
			ViewGroup parent) {
		return CommonViewHolder.get(mContext, convertView, parent, mItemLayoutId,
				position);
	}

}
