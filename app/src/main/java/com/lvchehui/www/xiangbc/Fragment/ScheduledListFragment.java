package com.lvchehui.www.xiangbc.Fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.ConfirmOrderActivity;
import com.lvchehui.www.xiangbc.activity.freeride.RideDetailsActivity;
import com.lvchehui.www.xiangbc.activity.mine.WebActivity;
import com.lvchehui.www.xiangbc.activity.pay.PayForTripActivity;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseListFragment;
import com.lvchehui.www.xiangbc.base.BasePageAdapter;
import com.lvchehui.www.xiangbc.bean.BaseBean;
import com.lvchehui.www.xiangbc.bean.DemandGetListBean;
import com.lvchehui.www.xiangbc.bean.DemandSubmitDataBean;
import com.lvchehui.www.xiangbc.bean.QuotationGetInfo;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.DateUtil;
import com.lvchehui.www.xiangbc.utils.DateUtils;
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
 * 作用：已预定列表
 */
public class ScheduledListFragment extends BaseListFragment {
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
        return new ScheduledListAdapter();
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
                (String) SPUtil.getInstance(getContext()).get(Constants.USER_GID, ""), 0, 0, "" + 2, this);
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return false;
    }

    class ScheduledListAdapter extends BasePageAdapter {
        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_scheduled, viewGroup, false);
            return new ScheduledItemViewHolder(inflate);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
            if (viewHoder instanceof ScheduledItemViewHolder) {
                ScheduledItemViewHolder holder = (ScheduledItemViewHolder) viewHoder;
                DemandSubmitDataBean dataBean = (DemandSubmitDataBean) mItems.get(position);
                boolean hasTimePicker = (dataBean.use_type <= 2);
                holder.m_tv_dingd_content.setText("订单号:" + dataBean.request_order);
                holder.m_tv_data_time.setText("" + DateUtil.getStrTime(dataBean.begin_time, hasTimePicker));
                holder.m_tv_address_content.setText(dataBean.begin_address);
                ConnectionManager.getInstance().quotationGetinfo(getContext(), dataBean.quotation_gid, new QuotationGetInfoOnDataLoadListener(holder));
                //调取报价的信息，并且显示
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


                holder.m_tv_cancel_dingd.setOnClickListener(new ScheduledListItemOnClick(dataBean));
                holder.m_tv_driver.setOnClickListener(new ScheduledListItemOnClick());
                holder.m_tv_service.setOnClickListener(new ScheduledListItemOnClick());
                holder.m_ll_ltinerary_content.setOnClickListener(new ScheduledListItemOnClick(dataBean));
                holder.m_tv_ltinerary_title.setOnClickListener(new ScheduledListItemOnClick());
                holder.m_tv_pay_off.setOnClickListener(new ScheduledListItemOnClick(dataBean));
            }
        }

        class QuotationGetInfoOnDataLoadListener implements ConnectionUtil.OnDataLoadEndListener {

            private ScheduledItemViewHolder holder;

            public QuotationGetInfoOnDataLoadListener(ScheduledItemViewHolder holder) {
                this.holder = holder;
            }

            @Override
            public void OnLoadEnd(String ret) {
                QuotationGetInfo quotationGetInfo = App.getInstance().getBeanFromJson(ret, QuotationGetInfo.class);
                if (quotationGetInfo.errCode != -1)
                {
                    resData = quotationGetInfo.resData;
                    double restMoney = resData.motorcade_quotation_money - resData.deposit;
                    holder.m_tv_total_money.setText("未付：" + restMoney + "元");
                    holder.m_tv_setoff_time.setText(
                            DateUtil.getDateToStr(resData.begin_time * 1000,
                            "司机号码将在MM月dd日HH:mm前给出"));
                }

            }
        }

        class ScheduledItemViewHolder extends RecyclerView.ViewHolder {

            @ViewInject(R.id.tv_quote_quantity)
            private TextView m_tv_ltinerary_title; //龙翔;

            @ViewInject(R.id.tv_driver)
            private TextView m_tv_driver; //司机;

            @ViewInject(R.id.tv_service)
            private TextView m_tv_service;
             //客服;

            @ViewInject(R.id.ll_ltinerary_content)
            private LinearLayout m_ll_ltinerary_content;

            @ViewInject(R.id.tv_data_time)
            private TextView m_tv_data_time;
            //02月23日 14:23;

            @ViewInject(R.id.tv_address_content)
            private TextView m_tv_address_content;
            //厦门北站商务营运中心3678号5楼享莫某上的方式发送到发送到水电费水电费水电费水电费;

            @ViewInject(R.id.tv_dingd_content)
            private TextView m_tv_dingd_content;
            //订单号：XM201606020001;

            @ViewInject(R.id.tv_ltinerary_type)
            private TextView m_tv_ltinerary_type;
            //单程用车;

            /*@ViewInject(R.id.tv_ltinerary_countdown)
            private TextView m_tv_ltinerary_countdown;
            //未支付300元;*/
            @ViewInject(R.id.tv_total_money)
            private TextView m_tv_total_money;


            @ViewInject(R.id.tv_cancel_dingd)
            private TextView m_tv_cancel_dingd;
            //取消订单;

            @ViewInject(R.id.tv_pay_off)
            private TextView m_tv_pay_off;
             // 付款
            @ViewInject(R.id.tv_setoff_time)
            private TextView m_tv_setoff_time;

            /*@ViewInject(R.id.textView15)
            private TextView m_textView15;*/
            public ScheduledItemViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
            }
        }
    }

    class ScheduledListItemOnClick implements View.OnClickListener {
        private DemandSubmitDataBean demandSubmitBean;

        private ScheduledListItemOnClick() {
        }

        private ScheduledListItemOnClick(DemandSubmitDataBean demandSubmitBean) {
            this.demandSubmitBean = demandSubmitBean;
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
                case R.id.tv_cancel_dingd:
                    cancelDingDan(demandSubmitBean.demand_gid);
                    break;
                case R.id.ll_ltinerary_content:
                    String demandUrl = Utils.getDemandUrl(
                            demandSubmitBean.demand_gid,
                            2,
                            demandSubmitBean.quotation_gid);
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra(WebActivity.WEB_EXT_TITLE,"订单详情");
                    intent.putExtra(WebActivity.WEB_EXT_URL,demandUrl);
                    startActivity(intent);
                    break;
                case R.id.tv_quote_quantity:
                    startActivity(new Intent(getActivity(), RideDetailsActivity.class));
                    break;
                case R.id.tv_pay_off:
                    startActivity(new Intent(getActivity(), PayForTripActivity.class));
                    if (resData != null) {
                        EventBus.getDefault().postSticky(resData);
                    }
                    if (demandSubmitBean != null) {
                        EventBus.getDefault().postSticky(demandSubmitBean);
                    }
                    break;
            }
        }

        private void cancelDingDan(final String demand_gid) {
            customDialog = new CustomDialog(getActivity(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showToast("传入demand_gid");
                    List<String> lists = new ArrayList<>();
                    lists.add("设计让跳转新页面");
                    lists.add("android暂放");
                    ConnectionManager.getInstance()
                            .demandCancel(getContext(),
                                    demand_gid,
                                    new Gson().toJson(lists),
                                    "1",
                                    new DemandCancelOnDataLoadEndListener());
                    customDialog.dismiss();
                }
            });
            customDialog.setTitle("取消订单");
            customDialog.setMessage("如果取消订单，将扣除2分信用值");
            customDialog.setButtonsText("否", "是");
            customDialog.show();
        }

        public void chooseWayDialog(String title, String p1, String p2, String p3, ChooseWayDialog.ChooseBack listener) {
            ChooseWayDialog dia = new ChooseWayDialog(getContext());
            dia.setWayBack(listener);
            dia.settitle(title);
            dia.setData(p1, p2, p3);
            dia.show();
        }

        class DemandCancelOnDataLoadEndListener implements ConnectionUtil.OnDataLoadEndListener {
            @Override
            public void OnLoadEnd(String ret) {
                BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                if (baseBean.errCode != -1) {
                    onRefresh();
                }
            }
        }

        class ChooseWayBack implements ChooseWayDialog.ChooseBack {
            @Override
            public void wayback(int i) {
                Utils.call(getContext(), getResources().getString(R.string.support_phone));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeStickyEvent(DemandSubmitDataBean.class);
        EventBus.getDefault().removeStickyEvent(QuotationGetInfo.ResDataBean.class);
    }
}
