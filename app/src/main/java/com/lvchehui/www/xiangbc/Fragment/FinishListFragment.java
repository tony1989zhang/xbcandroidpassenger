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
import com.lvchehui.www.xiangbc.activity.itinerary.AddEvaActivity;
import com.lvchehui.www.xiangbc.activity.ConfirmOrderActivity;
import com.lvchehui.www.xiangbc.activity.freeride.RideDetailsActivity;
import com.lvchehui.www.xiangbc.activity.mine.WebActivity;
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

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张灿能 on 2016/6/14.
 * 作用：我的行程-已完成列表
 */
public class FinishListFragment extends BaseListFragment {
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
        return new FinishListAdapter();
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
                (String) SPUtil.getInstance(getContext()).get(Constants.USER_GID, ""), 0, 0, "" + 4, this);
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return false;
    }

    class FinishListAdapter extends BasePageAdapter {
        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            final View view = View.inflate(viewGroup.getContext(), R.layout.item_finish, null);
            return new FinishItemViewHolder(view);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
            if (viewHoder instanceof FinishItemViewHolder) {
                FinishItemViewHolder holder = (FinishItemViewHolder) viewHoder;
                DemandSubmitDataBean dataBean = (DemandSubmitDataBean) mItems.get(position);
                boolean hasTimePicker = (dataBean.use_type <= 2);
                holder.m_tv_dingd_content.setText("订单号:" + dataBean.request_order);
                holder.m_tv_data_time.setText("" + DateUtil.getStrTime(dataBean.begin_time, hasTimePicker));
                holder.m_tv_address_content.setText(dataBean.begin_address);


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
                ConnectionManager.getInstance().quotationGetinfo(getContext(),
                        dataBean.quotation_gid,
                        new QuotationGetInfoOnDataLoadListener(holder));

               /* holder.m_tv_team.setText("享包车");
                holder.m_tv_data_time.setText("02月23日  14:23");
                holder.m_tv_address_content.setText("福建省集美区商务运营中心3678号5楼\n" +
                        "详细某某某座位某某某旁边");
                holder.m_tv_money.setText("含税总额：22300元");*/
                holder.m_tv_eva.setOnClickListener(new ScheduledListItemOnClick());
                holder.m_ll_ltinerary_content.setOnClickListener(new ScheduledListItemOnClick(dataBean));
                holder.m_tv_team.setOnClickListener(new ScheduledListItemOnClick());
                holder.m_tv_invoice.setOnClickListener(new ScheduledListItemOnClick());
                /*holder.m_tv_driver.setOnClickListener(new ScheduledListItemOnClick());
                holder.m_tv_service.setOnClickListener(new ScheduledListItemOnClick());*/
            }


        }

        class QuotationGetInfoOnDataLoadListener implements ConnectionUtil.OnDataLoadEndListener {

            private FinishItemViewHolder holder;

            public QuotationGetInfoOnDataLoadListener(FinishItemViewHolder holder) {
                this.holder = holder;
            }

            @Override
            public void OnLoadEnd(String ret) {
                QuotationGetInfo quotationGetInfo = App.getInstance().getBeanFromJson(ret, QuotationGetInfo.class);
                if (quotationGetInfo.errCode != -1)
                {
                    double restMoney = quotationGetInfo.resData.motorcade_quotation_money - quotationGetInfo.resData.deposit;
                    holder.m_tv_money.setText("含税总额：" + restMoney + "元");
                }

            }
        }
        class FinishItemViewHolder extends RecyclerView.ViewHolder {
            @ViewInject(R.id.tv_team)
            private TextView m_tv_team; //龙翔客运;

            @ViewInject(R.id.tv_passenger_had_conceled)
            private TextView m_tv_passenger_had_conceled; //乘客已取消;

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

            @ViewInject(R.id.tv_money)
            private TextView m_tv_money; //含税总额：22300元;

            @ViewInject(R.id.tv_eva)
            private TextView m_tv_eva; //追加评价;

            @ViewInject(R.id.tv_invoice)
            private TextView m_tv_invoice; //发票;


            public FinishItemViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this,itemView);
            }
        }


        class ScheduledListItemOnClick implements View.OnClickListener{
            DemandSubmitDataBean dataBean;
            public ScheduledListItemOnClick(){

            }
            public ScheduledListItemOnClick(DemandSubmitDataBean dataBean){
                this.dataBean = dataBean;
            }
            private CustomDialog customDialog = null;
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.tv_driver:
                        showToast("跳转司机列表——布局疑问暂放");
                        break;
                    case R.id.tv_service:
                        chooseWayDialog(getResources().getString(R.string.call_tech_support), getResources().getString(R.string.support_phone), null, null,new ChooseWayBack());
                        break;
                    case R.id.ll_ltinerary_content:

                        String demandUrl = Utils.getDemandUrl(dataBean.demand_gid, 2, dataBean.quotation_gid);
//                        startActivity(new Intent(getActivity(), ConfirmOrderActivity.class));
                        Intent intent = new Intent(getActivity(), WebActivity.class);
                        intent.putExtra(WebActivity.WEB_EXT_TITLE,"订单详情");
                        intent.putExtra(WebActivity.WEB_EXT_URL,demandUrl);
                        startActivity(intent);
                        break;
                    case R.id.tv_quote_quantity:
                        //showToast("车辆详情");
                        startActivity(new Intent(getActivity(), RideDetailsActivity.class));
                        break;
                    case R.id.tv_payment:
                        showToast("付款开发中");
                        break;
                    case R.id.tv_map:
                      //  showToast("地图开发中");
                        break;
                    case R.id.tv_eva:
                        startActivity(new Intent(getActivity(), AddEvaActivity.class));
                         break;
                    case R.id.tv_invoice:
                        showToast("发票开发中");
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

            class ChooseWayBack implements ChooseWayDialog.ChooseBack{

                @Override
                public void wayback(int i) {
                    Utils.call(getContext(), getResources().getString(R.string.support_phone));
                }
            }
        }
    }
}
