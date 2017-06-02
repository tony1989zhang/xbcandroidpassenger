package com.lvchehui.www.xiangbc.Fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.ConfirmOrderActivity;
import com.lvchehui.www.xiangbc.activity.itinerary.QuotationedListActivity;
import com.lvchehui.www.xiangbc.activity.mine.WebActivity;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseListFragment;
import com.lvchehui.www.xiangbc.base.BasePageAdapter;
import com.lvchehui.www.xiangbc.bean.BaseBean;
import com.lvchehui.www.xiangbc.bean.DemandGetListBean;
import com.lvchehui.www.xiangbc.bean.DemandSubmitDataBean;
import com.lvchehui.www.xiangbc.bean.QuotationedCountBean;
import com.lvchehui.www.xiangbc.impl.OnOperationListener;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.DateUtil;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.utils.Utils;
import com.lvchehui.www.xiangbc.view.dialog.CustomDialog;
import com.lvchehui.www.xiangbc.view.timeCountDown.CountdownView;

import org.simple.eventbus.EventBus;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 张灿能 on 2016/6/2.
 * 作用：报价中
 */
public class QuoteListFragment extends BaseListFragment {
    private Timer timer;
    private TimerTask task;

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    protected List convertToBeanList(String json) {

        DemandGetListBean demandGetListBean = App.getInstance().getBeanFromJson(json, DemandGetListBean.class);
        return demandGetListBean.resData;
    }

    @Override
    protected BasePageAdapter initAdapter() {
        return new QuoteAdapter();
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
        return ConnectionManager.getInstance().demandGetList(
                getContext(),(String) SPUtil.getInstance(getContext()).get(Constants.USER_GID,""),
                start,
                1000,this);
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return false;
    }


    class QuoteAdapter extends BasePageAdapter{
        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
//            View view = View.inflate(viewGroup.getContext(), R.layout.item_quote, null);
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.item_quote, viewGroup, false);
            return new QuoteItemViewHolder(inflate);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
            if (viewHoder instanceof QuoteItemViewHolder) {
                final DemandSubmitDataBean demandSubmitDataBean = (DemandSubmitDataBean) mItems.get(position);
                QuoteItemViewHolder holder = (QuoteItemViewHolder) viewHoder;
                holder.m_tv_dingd_content.setText("订单号:" + demandSubmitDataBean.request_order);
                holder.m_tv_ltinerary_countdown.start(2 * 60 * 60 * 1000); // Millisecond
                boolean hasTimePicker = (demandSubmitDataBean.use_type <= 2);
                holder.m_tv_data_time.setText("" + DateUtil.getStrTime(demandSubmitDataBean.begin_time, hasTimePicker));
                holder.m_tv_address_content.setText("" + demandSubmitDataBean.begin_address);

                int use_type = demandSubmitDataBean.use_type;
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

                QuoteOnClickListener quoteOnClickListener = new QuoteOnClickListener(demandSubmitDataBean);
                holder.m_tv_quote_quantity.setOnClickListener(quoteOnClickListener);
                holder.m_tv_cancel_dingd.setOnClickListener(quoteOnClickListener);
                holder.m_ll_ltinerary_content.setOnClickListener(quoteOnClickListener);
                ConnectionManager.getInstance().quotationGetquotationedcount(getContext(),
                        demandSubmitDataBean.demand_gid,
                        new QuotationedCountOnDataLoadEndListener(holder.m_tv_quote_quantity));
                /**
                 * 2016/09/06 zcn 10:10
                 * */
                if (null != demandSubmitDataBean) {
                    if (timer == null) {
                        timer = new Timer();
                    }
                    task = new TimerTask() {

                        @Override
                        public void run() {
                            Message message = new Message();
                            message.what = 1;
                            message.obj = demandSubmitDataBean.demand_gid;
                            handler.sendMessage(message);
                        }
                    };
                    timer.schedule(task, 1000, 60 * 1000);
                }
            }
        }

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String demandGid= (String) msg.obj;
                ConnectionManager.getInstance().demandDealPush(getContext(),
                        demandGid,
                        new DemandDealPushOnDataLoad());
            }
        };

        class DemandDealPushOnDataLoad implements ConnectionUtil.OnDataLoadEndListener {

            @Override
            public void OnLoadEnd(String ret) {
                BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                showToast("正在推送:" + baseBean.resMsg);
            }
        }
        class QuoteOnClickListener implements View.OnClickListener{
//            private int position;
//            public QuoteOnClickListener(int position){
//                this.position = position;
//            }
            private DemandSubmitDataBean demandSubmitDataBean;
            public QuoteOnClickListener(DemandSubmitDataBean demandSubmitDataBean)
            {
                this.demandSubmitDataBean = demandSubmitDataBean;
            }
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.tv_quote_quantity:
                        startActivity(new Intent(getActivity(), QuotationedListActivity.class));
                        EventBus.getDefault().postSticky(demandSubmitDataBean.demand_gid);
                        break;
                    case R.id.tv_cancel_dingd:
                        final CustomDialog customDialog = new CustomDialog(getContext());
                        customDialog.setTitle("取消订单");
                        customDialog.setMessage("取消订单将扣除信用值");
                        customDialog.setButtonsText("取消", "确认");
                        customDialog.show();
                        customDialog.setOperationListener(new OnOperationListener() {
                            @Override
                            public void onLeftClick() {
                                customDialog.dismiss();
                            }

                            @Override
                            public void onRightClick() {
                                showToast("传入demand_gid");
                                List<String> lists = new ArrayList<>();
                                lists.add("设计让跳转新页面");
                                lists.add("android暂放");
                                ConnectionManager.getInstance()
                                        .demandCancel(getContext(),
                                                demandSubmitDataBean.demand_gid,
                                               new Gson().toJson(lists),
                                                "1",
                                new DemandCancelOnDataLoadEndListener());
                                customDialog.dismiss();
                            }
                        });
                        break;
                    case R.id.ll_ltinerary_content:
                        Intent intent = new Intent(getActivity(), WebActivity.class);
                        String demandUrl = Utils.getDemandUrl(demandSubmitDataBean.demand_gid, 0);
                        intent.putExtra(WebActivity.WEB_EXT_TITLE, "订单详情");
                        intent.putExtra(WebActivity.WEB_EXT_URL,demandUrl);
                        startActivity(intent);
                        break;
                }
            }
        }


        class QuotationedCountOnDataLoadEndListener implements ConnectionUtil.OnDataLoadEndListener{

            private TextView tv;
            private QuotationedCountOnDataLoadEndListener(TextView tv)
            {
                this.tv = tv;
            }

            @Override
            public void OnLoadEnd(String ret) {
                QuotationedCountBean quotationedCount = App.getInstance().getBeanFromJson(ret, QuotationedCountBean.class);
                if (quotationedCount.errCode != -1) {
                    int resData = quotationedCount.resData;
                    String str="已报价 <font color='#FF0000'><big>"+  resData +"</big></font>  家";
                    tv.setText(Html.fromHtml(str));
                }
            }
        }

        class DemandCancelOnDataLoadEndListener implements ConnectionUtil.OnDataLoadEndListener{

            @Override
            public void OnLoadEnd(String ret) {
                BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                if (baseBean.errCode != -1)
                {
                    onRefresh();
                }

            }
        }

        class QuoteItemViewHolder extends RecyclerView.ViewHolder {
            @ViewInject(R.id.tv_quote_quantity)
            private TextView m_tv_quote_quantity; //查看已报价;

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

            @ViewInject(R.id.tv_ltinerary_countdown)
            private CountdownView m_tv_ltinerary_countdown;
            @ViewInject(R.id.tv_cancel_dingd)
            private TextView m_tv_cancel_dingd; //取消订单;



            public QuoteItemViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this,itemView);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        if (task !=null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }
}
