package com.lvchehui.www.xiangbc.Fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.util.Util;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.AMapYunTuActivity;
import com.lvchehui.www.xiangbc.activity.ConfirmOrderActivity;
import com.lvchehui.www.xiangbc.activity.freeride.RideDetailsActivity;
import com.lvchehui.www.xiangbc.activity.mine.WebActivity;
import com.lvchehui.www.xiangbc.activity.pay.PayForTripActivity;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseListFragment;
import com.lvchehui.www.xiangbc.base.BasePageAdapter;
import com.lvchehui.www.xiangbc.bean.DemandGetListBean;
import com.lvchehui.www.xiangbc.bean.DemandSubmitDataBean;
import com.lvchehui.www.xiangbc.bean.MemberBean;
import com.lvchehui.www.xiangbc.bean.QuotationGetInfo;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.DateUtil;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.utils.Utils;
import com.lvchehui.www.xiangbc.view.dialog.ChooseWayDialog;
import com.lvchehui.www.xiangbc.view.dialog.CustomDialog;

import org.simple.eventbus.EventBus;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张灿能 on 2016/6/14.
 * 作用：已出发
 */
public class SetOffListFragment extends BaseListFragment {
    private QuotationGetInfo.ResDataBean resData;
    @Override
    protected List convertToBeanList(String json) {

        DemandGetListBean demandGetListBean = App.getInstance().getBeanFromJson(json, DemandGetListBean.class);
        if (demandGetListBean.errCode != -1) {
            return demandGetListBean.resData;
        }
        return null;
    }

    @Override
    protected BasePageAdapter initAdapter() {
        return new SetOffListAdapter();
    }

    @Override
    protected boolean isSwipeRefreshLayoutEnabled() {
        return true;
    }

    @Override
    protected int getSizeInPage() {
        return 0;
    }

    @Override
    protected Request initRequest(int start) {
        return ConnectionManager.getInstance().demandGetList(getContext(),
                (String) SPUtil.getInstance(getContext()).get(Constants.USER_GID, ""), 0, 0, "" + 3, this);
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return false;
    }

    class SetOffListAdapter extends BasePageAdapter {
        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            final View view = View.inflate(viewGroup.getContext(), R.layout.item_setoff, null);
            return new SetOffItemViewHolder(view);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
            if (viewHoder instanceof SetOffItemViewHolder) {
                SetOffItemViewHolder holder = (SetOffItemViewHolder) viewHoder;

                DemandSubmitDataBean dataBean = (DemandSubmitDataBean) mItems.get(position);
                boolean hasTimePicker = (dataBean.use_type <= 2);
                holder.m_tv_dingd_content.setText("订单号:" + dataBean.request_order);
                holder.m_tv_data_time.setText("" + DateUtil.getStrTime(dataBean.begin_time, hasTimePicker));
                holder.m_tv_address_content.setText(dataBean.begin_address);
                ConnectionManager.getInstance().quotationGetinfo(getContext(), dataBean.quotation_gid, new QuotationGetInfoOnDataLoadListener(holder));

                int use_type = dataBean.use_type;
                if (use_type == 1){
                    holder.m_tv_ltinerary_type.setText("单程用车");
                }else if(use_type == 2){
                    holder.m_tv_ltinerary_type.setText("往还用车");
                }else if(use_type == 3){
                    holder.m_tv_ltinerary_type.setText("单日用车");
                }else if(use_type == 4){
                    holder.m_tv_ltinerary_type.setText("多日用车");
                }else{
                    holder.m_tv_ltinerary_type.setText("顺风车");
                }

                holder.m_tv_driver.setOnClickListener(new ScheduledListItemOnClick());
                holder.m_tv_quote_quantity.setOnClickListener(new ScheduledListItemOnClick());
                holder.m_tv_service.setOnClickListener(new ScheduledListItemOnClick());
                holder.m_ll_ltinerary_content.setOnClickListener(new ScheduledListItemOnClick(dataBean));
                holder.m_tv_map.setOnClickListener(new ScheduledListItemOnClick());
                holder.m_tv_payment.setOnClickListener(new ScheduledListItemOnClick(dataBean));
            }
        }

        class QuotationGetInfoOnDataLoadListener implements ConnectionUtil.OnDataLoadEndListener {

            private SetOffItemViewHolder holder;

            public QuotationGetInfoOnDataLoadListener(SetOffItemViewHolder holder) {
                this.holder = holder;
            }

            @Override
            public void OnLoadEnd(String ret) {
                QuotationGetInfo quotationGetInfo = App.getInstance().getBeanFromJson(ret, QuotationGetInfo.class);
                if (quotationGetInfo.errCode != -1)
                {
                    resData = quotationGetInfo.resData;
                    double restMoney = quotationGetInfo.resData.motorcade_quotation_money - quotationGetInfo.resData.deposit;
                    holder.m_tv_total_money.setText("未付：" + restMoney + "元");
                }

            }
        }

        class SetOffItemViewHolder extends RecyclerView.ViewHolder {
            /*   private TextView m_tv_ltinerary_title;
               private LinearLayout m_ll_ltinerary_content;
               private TextView m_tv_data_time; //02月23日 14:23;
               private TextView m_tv_address_content; //厦门北站商务营运中心3678号5楼享莫某上的方式发送到发送到水电费水电费水电费水电费;
               private TextView m_tv_dingd_content; //订单号：XM201606020001;
               private TextView m_tv_ltinerary_type; //单程用车;
               private TextView m_tv_service;
               private TextView m_tv_driver; //司机信息;
               private TextView m_tv_map;//地图;
               private TextView m_tv_payment; //付款;
              */
            @ViewInject(R.id.tv_total_money)
            private TextView m_tv_total_money;
            @ViewInject(R.id.tv_quote_quantity)
            private TextView m_tv_quote_quantity; //龙翔客运;

            @ViewInject(R.id.tv_driver)
            private TextView m_tv_driver; //司机;

            @ViewInject(R.id.tv_service)
            private TextView m_tv_service; //客服;

            @ViewInject(R.id.ll_ltinerary_content)
            private LinearLayout m_ll_ltinerary_content;

            @ViewInject(R.id.tv_dingd_content)
            private TextView m_tv_dingd_content; //订单号：XM201603030001;

            @ViewInject(R.id.tv_data_time)
            private TextView m_tv_data_time; //02月23日 14:23;

            @ViewInject(R.id.tv_ltinerary_type)
            private TextView m_tv_ltinerary_type; //单程用车;

            @ViewInject(R.id.tv_address_content)
            private TextView m_tv_address_content; //福建省集美区商务运营中心3678号5楼详细某某某座位某某某旁边;

            @ViewInject(R.id.tv_map)
            private TextView m_tv_map; //地图;

            @ViewInject(R.id.tv_payment)
            private TextView m_tv_payment; //付款;


            public SetOffItemViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this,itemView);
            }
        }
    }

    class ScheduledListItemOnClick implements View.OnClickListener {

        DemandSubmitDataBean demandSubmit;
        public ScheduledListItemOnClick(){

        }
        public ScheduledListItemOnClick(DemandSubmitDataBean demandSubmit){
            this.demandSubmit = demandSubmit;
        }
        private CustomDialog customDialog = null;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_driver:
                    showToast("跳转司机列表——布局疑问暂放");
                    break;
                case R.id.tv_service:
                    chooseWayDialog(getResources().getString(R.string.call_tech_support), getResources().getString(R.string.support_phone), null, null, new ChooseWayBack());
                    break;
                case R.id.ll_ltinerary_content:
                    String demandUrl = Utils.getDemandUrl(demandSubmit.demand_gid, 2, demandSubmit.quotation_gid);
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra(WebActivity.WEB_EXT_TITLE,"订单详情");
                    intent.putExtra(WebActivity.WEB_EXT_URL,demandUrl);
                    startActivity(intent);
                    break;
                case R.id.tv_quote_quantity:
//                    showToast("车辆详情");
                    startActivity(new Intent(getActivity(), RideDetailsActivity.class));
                    break;
                case R.id.tv_payment:
                    startActivity(new Intent(getActivity(), PayForTripActivity.class));
                    if (resData != null) {
                        EventBus.getDefault().postSticky(resData);
                    }
                    if (demandSubmit != null) {
                        EventBus.getDefault().postSticky(demandSubmit);
                    }
                    break;
                case R.id.tv_map:
//                    showToast("地图开发中");
                    startActivity(new Intent(getActivity(), AMapYunTuActivity.class));
                    break;

            }
        }


        public void chooseWayDialog(String title, String p1, String p2, String p3, ChooseWayDialog.ChooseBack listener) {
            ChooseWayDialog dia = new ChooseWayDialog(getContext());
            dia.setWayBack(listener);
            dia.settitle(title);
            dia.setData(p1, p2, p3);
            dia.show();
        }

        class ChooseWayBack implements ChooseWayDialog.ChooseBack {

            @Override
            public void wayback(int i) {
                Utils.call(getContext(), getResources().getString(R.string.support_phone));
            }
        }
    }
}
