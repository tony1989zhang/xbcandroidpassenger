package com.lvchehui.www.xiangbc.activity.itinerary;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseListFragment;
import com.lvchehui.www.xiangbc.base.BasePageAdapter;
import com.lvchehui.www.xiangbc.bean.GetQuotationedListBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.XgoLog;

import org.simple.eventbus.EventBus;

import java.sql.Date;
import java.util.List;

/**
 * 作用：我的行程--已报价车队列表
 */
public class MyTripQuotedCarListFragment extends BaseListFragment {
    private boolean mIsDataGot = false;
    private int sortQuotedCarType = 0;
    public static final int PRICE_SORT_TYPE = 1;
    public static final int VOLUME_SORT_TYPE = 2;
    public static final int EVA_SORT_TYPE = 3;
    String mDemandGid = "";

    @Override
    protected void initViews(View root, boolean hasDivider) {
        hasDivider = false;
        super.initViews(root, hasDivider);
    }

    public void setmDemandGid(String demandGid)
    {
        this.mDemandGid = demandGid;
        onRefresh();
    }
    @Override
    protected List convertToBeanList(String json) {
        GetQuotationedListBean listBean = App.getInstance().getBeanFromJson(json, GetQuotationedListBean.class);
        if (listBean.errCode != -1)
        {
            XgoLog.e("listBean:" + listBean.toString());
            return listBean.resData;
        }
        return null;
    }

    @Override
    protected BasePageAdapter initAdapter() {
        return new MyTripQuotedCarListAdapter();
    }

    @Override
    protected boolean isSwipeRefreshLayoutEnabled() {
        return true;
    }

    @Override
    protected int getSizeInPage() {
        return 10;
    }

    @Override
    protected Request initRequest(int start) {
        showToast("sortQuotedCarType" + sortQuotedCarType);
        XgoLog.e("mDemandGid:" + mDemandGid);
        return ConnectionManager.getInstance().getQuotationedList(getContext(),mDemandGid,this);
    }

    public void setSortQuotedCar(int type) {
        sortQuotedCarType = type;
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return mIsDataGot;
    }


    class MyTripQuotedCarListAdapter extends BasePageAdapter {

        class MyTripQuotedCarItemViewHolder extends RecyclerView.ViewHolder {
            View root;
            ImageView mPicIV;
            TextView mTvName;
            TextView mTvPrice;
            TextView mTvItemLossPercent;
            TextView mTvVolume;
            TextView mTvEndBookingTime;

            public MyTripQuotedCarItemViewHolder(View itemView) {
                super(itemView);
                root = itemView.findViewById(R.id.item_quoted_fleet);
                mPicIV = (ImageView) itemView.findViewById(R.id.iv_item_fleet_pic);
                mTvName = (TextView) itemView.findViewById(R.id.tv_item_fleet_name);
                mTvPrice = (TextView) itemView.findViewById(R.id.tv_item_quote_price);
                mTvItemLossPercent = (TextView) itemView.findViewById(R.id.tv_item_loss_percent);
                mTvVolume = (TextView) itemView.findViewById(R.id.tv_volume);
                mTvEndBookingTime = (TextView) itemView.findViewById(R.id.tv_end_booking_time);
            }
        }

        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            View inflate = View.inflate(viewGroup.getContext(), R.layout.item_quoted_fleet, null);
            return new MyTripQuotedCarItemViewHolder(inflate);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {

            if (viewHoder instanceof MyTripQuotedCarItemViewHolder) {
                MyTripQuotedCarItemViewHolder rideItemViewHolder = (MyTripQuotedCarItemViewHolder) viewHoder;
                GetQuotationedListBean.ResDataBean rideDataBean = ( GetQuotationedListBean.ResDataBean) mItems.get(position);
                ConnectionUtil.getInstance().loadImage(rideItemViewHolder.mPicIV, rideDataBean.cars_info.get(0).car_photos_url.out.get(0));
                rideItemViewHolder.mTvName.setText(rideDataBean.motorcade_info.get(0).name);
                rideItemViewHolder.mTvPrice.setText("¥" + rideDataBean.sum_money);
                rideItemViewHolder.mTvItemLossPercent.setText(String.format(getString(R.string.loss_percent), rideDataBean.loss_percent));
                rideItemViewHolder.mTvVolume.setText(String.format(getString(R.string.volume),rideDataBean.motorcade_info.get(0).volume));
                rideItemViewHolder.root.setOnClickListener(new MyTripQuotedCarItemOnclickListener(rideDataBean));
            }
        }

        class MyTripQuotedCarItemOnclickListener implements View.OnClickListener {
            GetQuotationedListBean.ResDataBean dataBean;
            public MyTripQuotedCarItemOnclickListener(GetQuotationedListBean.ResDataBean dataBean) {
                this.dataBean = dataBean;
            }

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), QuotedCarDetailsActivity.class));
                EventBus.getDefault().postSticky(dataBean);
            }
        }
    }
}
