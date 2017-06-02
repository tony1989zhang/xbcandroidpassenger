package com.lvchehui.www.xiangbc.Fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.freeride.RideDetailsActivity;
import com.lvchehui.www.xiangbc.base.BaseListFragment;
import com.lvchehui.www.xiangbc.base.BasePageAdapter;
import com.lvchehui.www.xiangbc.bean.RideBean;
import com.lvchehui.www.xiangbc.bean.RideDataBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张灿能 on 2016/6/16.
 * 作用：顺风车报价列表
 */
public class QuotedCarListFragment extends BaseListFragment {
    private boolean mIsDataGot = false;
    private int sortQuotedCarType = 0;
    public static final int PRICE_SORT_TYPE = 1;
    public static final int VOLUME_SORT_TYPE = 2;
    public static final int EVA_SORT_TYPE = 3;

    @Override
    protected List convertToBeanList(String json) {
        RideBean rideBean = new RideBean();
        //判断是否获取到数据
        if (rideBean.errCode != -1){
            mIsDataGot = true;
        }
        rideBean.resData = new ArrayList<>();
        for (int i = 0;i < 52;i++){
            RideDataBean rideDataBean = new RideDataBean();
            rideDataBean.name = "张灿能";
            rideDataBean.pic = "http://p1.so.qhimg.com/t018d14314235555831.jpg";
            rideDataBean.price = "999元";
            rideBean.resData.add(rideDataBean);
        }

        return rideBean.resData;
    }

    @Override
    protected BasePageAdapter initAdapter() {
        return new QuotedCarListAdapter();
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
        return ConnectionManager.getInstance().findSms(getActivity(),"",this);//ConnectionManager.getInstance().sendSms(this,"",this)
    }

    public void setSortQuotedCar(int type){
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


    class QuotedCarListAdapter extends BasePageAdapter {

        class QuotedCarItemViewHolder extends RecyclerView.ViewHolder {
            View root;
            ImageView mPicIV;
            TextView mTvName;
            TextView mTvPrint;
            public QuotedCarItemViewHolder(View itemView) {
                super(itemView);
                root = itemView.findViewById(R.id.root);
                mPicIV = (ImageView) itemView.findViewById(R.id.imageView_pic);
                mTvName = (TextView) itemView.findViewById(R.id.et_campany_name);
                mTvPrint = (android.widget.TextView) itemView.findViewById(R.id.tv_print);

            }
        }
        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            View inflate = View.inflate(viewGroup.getContext(), R.layout.item_ride, null);
            return new QuotedCarItemViewHolder(inflate);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {

            if (viewHoder instanceof QuotedCarItemViewHolder){
                QuotedCarItemViewHolder rideItemViewHolder = (QuotedCarItemViewHolder) viewHoder;
                RideDataBean rideDataBean = (RideDataBean) mItems.get(position);
                ConnectionUtil.getInstance().loadImage(rideItemViewHolder.mPicIV, rideDataBean.pic);
                rideItemViewHolder.mTvName.setText(rideDataBean.name);
                rideItemViewHolder.mTvPrint.setText(rideDataBean.price);
                rideItemViewHolder.root.setOnClickListener(new QuotedCarItemOnclickListener(position));
            }
        }

        class QuotedCarItemOnclickListener implements View.OnClickListener {
            int position;

            public QuotedCarItemOnclickListener(int position){
                this.position = position;
            }
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),RideDetailsActivity.class));
            }
        }
    }
}
